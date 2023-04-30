package no.itverket.batchdemo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication
class BatchDemoApplication

fun main(args: Array<String>) {
	// Returner jobben sin exit code som applikasjonen sin process exit code
	exitProcess(SpringApplication.exit(runApplication<BatchDemoApplication>(*args)))
}
