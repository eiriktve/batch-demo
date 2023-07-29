package no.stackcanary.batchdemo.batch

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("batch")
data class BatchProps (
    val inputFileRelativePath: String? = null
)