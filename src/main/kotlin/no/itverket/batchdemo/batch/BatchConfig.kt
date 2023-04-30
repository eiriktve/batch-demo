package no.itverket.batchdemo.batch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.jdbc.support.JdbcTransactionManager
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
class BatchConfig {

    @Bean
    fun batchDataSource(): DataSource =
        EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
            .addScript("/org/springframework/batch/core/schema-h2.sql")
            .generateUniqueName(true)
            .build()

    @Bean
    fun batchTransactionManager(): JdbcTransactionManager = JdbcTransactionManager(batchDataSource())

    /**
     * Automatisk oppstart av jobben når applikasjonen kjøres.
     */
    @Bean
    fun jobLauncherApplicationRunner(
        jobLauncher: JobLauncher,
        jobExplorer: JobExplorer,
        jobRepository: JobRepository
    ): JobLauncherApplicationRunner = JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository)
}