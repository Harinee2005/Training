import java.time.Duration
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
    var checkOutDateTime: LocalDateTime? = null,
    var workingHours: Double? = null
)

// Classes to manage employee and attendance
class Employee {
    private val employeeDetails = mutableMapOf<Int, DataEmployee>()
    private var employeeIdCounter = 101

    fun addEmployee(firstName: String, lastName: String, role: String, reportingToId: Int) {
        val employee = DataEmployee(employeeIdCounter, firstName, lastName, role, reportingToId)
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
            return true
        } else {
            return false  
        }
    }
    
    private fun hasCheckedIn(employeeId: Int, dateTime: LocalDateTime): Boolean {
        val checkInDate = dateTime.toLocalDate()
        return attendanceLog.any {
            it.employeeId == employeeId && it.checkInDateTime.toLocalDate() == checkInDate
        }
    }

    fun checkOut(employeeId: Int, dateTime: LocalDateTime): Boolean {
        val checkOutDate = dateTime.toLocalDate()

        if (hasCheckedOut(employeeId, dateTime)) {
            return false
        }

        val attendanceRecord = attendanceLog.find {
            it.employeeId == employeeId && it.checkInDateTime.toLocalDate() == checkOutDate
        } ?: return false

        if (dateTime.isBefore(attendanceRecord.checkInDateTime)) {
            return false
        }
        if (dateTime.isAfter(LocalDateTime.now())) {
            return false
        }

        attendanceRecord.checkOutDateTime = dateTime
        val duration = Duration.between(attendanceRecord.checkInDateTime, dateTime)
        attendanceRecord.workingHours = duration.toMinutes() / 60.0
        return true
    }

    private fun hasCheckedOut(employeeId: Int, dateTime: LocalDateTime): Boolean {
        val checkOutDate = dateTime.toLocalDate()
        return attendanceLog.any {
            it.employeeId == employeeId &&
                    it.checkInDateTime.toLocalDate() == checkOutDate &&
                    it.checkOutDateTime != null
        }
    }

    fun getAllAttendance(): List<DataAttendance> {
        return attendanceLog.toList()
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
            Pair(true, parsed)
        } catch (e: Exception) {
            Pair(false, null)
        }
    }
}

// Working hours format
fun formatWorkingHours(hours: Double?): String {
    return if (hours == null) {
        "N/A"
    } else {
        "%.2f hrs".format(hours)
    }
}

// Print attendance report
fun printAttendance(attendanceLog: List<DataAttendance>) {
    if (attendanceLog.isEmpty()) {
        println("No attendance records found.")
        return
    }
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    for (record in attendanceLog) {
        println("Employee ID: ${record.employeeId}")
        println("Check-In: ${record.checkInDateTime.format(formatter)}")
        if (record.checkOutDateTime != null) {
            println("Check-Out: ${record.checkOutDateTime?.format(formatter)}")
            println("Working Hours: ${formatWorkingHours(record.workingHours)}")
        } else {
            println("Check-Out: Not yet checked out")
            println("Working Hours: N/A")
        }
        println("---------------------------------------")
    }
}

// Main function
fun main() {
    val employee = Employee()
    val attendance = Attendance()

    employee.addEmployee("John", "Doe", "Manager", 0)

    while (true) {
        println("Menu: \n1. Add Employee \n2. List Employees \n3. Check-In \n4. Check-Out \n5. View Attendance Records " +
                "\n6. Exit \nEnter your choice: ")
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

                    employee.addEmployee(firstName, lastName, role, reportingTo)
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
                            if (dateTime.isAfter(LocalDateTime.now())) {
                                println("Check-in cannot be in the future. Try again.")
                            } else {
                                checkInTime = dateTime
                            }
                        } else {
                            println("Invalid date format. Try again.")
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
                println("Check-Out")
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

                    var checkOutTime: LocalDateTime? = null
                    while (checkOutTime == null) {
                        println("Enter check-out date and time (dd-MM-yyyy HH:mm) or press Enter for now:")
                        val (isValid, dateTime) = validateDateTime(readln())
                        if (isValid && dateTime != null) {
                            if (dateTime.isAfter(LocalDateTime.now())) {
                                println("Check-out cannot be in the future. Try again.")
                            } else {
                                checkOutTime = dateTime
                            }
                        } else {
                            println("Invalid date format. Try again.")
                        }
                    }

                    val result = attendance.checkOut(employeeId, checkOutTime)
                    if (result) {
                        println("Checked out successfully!")
                    } else {
                        println("Cannot check out!")
                    }

                    println("Type 'exit' to stop or press Enter to check out another:")
                    if (readln().trim().lowercase() == "exit") break
                }
            }

            "5" -> {
                println("Attendance Records:")
                printAttendance(attendance.getAllAttendance())
            }

            "6" -> {
                println("Ending Attendance!")
                break
            }

            else -> {
                println("Invalid choice. Try again.")
            }
        }
    }
}
