import java.time.LocalDate
import java.time.LocalTime
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
    val checkedInDate: LocalDate,
    val checkedInTime: LocalTime,
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
        println("Invalid input. Please ${displayMessage}")
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
        val checkInDate = getValidCheckInDate()
        val checkInTime = LocalTime.now()

        if (hasCheckedIn(checkInDate, employeeCheckInId)){
            println("You already CheckedIn!")
        }
        else{
            val attendance = EmployeeAttendance(
                employeeCheckInId, checkInDate, checkInTime
            )
            attendanceLog.add(attendance)

            println("Checked In Successfully (${checkInDate})!")
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
fun getValidCheckInDate(): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    println("Enter check-in date (dd-MM-yyyy) or press Enter for today:")

    val input = readln().trim()

    return if (input.isEmpty()) {
        LocalDate.now()
    } else {
        try {
            val enteredDate = LocalDate.parse(input, formatter)
            if (enteredDate.isAfter(LocalDate.now())) {
                println("Future date is not allowed.")
                getValidCheckInDate()
            } else {
                enteredDate
            }
        } catch (e: Exception) {
            println("Invalid date format.")
            getValidCheckInDate()
        }
    }
}


// It is used to check whether they already checkedIn or not using attendanceLog
fun hasCheckedIn(date: LocalDate, id: Int): Boolean {
    return attendanceLog.any { it.checkedInDate == date && it.employeeId == id }
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
