package no.itverket.batchDemo.batch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
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
}