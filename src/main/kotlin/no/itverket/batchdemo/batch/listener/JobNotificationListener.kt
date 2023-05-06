package no.itverket.batchdemo.batch.listener

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component

@Component
class JobNotificationListener(): JobExecutionListener {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(JobNotificationListener::class.java)
    }

    override fun beforeJob(jobExecution: JobExecution) {
        super.beforeJob(jobExecution)
    }

    override fun afterJob(jobExecution: JobExecution) {
        super.afterJob(jobExecution)
    }
}