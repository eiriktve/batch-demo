package no.itverket.batchdemo.dal

data class Person(
    val firstName: String,
    val lastName: String,
    val age: Int,
    val email: String,
    val company: String,
    val street: String,
    val city: String,
    val zipCode: Int,
    val phone: String
)