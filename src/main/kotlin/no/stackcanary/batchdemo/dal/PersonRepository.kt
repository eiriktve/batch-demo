package no.stackcanary.batchdemo.dal

import no.stackcanary.batchdemo.dal.model.Person
import no.stackcanary.batchdemo.dal.table.PersonTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.Instant
import javax.sql.DataSource

@Repository
class PersonRepository(private val dataSource: DataSource) {
    init {
        Database.connect(dataSource)
    }

    /**
     * Inserts a person into the database.
     *
     * Note: It's up to you if you want to use Spring's transaction management with @Transactional,
     * or if you want to use Exposed's transaction management with 'transaction'. The latter
     * lets you more easily put a logger on the database calls.
     *
     * @param person person to be stored
     */
    fun storePerson(person: Person) {
        transaction {
            addLogger(StdOutSqlLogger)
            PersonTable.insert {
                it[firstName] = person.firstName
                it[lastName] = person.lastName
                it[age] = person.age
                it[email] = person.email
                it[company] = person.company
                it[street] = person.street
                it[city] = person.city
                it[zipCode] = person.zipCode
                it[phone] = person.phone
                it[creationDate] = Instant.now()
                it[lastUpdated] = Instant.now()
            }
        }
    }
}