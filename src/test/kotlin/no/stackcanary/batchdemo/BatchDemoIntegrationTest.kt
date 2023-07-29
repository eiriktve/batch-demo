package no.stackcanary.batchdemo

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import no.stackcanary.batchdemo.batch.BatchProps
import no.stackcanary.batchdemo.batch.JOB_NAME
import no.stackcanary.batchdemo.dal.PersonRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.JobRepositoryTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles


@SpringBootTest
@SpringBatchTest // provides the launcher and repository test utils
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
class BatchDemoIntegrationTest {

    @Autowired(required = false)
    private lateinit var jobLauncher: JobLauncherTestUtils

    @Autowired(required = false)
    private lateinit var jobRepository: JobRepositoryTestUtils

    @MockkBean
    private lateinit var personRepositoryMock: PersonRepository

    @BeforeEach
    fun setup() {
        jobRepository.removeJobExecutions()
        every { personRepositoryMock.storePerson(any()) } returns Unit
    }

    @Test
    fun `Job with valid input should complete without errors`() {
        val jobExecution: JobExecution = jobLauncher.launchJob()
        val jobInstance = jobExecution.jobInstance
        val jobExitStatus = jobExecution.exitStatus

        assertEquals(JOB_NAME, jobInstance.jobName)
        assertEquals(ExitStatus.COMPLETED.exitCode, jobExitStatus.exitCode)
        verify(atLeast = 3) { personRepositoryMock.storePerson(any())  }
    }
}