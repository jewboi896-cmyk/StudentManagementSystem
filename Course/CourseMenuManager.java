/*
This file deals with displaying the course menu and such
 */

package Course;

import Assignments.Assignment;
import CategoryWeight.CategoryWeightManager;
import Course.CourseSchedular.DaysOfTheWeek;
import Main.Main;
import Report.ReportGenerator;
import Role.RoleUtil;
import Student.Student;
import User.User;
import java.util.Scanner;

public class CourseMenuManager {
    private final Scanner scanner;
    private final Main mainApp;
    private final CategoryWeightManager categoryWeightManager;

    public CourseMenuManager(Scanner scanner, Main mainApp) {
        this.scanner = scanner;
        this.mainApp = mainApp;
        this.categoryWeightManager = new CategoryWeightManager(scanner, mainApp);
    }

    public void viewCourseInfo(User currentUser) {
        System.out.println("\n=== View Course Information ===");
        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine().trim();
        // Validate course
        Course selectedCourse = mainApp.courseValidation(courseName);
        // Check if course exists
        if (selectedCourse == null) {
            System.out.println("Error: Course '" + courseName + "' not found.");
            return;
        }
        // permission check
        if (!currentUser.canAccessCourse(selectedCourse)) {
            System.out.println("Access denied. You do not have permission to view this course's information.");
            return;
        }
        
        // Display course information
        System.out.println("\n=== Course Details ===");
        mainApp.displayCourseAttributes(selectedCourse);
    }

    public void addNewCourse(User currentUser) {
        // check if user has correct permissions
        if (currentUser.getRole() != RoleUtil.Role.ADMIN) {
            System.out.println("Access denied. You do not have the required permissions to access this data.");
            return;
        }

        System.out.println("\n=== Add New Course ===");
    
        // Get course information
        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine().trim();
    
        // Check if course already exists
        if (Main.courseMap.containsKey(courseName)) {
            System.out.println("Error: A course with the name '" + courseName + "' already exists.");
            return;
        }
        // Get remaining course details
        System.out.print("Enter instructor name: ");
        String courseInstructor = scanner.nextLine().trim();

        // Get course ID
        System.out.print("Enter course ID: ");
        String courseID = scanner.nextLine().trim();

        // Get number of credits
        System.out.print("Enter number of credits: ");
        int courseCredits;

        // Validate credit input
        try {
            courseCredits = Integer.parseInt(scanner.nextLine().trim());
            if (courseCredits < 0 || courseCredits > 5) {
                System.out.println("Error: Credits must be between 0 and 5");
                return;
            }
        }
        // if credits isnt a number, trigger error 
        catch (NumberFormatException e) {
            System.out.println("Error: Invalid credits. Please enter a number.");
            return;
        }
        // Get class size
        System.out.print("Enter class size: ");
        int courseSize;
        // Validate size input
        try {
            courseSize = Integer.parseInt(scanner.nextLine().trim());
            // courses cannot have less than 0 students or more than 40
            if (courseSize < 0 || courseSize > 40) {
                System.out.println("Error: Class size must be between 0 and 40");
                return;
            }
        } 
        catch (NumberFormatException e) {
            System.out.println("Error: Invalid size. Please enter a number.");
            return;
        }
    
        // Create the course
        Course newCourse = new Course(courseName, courseInstructor, courseID, courseCredits, courseSize, null, DaysOfTheWeek.MONDAY);
    
        // Ask if they want to add category weights
        System.out.print("Do you want to add category weights? (yes/no): ");
        String addWeights = scanner.nextLine().trim().toLowerCase();

        // If yes, proceed to add weights
        if (addWeights.equals("yes") || addWeights.equals("y")) {
            categoryWeightManager.addCategoryWeights(newCourse);
        }
        // Add the course to the system
        Main.addCourse(newCourse);
        System.out.println("Success! Course '" + courseName + "' has been added.");
    }

