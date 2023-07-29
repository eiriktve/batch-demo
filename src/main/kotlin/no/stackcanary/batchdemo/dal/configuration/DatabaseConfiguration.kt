package no.stackcanary.batchdemo.dal.configuration

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
    fun playGroundDataSource(): DataSource = playGroundDataSourceProperties()
        .initializeDataSourceBuilder()
        .build()
}