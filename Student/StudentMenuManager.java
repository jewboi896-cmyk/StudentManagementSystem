package Student;

import Assignments.Assignment;
import Assignments.AssignmentSubmission;
import Course.Course;
import GPACalculator.GpaCalculator;
import Grades.TermGrade;
import Main.Entry;
import Role.RoleUtil;
import Term.AcademicTerm;
import User.User;
import java.util.Map;
import java.util.Scanner;

public class StudentMenuManager {
    private final Scanner scanner;
    public static Entry entryApp;

    public StudentMenuManager(Scanner scanner, Entry entryApp) {
        this.scanner = scanner;
        this.entryApp = entryApp;
    }

    public void viewStudentInfo(User currentUser) {
        String studentName;
        // if the user is a student, then get there info, otherwise prompt for their name
        if (currentUser.getRole() == RoleUtil.Role.STUDENT) {
            // Auto-select for students
            studentName = currentUser.getAssocID();
            System.out.println("Viewing your information...");
        } 
        else {
            System.out.print("Enter student name: ");
            studentName = scanner.nextLine().trim();
        }

        // Validate student
        Student student = entryApp.studentValidation(studentName);

        // Check if student exists
        if (student == null) {
            System.out.println("Error: Student '" + studentName + "' not found.");
            return;
        }

        // check if user can access student
        if (!currentUser.canAccessStudent(studentName)) {
            System.out.println("Access denied. You cannot access another students information.");
            return;
        }

        // Display student information
        System.out.println("\n=== Student Details ===");
        entryApp.displayStudentAttributes(student);
    
    }

    public void addNewStudent(User currentUser) {
        // check if user has correct permissions
        if (currentUser.getRole() != RoleUtil.Role.ADMIN) {
            System.out.println("Access denied. You do not have the required permissions to access this data.");
            return;
        }

        System.out.println("\n=== Add New Student ===");
    
        // Get student information
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();
    
        // Check if student already exists
        if (Entry.studentMap.containsKey(name)) {
            System.out.println("Error: A student with the name '" + name + "' already exists.");
            return;
        }
    
        System.out.print("Enter student age: ");
        int age;
        try {
            age = Integer.parseInt(scanner.nextLine().trim());
            // valid age check
            if (age < 0 || age > 150) { 
                System.out.println("Error: Age must be between 0 and 150.");
                return;
            }
        } 
        catch (NumberFormatException e) {
            System.out.println("Error: Invalid age. Please enter a number.");
            return;
        }
    
        System.out.print("Enter student year (Freshman/Sophomore/Junior/Senior): ");
        String year = scanner.nextLine().trim();
    
        // Create and add the student
        Student newStudent = new Student(name, age, year);
        Entry.addStudent(newStudent);
    
        System.out.println("Success! Student '" + name + "' has been added.");
    }

    public void deleteStudent(User currentUser) {
        // check if user has correct permissions
        if (currentUser.getRole() != RoleUtil.Role.ADMIN) {
            System.out.println("Access denied. You do not have the required permissions to access this data.");
            return;
        }

        System.out.println("\n=== Delete Student ===");
        System.out.print("Enter student name to delete: ");
        String studentName = scanner.nextLine().trim();

        // Validate student
        Student student = entryApp.studentValidation(studentName);

        // Check if student exists
        if (student == null) {
            System.out.println("Error: Student '" + studentName + "' not found.");
            return;
        }
        // Confirm deletion
        System.out.print("Are you sure you want to delete student '" + studentName + "'? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (!confirmation.equals("yes") && !confirmation.equals("y")) {
            System.out.println("Deletion cancelled.");
            return;
        }
    
        // Remove student from the system
        Entry.studentMap.remove(studentName);
        System.out.println("Success! Student '" + studentName + "' has been deleted.");
    }

    public void enrollStudentInCourse(User currentUser) {
        // check permissions
        if ((currentUser.getRole() != RoleUtil.Role.ADMIN) && (currentUser.getRole() != RoleUtil.Role.TEACHER)) {
            System.out.println("Access denied. You do not have the required permissions to access this data.");
            return;
        }

        System.out.println("\n=== Enroll Student in Course ===");
    
        System.out.print("Enter student name: ");
        String studentName = scanner.nextLine().trim();
        Student student = entryApp.studentValidation(studentName);
        if (student == null) {
            System.out.println("Error: Student not found.");
            return;
        }
    
        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine().trim();
        Course course = entryApp.courseValidation(courseName);
        if (course == null) {
            System.out.println("Error: Course not found.");
            return;
        }

        // Check if already enrolled
        if (student.isEnrolledInCourse(course.getCourseName())) {
        System.out.println("Error: " + studentName + " is already enrolled in " + courseName + ".");
        return;
    }
    
        // Enroll the student
        student.enrollInCourse(course);
        System.out.println("Success! " + studentName + " has been enrolled in " + courseName + ".");
    }

