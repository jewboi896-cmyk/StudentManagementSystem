/*
This file manages attendence tracking and recording
 */

package Attendance;

import Course.Course;
import Main.Main;
import Role.RoleUtil;
import Student.Student;
import User.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AttendanceManager {
    private final Scanner scanner;
    private final Main mainApp;
    
    public AttendanceManager(Scanner scanner, Main mainApp) {
        this.scanner = scanner;
        this.mainApp = mainApp;
    }
    
    public void takeAttendance(User currentUser) {
        // Only teachers and admins can take attendance
        if (currentUser.getRole() != RoleUtil.Role.TEACHER && currentUser.getRole() != RoleUtil.Role.ADMIN) {
            System.out.println("Access denied. Only teachers and administrators can take attendance.");
            return;
        }
        
        System.out.println("\n=== Take Attendance ===");
        
        // Get course
        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine().trim();
        Course course = mainApp.courseValidation(courseName);
        
        if (course == null) {
            System.out.println("Error: Course '" + courseName + "' not found.");
            return;
        }
        
        // Check if teacher has permission for this course
        if (currentUser.getRole() == RoleUtil.Role.TEACHER) {
            if (!currentUser.canAccessCourse(course)) {
                System.out.println("Access denied. You can only take attendance for courses you teach.");
                return;
            }
        }
        
        // Get period
        System.out.print("Enter period/section (e.g., 'Period 1', 'Section A'): ");
        String period = scanner.nextLine().trim();
        
        // Use today's date
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        System.out.println("\nTaking attendance for: " + today.format(dateFormatter));
        System.out.println("Course: " + course.getCourseName() + " - " + period);
        
        // Get enrolled students
        List<Student> enrolledStudents = new ArrayList<>();
        for (Student enrolledStudent : Main.studentMap.values()) {
            if (enrolledStudent.isEnrolledInCourse(course.getCourseName())) {
                enrolledStudents.add(enrolledStudent);
            }
        }
        
        if (enrolledStudents.isEmpty()) {
            System.out.println("No students enrolled in this course.");
            return;
        }
        
        System.out.println("\n--- Student List ---");
        System.out.println("Mark attendance (P=Present, A=Absent, T=Tardy, E=Excused)");
        System.out.println("Press Enter to keep as Present\n");
        
        // Take attendance for each student
        for (Student student : enrolledStudents) {
            // Check if attendance already exists for today
            AttendanceRecord existingStudent = Main.getAttendanceRecord(student, course, today, period);
            // if they dont, add them
            if (existingStudent != null) {
                System.out.print(student.getStudentName() + " [Current: " + existingStudent.getStatusString() + "]: ");
            } 
            else {
                System.out.print(student.getStudentName() + " [Default: Present]: ");
            }
            
            String input = scanner.nextLine().trim().toUpperCase();
            
            AttendanceRecord.AttendanceStatus attendenceStatus;
            if (input.isEmpty() || input.equals("P")) {
                attendenceStatus = AttendanceRecord.AttendanceStatus.PRESENT;
            } 
            else if (input.equals("A")) {
                attendenceStatus = AttendanceRecord.AttendanceStatus.ABSENT;
            } 
            else if (input.equals("T")) {
                attendenceStatus = AttendanceRecord.AttendanceStatus.TARDY;
            } 
            else if (input.equals("E")) {
                attendenceStatus = AttendanceRecord.AttendanceStatus.EXCUSED;
            } 
            else {
                System.out.println("  Invalid input, marking as Present");
                attendenceStatus = AttendanceRecord.AttendanceStatus.PRESENT;
            }
            
            // Create or update attendance record
            AttendanceRecord attendenceRecord;
            if (existingStudent != null) {
                existingStudent.setStatus(attendenceStatus);
                attendenceRecord = existingStudent;
            } 
            else {
                attendenceRecord = new AttendanceRecord(student, course, today, period, null);
                attendenceRecord.setStatus(attendenceStatus);
                Main.attendanceMap.put(attendenceRecord.getKey(), attendenceRecord);
            }
            
            // Optional: Add note for absences
            if (attendenceStatus  == AttendanceRecord.AttendanceStatus.ABSENT || attendenceStatus == AttendanceRecord.AttendanceStatus.EXCUSED) {
                System.out.print("  Add note (optional): ");
                String note = scanner.nextLine().trim();
                if (!note.isEmpty()) {
                    attendenceRecord.setNotes(note);
                }
            }
        }
        
        System.out.println("\nAttendance saved successfully!");
    }
    
    public void viewAttendance(User currentUser) {
        System.out.println("\n=== View Attendance ===");
        System.out.println("1. View by Student");
        System.out.println("2. View by Course");
        System.out.println("3. View by Date");
        System.out.print("Choose option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1" -> viewAttendanceByStudent(currentUser);
            case "2" -> viewAttendanceByCourse(currentUser);
            case "3" -> viewAttendanceByDate(currentUser);
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void viewAttendanceByStudent(User currentUser) {
        System.out.print("\nEnter student name: ");
        String studentName = scanner.nextLine().trim();
        
        Student student = mainApp.studentValidation(studentName);
        if (student == null) {
            System.out.println("Error: Student '" + studentName + "' not found.");
            return;
        }
        
        // Check permissions
        if (!currentUser.canAccessStudent(studentName)) {
            System.out.println("Access denied.");
            return;
        }
        
        System.out.println("\n--- Attendance for " + studentName + " ---");
        
        boolean foundRecords = false;
        int presentCount = 0, absentCount = 0, tardyCount = 0, excusedCount = 0;
        
        for (AttendanceRecord attendenceRecord : Main.attendanceMap.values()) {
            // if student found in attendence record, print their info
            if (attendenceRecord.getStudent().getStudentName().equals(studentName)) {
                foundRecords = true;
                System.out.println("\nDate: " + attendenceRecord.getDate());
                System.out.println("Course: " + attendenceRecord.getCourse().getCourseName() + " - " + attendenceRecord.getPeriod());
                System.out.println("Status: " + attendenceRecord.getStatusString());
                if (!attendenceRecord.getNotes().isEmpty()) {
                    System.out.println("Notes: " + attendenceRecord.getNotes());
                }
                
                // Count statuses
                switch (attendenceRecord.getStatus()) {
                    case PRESENT -> presentCount++;
                    case ABSENT -> absentCount++;
                    case TARDY -> tardyCount++;
                    case EXCUSED -> excusedCount++;
                }
            }
        }
        
        if (!foundRecords) {
            System.out.println("No attendance records found for this student.");
        } 
        else {
            int total = presentCount + absentCount + tardyCount + excusedCount;
            double attendanceRate = total > 0 ? (presentCount * 100.0 / total) : 0;

            System.out.println("\n--- Summary ---");
            System.out.println("Present: " + presentCount);
            System.out.println("Absent: " + absentCount);
            System.out.println("Tardy: " + tardyCount);
            System.out.println("Excused: " + excusedCount);
            System.out.println("Attendance Rate: " + String.format("%.1f%%", attendanceRate));
        }
    }
    
    private void viewAttendanceByCourse(User currentUser) {
        System.out.print("\nEnter course name: ");
        String courseName = scanner.nextLine().trim();
        // validate course
        Course course = mainApp.courseValidation(courseName);
        if (course == null) {
            System.out.println("Error: Course '" + courseName + "' not found.");
            return;
        }
        
        // Check permissions
        if (currentUser.getRole() == RoleUtil.Role.TEACHER) {
            if (!currentUser.canAccessCourse(course)) {
                System.out.println("Access denied. You can only view attendance for courses you teach.");
                return;
            }
        }
        
        System.out.print("Enter date (YYYY-MM-DD) or leave blank for today: ");
        String dateInput = scanner.nextLine().trim();
        
        LocalDate targetDate = dateInput.isEmpty() ? LocalDate.now() : LocalDate.parse(dateInput);
        
        System.out.print("Enter period/section (or leave blank for all): ");
        String period = scanner.nextLine().trim();
        
        System.out.println("\n--- Attendance for " + course.getCourseName() + " ---");
        System.out.println("Date: " + targetDate);
        if (!period.isEmpty()) {
            System.out.println("Period: " + period);
        }
        
        boolean foundRecords = false;
        
        for (AttendanceRecord attendenceRecord : Main.attendanceMap.values()) {
            // if the attendence record of the selected course equals the courses name and the date and the period is empty or the selected period is the correct one, print the status of the record
            if (attendenceRecord.getCourse().getCourseName().equals(courseName) && attendenceRecord.getDate().equals(targetDate) && (period.isEmpty() || attendenceRecord.getPeriod().equals(period))) {
                foundRecords = true;
                System.out.println("\n" + attendenceRecord.getStudent().getStudentName() + " - " + attendenceRecord.getStatusString());
                if (!attendenceRecord.getNotes().isEmpty()) {
                    System.out.println("  Notes: " + attendenceRecord.getNotes());
                }
            }
        }
        
        if (!foundRecords) {
            System.out.println("No attendance records found for this course on this date.");
        }
    }
    
    private void viewAttendanceByDate(User currentUser) {
        System.out.print("\nEnter date (YYYY-MM-DD) or leave blank for today: ");
        String dateInput = scanner.nextLine().trim();
        
        LocalDate targetDate = dateInput.isEmpty() ? LocalDate.now() : LocalDate.parse(dateInput);
        
        System.out.println("\n--- Attendance for " + targetDate + " ---");
        
        // Group by course
        Map<String, List<AttendanceRecord>> attendenceByCourse = new HashMap<>();
        
        for (AttendanceRecord attendenceRecord : Main.attendanceMap.values()) {
            // if date is correct, add attendence record to the map
            if (attendenceRecord.getDate().equals(targetDate)) {
                String key = attendenceRecord.getCourse().getCourseName() + " - " + attendenceRecord.getPeriod();
                attendenceByCourse.computeIfAbsent(key, k -> new ArrayList<>()).add(attendenceRecord);
            }
        }
        
        if (attendenceByCourse.isEmpty()) {
            System.out.println("No attendance records found for this date.");
            return;
        }
        
        for (Map.Entry<String, List<AttendanceRecord>> entry : attendenceByCourse.entrySet()) {
            System.out.println("\n" + entry.getKey() + ":");
            for (AttendanceRecord attendenceRecord : entry.getValue()) {
                // Check permissions
                if (currentUser.getRole() == RoleUtil.Role.TEACHER) {
                    if (!currentUser.canAccessCourse(attendenceRecord.getCourse())) {
                        continue;
                    }
                }
                
                System.out.println("  " + attendenceRecord.getStudent().getStudentName() + " - " + attendenceRecord.getStatusString());
            }
        }
    }
    
    public void generateAttendanceReport(User currentUser) {
        // only teachers and admins can use this method
        if (currentUser.getRole() != RoleUtil.Role.TEACHER && currentUser.getRole() != RoleUtil.Role.ADMIN) {
            System.out.println("Access denied.");
            return;
        }
        
        System.out.println("\n=== Generate Attendance Report ===");
        System.out.print("Enter student name: ");
        String studentName = scanner.nextLine().trim();
        // validate student name
        Student student = mainApp.studentValidation(studentName);
        if (student == null) {
            System.out.println("Error: Student not found.");
            return;
        }
        
        String report = AttendanceReportGenerator.generateStudentAttendanceReport(student);
        System.out.println("\n" + report);
    }
}