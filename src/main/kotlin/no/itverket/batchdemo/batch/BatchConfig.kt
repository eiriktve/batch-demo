package no.itverket.batchdemo.batch

import no.itverket.batchdemo.batch.listener.JobNotificationListener
import no.itverket.batchdemo.dal.Person
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
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.mapping.DefaultLineMapper
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
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
import java.util.*
import javax.sql.DataSource

/**
 * Konfigurasjon av batchjobben.
 *
 * Konfigurert deklarativt med @EnableBatchProcessing. Alternativt kan man extende DefaultBatchConfiguration og
 * override de metodene man ønsker, f.eks getDataSource().
 *
 * Notat: Med spring batch 5 (Spring Boot 3) så kan man droppe både EnableBatchConfiguration og
 * DefaultBatchConfiguration. Spring vil selv skjønne at dette er en batch-applikasjon, og vil autokonfigureres
 * deretter.
 */
@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource", transactionManagerRef = "batchTransactionManager")
class BatchConfig(@Qualifier("playGroundDataSource") private val dataSource: DataSource) {

    @Bean
    fun importJob(
        listener: JobNotificationListener,
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,

    ): Job {
        return JobBuilder("ImportJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .flow(ioStep(jobRepository, transactionManager))
            .end()
            .build()
    }

    @Bean
    fun ioStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Step {
        return StepBuilder("IOStep", jobRepository)
            .chunk<Person, Person>(10, transactionManager)
            .reader(personItemReader())
            .processor(personItemProcessor())
            .writer(personItemWriter(dataSource))
            .build()
    }

    @Bean
    fun personItemReader(): FlatFileItemReader<Person> {
        val reader = FlatFileItemReader<Person>()
        reader.setResource(ClassPathResource("people.csv"))
        reader.setLinesToSkip(1) // skip header row
        reader.setLineMapper(DefaultLineMapper<Person>().apply {
            setLineTokenizer(DelimitedLineTokenizer().apply {
                setNames("first_name", "last_name", "age", "email", "company", "street", "city", "zip_code", "phone")
            })
            setFieldSetMapper(BeanWrapperFieldSetMapper<Person>().apply {
                setTargetType(Person::class.java)
            })
        })
        return reader
    }

    @Bean
    fun personItemProcessor(): ItemProcessor<Person, Person> {
        return ItemProcessor { person ->
            person.copy(firstName = person.firstName.uppercase(Locale.getDefault()))
        }
    }

    @Bean
    fun personItemWriter(dataSource: DataSource): JdbcBatchItemWriter<Person> {
        val writer = JdbcBatchItemWriter<Person>()
        writer.setItemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider())
        writer.setSql(
            "INSERT INTO Person (first_name, last_name, age, email, company, street, city, zip_code, phone) " +
                    "VALUES (:first_name, :last_name, :age, :email, :company, :street, :city, :zip_code, :phone)"
        )
        writer.setDataSource(dataSource)
        return writer
    }

    /**
     * DataSource som brukes spesifikt av JobRepository. Lagrer metadata knytte til selve jobben, som informasjon
     * om kjøring, ExitCode, m.m. Vi gidder ikke sette opp dette i egen database, så vi bare kaster det inn i en
     * midlertidig in-memory db.
     *
     * Notat: Ikke utenkelig at du ønsker en annen datasource for resten av applikasjonen din, i så fall kan du f.eks
     * lage en egen @Configuration med en @Primary DataSource.
     * @see DatabaseConfiguration
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
     * Automatisk oppstart av jobben når applikasjonen kjøres.
     */
    @Profile("!test") // tester har sin egen job launcher
    @Bean
    fun jobLauncherApplicationRunner(
        jobLauncher: JobLauncher,
        jobExplorer: JobExplorer,
        jobRepository: JobRepository
    ): JobLauncherApplicationRunner = JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository)
}