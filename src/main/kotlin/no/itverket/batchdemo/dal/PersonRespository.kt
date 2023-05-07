package no.itverket.batchdemo.dal

import no.itverket.batchdemo.dal.table.PersonTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import javax.sql.DataSource

@Repository
class PersonRespository(dataSource: DataSource) {
    init {
        Database.connect(dataSource)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(PersonRespository::class.java)
    }

    /**
     * Lagrer en person til databasen.
     *
     * Notat: Det er opp til deg om du vil bruke Spring sin transaction management med @Transactional, eller
     * Exposed sin transaction management med 'transaction {}'. Sistnevnte lar deg enkelt legge til en logger
     * på databasekallene dine.
     *
     * @param person personen som skal lagres
     */
    fun storePerson(person: Person) {
        log.info("Inserting person into database: ${person.firstName} ${person.lastName}")
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