    public void viewClassRoster(User currentUser) {
        // check user permissions
        if ((currentUser.getRole() != RoleUtil.Role.ADMIN) && (currentUser.getRole() != RoleUtil.Role.TEACHER)) {
            System.out.println("Access denied. You do not have the correct permissions to access this data.");
            return;
        }

        System.out.println("\n=== View Class Roster ===");
    
        // Get course
        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine().trim();

        // Validate course
        Course selectedCourse = mainApp.courseValidation(courseName);

        // Check if course exists
        if (selectedCourse == null) {   
            System.out.println("Error: Course '" + courseName + "' not found.");
            return;
        }
    
        // Find all students enrolled in this course
        System.out.println("\n--- Roster for " + selectedCourse.getCourseName() + " ---");
        System.out.println("Instructor: " + selectedCourse.getCourseInstructor());
        System.out.println("Course ID: " + selectedCourse.getCourseID());
        System.out.println("\nEnrolled Students:");
        System.out.println("----------------------------------------");
    
        boolean foundStudents = false;
    
        for (Student enrolledStudent : Main.studentMap.values()) {
            // Check if student is enrolled in selected course
            if (enrolledStudent.isEnrolledInCourse(selectedCourse.getCourseName())) {
                foundStudents = true;
                String courseGrade = enrolledStudent.getCourseGrades().get(selectedCourse.getCourseName()).toString();
                // Show grade if available, otherwise show "No grade yet"
                String displayGrade = (courseGrade != null) ? courseGrade : "No grade yet";
                System.out.println("- " + enrolledStudent.getStudentName() + " (" + enrolledStudent.getStudentYear() + ") - Grade: " + displayGrade);
            }
        }
        // If no students found, inform user
        if (!foundStudents) {
            System.out.println("No students are currently enrolled in this course.");
        }
    
        System.out.println("----------------------------------------");
    }

    public void deleteCourse(User currentUser) {
        // check if user has correct permissions
        if (currentUser.getRole() != RoleUtil.Role.ADMIN) {
            System.out.println("Access denied. You do not have the required permissions to access this data.");
            return;
        }

        System.out.println("\n=== Delete Course ===");
        System.out.print("Enter course name to delete: ");
        String courseName = scanner.nextLine().trim();
        // Validate course
        Course selectedCourse = mainApp.courseValidation(courseName);
        // Check if course exists
        if (selectedCourse == null) {
            System.out.println("Error: Course '" + courseName + "' not found.");
            return;
        }

        boolean hasAssignments = false;
        // Check if any assignments are linked to this course
        for (Assignment assignment : Main.assignmentMap.values()) {
            if (assignment.getCourse().getCourseName().equals(courseName)) {
                hasAssignments = true;
                break;
            }
        }
        // If assignments exist, warn user
        if (hasAssignments) {
            System.out.println("Warning: This course has assignments associated with it.");
            System.out.print("Deleting the course will also delete all its assignments. Continue? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (!confirm.equals("yes") && !confirm.equals("y")) {
            System.out.println("Deletion cancelled.");
            return;
        }
        
        // Delete all assignments for this course
        Main.assignmentMap.values().removeIf(a -> a.getCourse().equals(selectedCourse));
    }

        // Confirm deletion
        System.out.print("Are you sure you want to delete course '" + courseName + "'? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (!confirmation.equals("yes") && !confirmation.equals("y")) {
            System.out.println("Deletion cancelled.");
            return;
        }
    
        // Remove course from the system
        Main.courseMap.remove(courseName);
        System.out.println("Success! Course '" + courseName + "' has been deleted.");
    }

    public void generateCourseReport(User currentUser) {
        // Only teachers and admins can generate course reports
        if (currentUser.getRole() != RoleUtil.Role.ADMIN && currentUser.getRole() != RoleUtil.Role.TEACHER) {
            System.out.println("Access denied. You do not have the required permissions to access this data.");
            return;
        }   
    
        System.out.println("\n=== Generate Course Report ===");
        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine().trim();
    
        // Validate course
        Course selectedCourse = mainApp.courseValidation(courseName);
        if (selectedCourse == null) {
            System.out.println("Error: Course '" + courseName + "' not found.");
            return;
        }
    
        // Check if teacher has permission for this specific course
        if (currentUser.getRole() == RoleUtil.Role.TEACHER) {
            if (!currentUser.canAccessCourse(selectedCourse)) {
                System.out.println("Access denied. You can only generate reports for courses you teach.");
                return;
            }
        }
    
        // Generate and display report
        String courseReport = ReportGenerator.generateCourseReport(selectedCourse);
        System.out.println("\n" + courseReport);
    }
}
