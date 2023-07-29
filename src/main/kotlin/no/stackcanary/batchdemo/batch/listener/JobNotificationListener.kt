package no.stackcanary.batchdemo.batch.listener

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component

/**
 * Listener bean which can hold logic that runs before or after a job. For example,
 * if you have your own database tables for storing batch state (i.e., not using the default spring batch
 * table structure), you can implement calls to this service/procedure before and after the job to log job parameters,
 * execution time, job status etc.
 */
@Component
class JobNotificationListener(): JobExecutionListener {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(JobNotificationListener::class.java)
    }

    override fun beforeJob(jobExecution: JobExecution) {
        log.info("Starting batch job")
        super.beforeJob(jobExecution)
    }

    override fun afterJob(jobExecution: JobExecution) {
        log.info("Batch job finished with status ${jobExecution.status}")
        super.afterJob(jobExecution)
    }
}