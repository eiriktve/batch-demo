package no.stackcanary.batchdemo

import no.stackcanary.batchdemo.batch.BatchProps
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@EnableConfigurationProperties(BatchProps::class)
@SpringBootApplication
class BatchDemoApplication

fun main(args: Array<String>) {
	// Return the batch ExitCode as the application's process exit code
	exitProcess(SpringApplication.exit(runApplication<BatchDemoApplication>(*args)))
}
