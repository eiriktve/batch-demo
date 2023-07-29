package no.stackcanary.batchdemo.error

/**
 * Batch related runtime exceptions related to the execution of this job
 */
class BatchRuntimeException(message: String): RuntimeException(message)