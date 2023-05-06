package no.itverket.batchdemo.dal.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
class DatabaseConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource")
    fun playGroundDataSourceProperties(): DataSourceProperties = DataSourceProperties()

    @Bean
    @Primary
    @Qualifier("playGroundDataSource")
    fun playGroundDataSource(): DataSource = playGroundDataSourceProperties().initializeDataSourceBuilder().build()
}