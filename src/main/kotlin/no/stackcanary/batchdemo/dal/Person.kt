package no.stackcanary.batchdemo.dal

/**
 * Person model.
 *
 * FlatFileItemReaderBuilder used by the batch reader uses reflection
 * to set the fields, hence we need a noargs constructor and setters
 * for this to work seamlessly with the job, as opposed to using a data class.
 */
class Person(
    var firstName: String = "",
    var lastName: String = "",
    var age: Int = -1,
    var email: String = "",
    var company: String = "",
    var street: String = "",
    var city: String = "",
    var zipCode: Short = -1,
    var phone: String = ""
)

