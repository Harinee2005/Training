import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Data class to hold Employee Details
data class Employee(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val role: String,
    val contactNumber: Long,
    val reportingTo: Int,
)

// Data class to hold Employee Attendance
data class EmployeeAttendance(
    val employeeId: Int,
    val checkInDateTime: LocalDateTime,
)

val employeeDetails = mutableMapOf<Int, Employee>()
var employeeId = 0

// Adding employee to the employeeDetails map
fun addEmployee(){
    val firstName = readNonEmptyInput("Enter employee ${employeeId + 1} first name:")
    val lastName = readNonEmptyInput("Enter $firstName's last name:")
    val role = readNonEmptyInput("Enter $firstName's role:")
    val contactNo = readValidLong("Enter $firstName's contact number:")
    val reportingToId = readValidInt("Enter $firstName's reporting to id:")

    employeeDetails[employeeId] = Employee(employeeId, firstName, lastName, role, contactNo, reportingToId)

    employeeId++
}

// To avoid empty inputs
fun readNonEmptyInput(displayMessage: String): String{
    while (true){
        println(displayMessage)
        val input = readln()
        if (input.isNotEmpty()) {
            return input
        }
        println("Invalid input. Please ${displayMessage}!")
    }
}

// To validate Long
fun readValidLong(displayMessage: String): Long {
    println(displayMessage)
    var value = readln().toLongOrNull()
    while (value == null) {
        println("Invalid input. Please $displayMessage")
        value = readln().toLongOrNull()
    }
    return value
}

// To validate Int
fun readValidInt(displayMessage: String): Int {
    println(displayMessage)
    var value = readln().toIntOrNull()
    while (value == null) {
        println("Invalid input. Please ${displayMessage}!")
        value = readln().toIntOrNull()
    }
    return value
}


// List out employeeId and their name
fun listEmployee() {
    for ((id, employee) in employeeDetails) {
        println("Employee ID: $id, Name: ${employee.firstName} ${employee.lastName}")
    }
}

// Holds checkedIn with id, date and time
val attendanceLog = mutableListOf<EmployeeAttendance>()

// Validates and stores checkedIn details
fun createCheckIn() {
    println("-----------------------------------------------------------------")
    println("Check In.....")

    val employeeCheckInId = readValidInt("Enter your ID:")

    if (validateId(employeeCheckInId)){
        val checkInDateTime = getValidCheckInDateTime()

        if (hasCheckedIn(checkInDateTime, employeeCheckInId)){
            println("You already CheckedIn!")
        }
        else{
            val attendance = EmployeeAttendance(
                employeeCheckInId, checkInDateTime,
            )
            attendanceLog.add(attendance)

            println("Checked In Successfully!")
        }
    }

    else{
        println("Employee ID is invalid")
    }
}

// Check whether the employee id is valid
fun validateId(id: Int): Boolean{
    return employeeDetails.containsKey(id)
}

// Validate input date
fun getValidCheckInDateTime(): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    println("Enter check-in date and time (dd-MM-yyyy HH:mm) or press Enter for today:")

    val input = readln().trim()

    return if (input.isEmpty()) {
        LocalDateTime.now()
    }
    else {
        try {
            val enteredDateTime = LocalDateTime.parse(input, formatter)
            if (enteredDateTime.isAfter(LocalDateTime.now())) {
                println("Future date is not allowed.")
                getValidCheckInDateTime()
            } else {
                enteredDateTime
            }
        } catch (e: Exception) {
            println("Invalid date format.")
            getValidCheckInDateTime()
        }
    }
}


// It is used to check whether they already checkedIn or not using attendanceLog
fun hasCheckedIn(dateTime: LocalDateTime, id: Int): Boolean {
    val checkInDate = dateTime.toLocalDate()
    return attendanceLog.any {
        it.employeeId == id && it.checkInDateTime.toLocalDate() == checkInDate
    }
}

fun main(){
    println("Add Employee")
    while (true) {
        addEmployee()

        println("Do you want to add another employee? (y/n):")
        val choice = readln()
        if (choice.lowercase() != "y") {
            break
        }
    }

    println("Employee Details added successfully!")
    println("-----------------------------------------------------------------")
    println("Employee List:")
    listEmployee()

    while(true){
        createCheckIn()

        println("Type 'exit' to stop or press Enter to continue:")
        val userInput = readln()

        if (userInput.lowercase() == "exit") {
            println("Exiting check-in!")
            println("-----------------------------------------------------------------")
            break
        }
    }
}
