# EMPLOYEE ATTENDANCE

> OVERVIEW

This project is a Command Line Interface (CLI) based Employee Check-In & Check-out System implemented in Kotlin.  
It manages employee records, and tracks daily check-ins ad check-outs.

> DATA CLASSES

**1. DataEmployee**

  - `id: Int` - Unique employee identifier, auto-generated.  
  - `firstName: String` 
  - `lastName: String` 
  - `role: String` 
  - `reportingTo: Int` 

**2. DataAttendance**

  - `employeeId: Int`
  - `checkInDateTime: LocalDateTime`
  - `checkOutDateTime: LocalDateTime`
  - `workingHours: Double`

> CLASSES & FUNCTIONS

**1. Employee**

- `addEmployee(firstName: String, lastName: String, role: String, reportingToId: Int)`  
  Adds a new employee with auto-incremented ID and stores in a mutable map keyed by employee ID.

- `listAllEmployees(): List<DataEmployee>`  
  Returns a list of all employees currently stored.

- `isValidEmployeeId(id: Int): Boolean`  
  Checks if the given employee ID exists in the employee map.

**2. Attendance**

- `checkIn(employeeId: Int, dateTime: LocalDateTime): Boolean`  
- `hasCheckedIn(employeeId: Int, dateTime: LocalDateTime): Boolean` (private)  
- `checkOut(employeeId: Int, dateTime: LocalDateTime): Boolean`
- `hasCheckedOut(employeeId: Int, dateTime: LocalDateTime): Boolean` (private)
- `getAllAttendance(): List<DataAttendance>`

> INPUT VALIDATIONS

- `validateNonEmptyInput(input: String): Boolean`  
  Ensures input string is not blank or empty.

- `validateIntInput(input: String): Pair<Boolean, Int?>`  
  Parses input string to integer and return valid or not (Boolean) and parsed int.

- `validateDateTime(input: String): Pair<Boolean, LocalDateTime?>`  
  Parses date-time string (format: `"dd-MM-yyyy HH:mm"`) or current date-time if blank.
  It also doesn't allow future dates.

- `fun formatWorkingHours(hours: Double?): String `
- `fun printAttendance(attendanceLog: List<DataAttendance>)`
