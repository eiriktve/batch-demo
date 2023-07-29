package no.stackcanary.batchdemo.batch

import no.stackcanary.batchdemo.batch.listener.JobNotificationListener
import no.stackcanary.batchdemo.dal.PersonRepository
import no.stackcanary.batchdemo.dal.model.Person
import no.stackcanary.batchdemo.error.BatchRuntimeException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.jdbc.support.JdbcTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

/**
 * Configuration of the batch job.
 *
 * Declarative configuration with @EnableBatchProcessing. Alternatively, you can extend DefaultBatchConfiguration and
 * override the methods you want to configure, such as getDataSource()
 *
 * Note: It's not a requirement in Spring Batch 5 (Spring Boot 3) to use EnableBatchConfiguration or
 * DefaultBatchConfiguration. Spring will know that this is a batch application and set it up through
 * spring autoconfiguration. However, if you want something that's not straight out of the box, this
 * is a good approach.
 *
 */
@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource", transactionManagerRef = "batchTransactionManager")
class BatchConfig(
    private val batchProps: BatchProps,
    private val personRepository: PersonRepository
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(BatchConfig::class.java)
    }

    @Bean
    fun job(
        listener: JobNotificationListener,
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
        ): Job {
        return JobBuilder(JOB_NAME, jobRepository)
            .incrementer(RunIdIncrementer())
            .listener(listener)
            .flow(ioStep(jobRepository, transactionManager))
            .end()
            .build()
    }

    @Bean
    fun ioStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Step {
        return StepBuilder(STEP_NAME, jobRepository)
            .chunk<Person, Person>(10, transactionManager)
            .reader(personItemReader())
            .processor(personItemProcessor())
            .writer(personItemWriter())
            .build()
    }

    @Bean
    fun personItemReader(): FlatFileItemReader<Person> {
        val filePath: String =
            batchProps.inputFileRelativePath ?: throw BatchRuntimeException("Input file not provided")

        return FlatFileItemReaderBuilder<Person>()
            .resource(ClassPathResource(filePath))
            .saveState(false)
            .targetType(Person::class.java)
            .linesToSkip(1)
            .delimited()
            .delimiter(",")
            .names("first_name", "last_name", "age", "email", "company", "street", "city", "zip_code", "phone")
            .fieldSetMapper(FieldSetMapper { fieldSet ->
                Person(
                    fieldSet.readString("first_name"),
                    fieldSet.readString("last_name"),
                    fieldSet.readInt("age"),
                    fieldSet.readString("email"),
                    fieldSet.readString("company"),
                    fieldSet.readString("street"),
                    fieldSet.readString("city"),
                    fieldSet.readShort("zip_code"),
                    fieldSet.readString("phone")
                )
            })
            .build()
    }

    /**
     * Define a processor like this if you want to do any form of transformation on the data
     * before propagating it to the writer. Currently this does nothing, it just returns the
     * person it gets from the reader
     */
    @Bean
    fun personItemProcessor(): ItemProcessor<Person, Person> {
        return ItemProcessor { it }
    }

    /**
     * Writer responsible for writing the person to the database.
     *
     * If you don't have any other database dependencies in your application, you could define
     * your db operations directly in this bean with a JdbcBatchItemWriter.
     *
     * This bean shows how you can weave together different types of writers, for example if you
     * want to have a more customized implementation using a repository class.
     */
    @Bean
    fun personItemWriter(): ItemWriter<Person> =
        ItemWriter<Person> {
            it.items.forEach(personRepository::storePerson)
        }


    /**
     * DataSource used specifically by the Spring Batch JobRepository. This datasource is used to store
     * metadata related to the execution of the job. As we don't currently want the spring batch tables
     * in our database, we configure this to just store the job information in an in-memory db.
     *
     * Note: it wouldn't be unusual to want another datasource elsewhere in your application, in which case you
     * can create a different datasource bean which you annotate with @Primary
     *
     * @see no.stackcanary.batchdemo.dal.configuration.DatabaseConfiguration
     */
    @Qualifier("batchDataSource")
    @Bean
    fun batchDataSource(): DataSource =
        EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
            .addScript("/org/springframework/batch/core/schema-h2.sql")
            .generateUniqueName(true)
            .build()

    @Bean
    fun batchTransactionManager(@Qualifier("batchDataSource") dataSource: DataSource): JdbcTransactionManager =
        JdbcTransactionManager(dataSource)

    /**
     * Automatically start the job when the application runs.
     */
    @Profile("!test") // Tests have their own job launcher
    @Bean
    fun jobLauncherApplicationRunner(
        jobLauncher: JobLauncher,
        jobExplorer: JobExplorer,
        jobRepository: JobRepository
    ): JobLauncherApplicationRunner = JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository)
}