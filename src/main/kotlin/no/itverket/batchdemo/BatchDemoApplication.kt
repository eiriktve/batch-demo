package no.itverket.batchdemo

import no.itverket.batchdemo.batch.BatchProps
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@EnableConfigurationProperties(BatchProps::class)
@SpringBootApplication
class BatchDemoApplication

fun main(args: Array<String>) {
	// Returner jobben sin exit code som applikasjonen sin process exit code
	exitProcess(SpringApplication.exit(runApplication<BatchDemoApplication>(*args)))
}
