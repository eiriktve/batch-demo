package no.stackcanary.batchdemo.dal.table

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object PersonTable : Table("person") {
    private val id: Column<Int> = integer("id")
    val firstName: Column<String> = varchar("first_name", 30)
    val lastName: Column<String> = varchar("last_name", 30)
    val age: Column<Int> = integer("age")
    val email: Column<String> = varchar("email", 60)
    val company: Column<String> = varchar("company", 60)
    val street: Column<String> = varchar("street", 50)
    val city: Column<String> = varchar("city", 30)
    val zipCode: Column<Short> = short("zip_code")
    val phone: Column<String> = varchar("phone", 30)
    val creationDate: Column<Instant> = timestamp("creation_date")
    val lastUpdated: Column<Instant> = timestamp("last_updated")
    override val primaryKey = PrimaryKey(id) // auto increments on insert
}