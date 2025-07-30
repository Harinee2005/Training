import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Data Classes to hold employee data and attendance data
data class DataEmployee(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val role: String,
    val reportingTo: Int,
)

data class DataAttendance(
    val employeeId: Int,
    val checkInDateTime: LocalDateTime,
)

// Classes to manage employee and attendance
class Employee {
    private val employeeDetails = mutableMapOf<Int, DataEmployee>()
    private var employeeIdCounter = 101

    fun addEmployee(firstName: String, lastName: String, role: String, reportingToId: Int) {
        val employee = DataEmployee(employeeIdCounter, firstName, lastName, role,reportingToId)
        employeeDetails[employeeIdCounter] = employee
        employeeIdCounter++
    }

    fun listAllEmployees(): List<DataEmployee> {
        return employeeDetails.values.toList()
    }

    fun isValidEmployeeId(id: Int): Boolean {
        return employeeDetails.containsKey(id)
    }
}

class Attendance() {
    private val attendanceLog = mutableListOf<DataAttendance>()

    fun checkIn(employeeId: Int, dateTime: LocalDateTime): Boolean {
        val hasAlreadyCheckedIn = hasCheckedIn(employeeId, dateTime)
        if (!hasAlreadyCheckedIn) {
            attendanceLog.add(DataAttendance(employeeId, dateTime))
        }
        return !hasAlreadyCheckedIn
    }

    private fun hasCheckedIn(employeeId: Int, dateTime: LocalDateTime): Boolean {
        val checkInDate = dateTime.toLocalDate()
        return attendanceLog.any {
            it.employeeId == employeeId && it.checkInDateTime.toLocalDate() == checkInDate
        }
    }
}

// Input validations
fun validateNonEmptyInput(input: String): Boolean {
    val trimmed = input.trim()
    return trimmed.isNotEmpty()
}

fun validateIntInput(input: String): Pair<Boolean, Int?> {
    val number = input.toIntOrNull()
    return if (number != null) {
        Pair(true, number)
    } else {
        Pair(false, null)
    }
}

// Date time validations
fun validateDateTime(input: String): Pair<Boolean, LocalDateTime?> {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    return if (input.isBlank()) {
        Pair(true, LocalDateTime.now())
    } else {
        try {
            val parsed = LocalDateTime.parse(input, formatter)
            if (parsed.isAfter(LocalDateTime.now())){
                Pair(false, null)
            }
            else {
                Pair(true, parsed)
            }
        } catch (e: Exception) {
            Pair(false, null)
        }
    }
}

// Main function
fun main() {
    val employee = Employee()
    val attendance = Attendance()

    employee.addEmployee("John", "Doe", "Manager", 0)

    while (true) {
        println("Menu: \n1. AddEmployee \n2. List Employees \n3. Check-In \n4. Exit \nEnter your choice: ")
        when (readln().trim()) {
            "1" -> {
                println("Add Employees:")
                while (true) {
                    var firstName = ""
                    while (!validateNonEmptyInput(firstName)) {
                        println("Enter first name:")
                        firstName = readln()
                        if (!validateNonEmptyInput(firstName)) {
                            println("Invalid input.")
                        }
                    }

                    var lastName = ""
                    while (!validateNonEmptyInput(lastName)){
                        println("Enter last name:")
                        lastName = readln()
                        if (!validateNonEmptyInput(lastName)){
                            println("Invalid input.")
                        }
                    }

                    var role = ""
                    while (!validateNonEmptyInput(role)){
                        println("Enter role:")
                        role = readln()
                        if (!validateNonEmptyInput(role)){
                            println("Invalid input.")
                        }
                    }

                    var reportingTo: Int? = null
                    while (reportingTo == null) {
                        println("Enter reporting to ID:")
                        val input = readln()
                        val (isValid, id) = validateIntInput(input)
                        if (isValid && id != null) {
                            reportingTo = id
                        } else {
                            println("Invalid input.")
                        }
                    }

                    val addedEmployee = employee.addEmployee(firstName, lastName, role, reportingTo)
                    println("Successfully added!")

                    println("Add another employee? (y/n):")
                    if (readln().trim().lowercase() != "y") {
                        break
                    }
                }
            }

            "2" -> {
                println("Employee List:")
                val allEmployeeDetails = employee.listAllEmployees()
                if (allEmployeeDetails.isEmpty()) {
                    println("No employees found.")
                } else {
                    for (emp in allEmployeeDetails) {
                        println("ID: ${emp.id}, Name: ${emp.firstName} ${emp.lastName}, Role: ${emp.role}")
                    }
                }
            }

            "3" -> {
                println("Check-In")
                while (true) {
                    var employeeId: Int? = null
                    while (employeeId == null) {
                        println("Enter your employee ID:")
                        val (isValid, id) = validateIntInput(readln())
                        if (isValid && id != null && employee.isValidEmployeeId(id)) {
                            employeeId = id
                        } else {
                            println("Invalid or ID not found.")
                        }
                    }

                    var checkInTime: LocalDateTime? = null
                    while (checkInTime == null) {
                        println("Enter check-in date and time (dd-MM-yyyy HH:mm) or press Enter for now:")
                        val (isValid, dateTime) = validateDateTime(readln())
                        if (isValid && dateTime != null) {
                            checkInTime = dateTime
                        } else {
                            println("Invalid or future date. Try again.")
                        }
                    }

                    if (attendance.checkIn(employeeId, checkInTime)) {
                        println("Checked in successfully!")
                    } else {
                        println("Already checked in.")
                    }

                    println("Type 'exit' to stop or press Enter to check in another:")
                    if (readln().trim().lowercase() == "exit") break
                }
            }

            "4" -> {
                println("Ending Attendance!")
                break
            }

            else -> {
                println("Invalid choice. Try again.")
            }
        }
    }
}