    public void generateStudentReport(User currentUser) {
        System.out.println("\n=== Generate Student Report ===");

        // Determine which student to generate report for
        String studentName = determineStudentName(currentUser);

        // Validate student
        Student student = entryApp.studentValidation(studentName);
        if (student == null) {
            System.out.println("Error: Student '" + studentName + "' not found.");
            return;
        }

        // Check permissions
        if (!currentUser.canAccessStudent(studentName)) {
            System.out.println("Access denied. You cannot access this student's information.");
            return;
        }

        // Generate and display report
        String report = Report.ReportGenerator.generateStudentReport(student);
        System.out.println("\n" + report);
    }
    
    // method to determine student name based on role
    public String determineStudentName(User currentUser) {
        if (currentUser.getRole() == RoleUtil.Role.STUDENT || currentUser.getRole() == RoleUtil.Role.PARENT) {
            // Students and parents: use their associated ID
            return currentUser.getAssocID();
        } 
        else {
            // Teachers and admins: prompt for student name
            System.out.print("Enter student name: ");
            return scanner.nextLine().trim();
        }
    }

    public void dropStudentFromCourse(User currentUser) {
        // Check permissions (ADMIN or TEACHER only)
        if ((currentUser.getRole() != RoleUtil.Role.ADMIN) && (currentUser.getRole() != RoleUtil.Role.TEACHER)) {
            System.out.println("Access denied. You do not have the required permissions to access this data.");
            return;
        }

        System.out.println("\n=== Drop Student From Course ===");

        // Prompt for student name
        System.out.print("Enter student name: ");
        String studentName = scanner.nextLine().trim();

        // Validate student
        Student student = entryApp.studentValidation(studentName);
        if (student == null) {
            System.out.println("Error: Student '" + studentName + "' not found.");
            return;
        }

        // Prompt for course name
        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine().trim();

        // Validate course
        Course course = entryApp.courseValidation(courseName);
        if (course == null) {
            System.out.println("Error: Course '" + courseName + "' not found.");
            return;
        }

        // For TEACHERS: Check if they teach this course
        if (currentUser.getRole() == RoleUtil.Role.TEACHER) {
            if (!currentUser.canAccessCourse(course)) {
                System.out.println("Access denied. You can only drop students from courses you teach.");
                return;
            }
        }

        // Check if student is actually enrolled
        if (!student.isEnrolledInCourse(courseName)) {
            System.out.println("Error: " + studentName + " is not enrolled in " + courseName + ".");
            return;
        }

        // Confirm action
        System.out.print("Are you sure you want to drop " + studentName + " from " + courseName + "? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (!confirmation.equals("yes") && !confirmation.equals("y")) {
            System.out.println("Drop cancelled.");
            return;
        }

        // Remove all course data (enrollment, grades, assignment scores)
        student.removeAllCourseData(courseName);
    
        System.out.println("Success! " + studentName + " has been dropped from " + courseName + ".");
    }

    public void editStudentDetails(User currentUser) {
        // Only admins can edit student details
        if (currentUser.getRole() != RoleUtil.Role.ADMIN) {
            System.out.println("Access denied. Only administrators can edit student details.");
            return;
        }

        System.out.println("\n=== Edit Student Details ===");
        System.out.print("Enter student name: ");
        String studentName = scanner.nextLine().trim();

        Student student = entryApp.studentValidation(studentName);
        if (student == null) {
            System.out.println("Error: Student '" + studentName + "' not found.");
            return;
        }

        boolean editing = true;
        while (editing) {
            System.out.println("\n--- Edit Options for " + studentName + " ---");
            System.out.println("1. Student ID");
            System.out.println("2. Email");
            System.out.println("3. Phone");
            System.out.println("4. Address");
            System.out.println("5. Parent Information");
            System.out.println("6. Emergency Contact");
            System.out.println("7. Medical Notes");
            System.out.println("8. Accommodations");
            System.out.println("9. View All Details");
            System.out.println("10. Done Editing");
            System.out.print("Choose field to edit: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.print("Enter Student ID: ");
                    student.setStudentId(scanner.nextLine().trim());
                    System.out.println("Student ID updated.");
                }
                case "2" -> {
                    System.out.print("Enter Email: ");
                    student.setEmail(scanner.nextLine().trim());
                    System.out.println("Email updated.");
                }
                case "3" -> {
                    System.out.print("Enter Phone: ");
                    student.setPhone(scanner.nextLine().trim());
                    System.out.println("Phone updated.");
                }   
                case "4" -> {
                    System.out.print("Enter Address: ");
                    student.setAddress(scanner.nextLine().trim());
                    System.out.println("Address updated.");
                }
                case "5" -> {
                    System.out.print("Enter Parent Name: ");
                    student.setParentName(scanner.nextLine().trim());
                    System.out.print("Enter Parent Email: ");
                    student.setParentEmail(scanner.nextLine().trim());
                    System.out.print("Enter Parent Phone: ");
                    student.setParentPhone(scanner.nextLine().trim());
                    System.out.println("Parent information updated.");
                }
                case "6" -> {
                    System.out.print("Enter Emergency Contact Name: ");
                    student.setEmergencyContactName(scanner.nextLine().trim());
                    System.out.print("Enter Relation (Parent/Guardian/Other): ");
                    student.setEmergencyContactRelation(scanner.nextLine().trim());
                    System.out.print("Enter Emergency Contact Phone: ");
                    student.setEmergencyContactPhone(scanner.nextLine().trim());
                    System.out.println("Emergency contact updated.");
                }
                case "7" -> {
                    System.out.print("Enter Medical Notes (allergies, conditions, medications): ");
                    student.setMedicalNotes(scanner.nextLine().trim());
                    System.out.println("Medical notes updated.");
                }
                case "8" -> {
                    System.out.print("Enter Accommodations (IEP, 504 plan details): ");
                    student.setAccommodations(scanner.nextLine().trim());
                    System.out.println("Accommodations updated.");
                }
                case "9" -> System.out.println("\n" + student.getComprehensiveInfo());
                case "10" -> {
                    editing = false;
                    System.out.println("Finished editing student details.");
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // Students can submit their own assignments and teachers/admins can submit on behalf of students
    public void submitAssignment(User currentUser) {
    
        System.out.println("\n=== Submit Assignment ===");
    
        String studentName;
        if (currentUser.getRole() == null) {
            System.out.println("Access denied.");
            return;
        } 
        else switch (currentUser.getRole()) {
            case STUDENT -> {
                studentName = currentUser.getAssocID();
                System.out.println("Submitting assignment for: " + studentName);
            }
            case TEACHER, ADMIN -> {
                System.out.print("Enter student name: ");
                studentName = scanner.nextLine().trim();
            }
            default -> {
                System.out.println("Access denied.");
                return;
            }
        }
        // validate student
        Student student = entryApp.studentValidation(studentName);

        // check if student exists
        if (student == null) {
            System.out.println("Error: Student '" + studentName + "' not found.");
            return;
        }

        System.out.print("Enter assignment name: ");
        String assignmentName = scanner.nextLine().trim();

        // validate assignment
        Assignment assignment = entryApp.assignmentValidation(assignmentName);

        if (assignment == null) {
            System.out.println("Error: Assignment '" + assignmentName + "' not found.");
            return;
        }

        // Check if student is enrolled in the course
        if (!student.isEnrolledInCourse(assignment.getCourse().getCourseName())) {
            System.out.println("Error: Student is not enrolled in " + assignment.getCourse().getCourseName());
            return;
        }

        // Submit the assignment
        student.submitAssignment(assignment);

        // get submission status
        AssignmentSubmission submission = student.getSubmission(assignment);
    
        System.out.println("\n--- Submission Successful ---");
        System.out.println("Assignment: " + assignment.getAssignmentName());
        System.out.println("Status: " + submission.getStatusString());
    
        if (submission.isLate()) {
            System.out.println("WARNING: This submission is LATE!");
            System.out.println("Due date was: " + assignment.getDueDate());
        }
    }

    public void viewSubmissionStatus(User currentUser) {
        System.out.println("\n=== View Submission Status ===");
    
        String studentName;
        if (currentUser.getRole() == RoleUtil.Role.STUDENT) {
            studentName = currentUser.getAssocID();
        } 
        else {
            System.out.print("Enter student name: ");
            studentName = scanner.nextLine().trim();
        }

        Student student = entryApp.studentValidation(studentName);
        if (student == null) {
            System.out.println("Error: Student '" + studentName + "' not found.");
            return;
        }

        if (!currentUser.canAccessStudent(studentName)) {
            System.out.println("Access denied.");
            return;
        }

        System.out.print("Enter assignment name (or leave blank for all): ");
        String assignmentName = scanner.nextLine().trim();

        if (assignmentName.isEmpty()) {
            // Show all submissions for this student
            System.out.println("\n--- All Submissions for " + studentName + " ---");
            boolean hasSubmissions = false;
        
            for (String courseName : student.getEnrolledCourses()) {
                Course course = Entry.courseMap.get(courseName);
                if (course == null) continue;
            
                System.out.println("\nCourse: " + courseName);
                System.out.println("----------------------------------------");
            
                for (Assignment assignment : Entry.assignmentMap.values()) {
                    if (assignment.getCourse().equals(course)) {
                        hasSubmissions = true;
                        AssignmentSubmission submission = student.getSubmission(assignment);
                    
                        System.out.println("\n  Assignment: " + assignment.getAssignmentName());
                        System.out.println("  Due: " + assignment.getDueDate());
                        System.out.println("  " + submission.getSubmissionInfo());
                    }
                }
            }
        
            if (!hasSubmissions) {
                System.out.println("No assignments found.");
            }
        } 
        else {
            // Show specific assignment
            Assignment assignment = entryApp.assignmentValidation(assignmentName);
            if (assignment == null) {
                System.out.println("Error: Assignment '" + assignmentName + "' not found.");
                return;
            }

            AssignmentSubmission submission = student.getSubmission(assignment);
        
            System.out.println("\n--- Submission Details ---");
            System.out.println("Assignment: " + assignment.getAssignmentName());
            System.out.println("Course: " + assignment.getCourse().getCourseName());
            System.out.println("Due Date: " + assignment.getDueDate());
            System.out.println("Max Score: " + assignment.getMaxScore());
            System.out.println("\n" + submission.getSubmissionInfo());
        }
    }

    public void markAssignmentMissing(User currentUser) {
        // Only teachers and admins can mark assignments as missing
        if (currentUser.getRole() != RoleUtil.Role.TEACHER && currentUser.getRole() != RoleUtil.Role.ADMIN) {
            System.out.println("Access denied. Only teachers and administrators can mark assignments as missing.");
            return;
        }

        System.out.println("\n=== Mark Assignment as Missing ===");
    
        System.out.print("Enter student name: ");
        String studentName = scanner.nextLine().trim();

        Student student = entryApp.studentValidation(studentName);
        if (student == null) {
            System.out.println("Error: Student '" + studentName + "' not found.");
            return;
        }

        System.out.print("Enter assignment name: ");
        String assignmentName = scanner.nextLine().trim();

        Assignment assignment = entryApp.assignmentValidation(assignmentName);
        if (assignment == null) {
            System.out.println("Error: Assignment '" + assignmentName + "' not found.");
            return;
        }

        // Check teacher permissions
        if (currentUser.getRole() == RoleUtil.Role.TEACHER) {
            if (!currentUser.canAccessCourse(assignment.getCourse())) {
                System.out.println("Access denied. You can only mark assignments for courses you teach.");
                return;
            }
        }

        AssignmentSubmission submission = student.getSubmission(assignment);
        submission.markAsMissing();

        System.out.println("Success! Assignment marked as MISSING for " + studentName);
    }

    public void viewDetailedStudentInfo(User currentUser) {
        System.out.println("\n=== View Detailed Student Information ===");
    
        String studentName;
        if (currentUser.getRole() == RoleUtil.Role.STUDENT) {
            studentName = currentUser.getAssocID();
        } 
        else {
            System.out.print("Enter student name: ");
            studentName = scanner.nextLine().trim();
        }

        Student student = entryApp.studentValidation(studentName);
        if (student == null) {
            System.out.println("Error: Student '" + studentName + "' not found.");
            return;
        }

        if (!currentUser.canAccessStudent(studentName)) {
            System.out.println("Access denied.");
            return;
        }

        // Display comprehensive information
        System.out.println("\n" + student.getComprehensiveInfo());
    
        // Also show academic info
        System.out.println("\n=== Academic Information ===");
        System.out.println("Enrolled Courses: " + student.getEnrolledCourses().size());
    
        Map<String,Map<AcademicTerm,TermGrade>> grades = student.getCourseGrades();
        if (!grades.isEmpty()) {
            System.out.println("\nCurrent Grades:");
            for (Map.Entry<String, Map<AcademicTerm, TermGrade>> entry : grades.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
        
            double gpa = GpaCalculator.calculateGPA(grades);
            System.out.println("\nGPA: " + String.format("%.2f", gpa));
        }
    }
}