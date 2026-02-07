/*
This file is a helper to display assignment menu(s)
 */

package Assignments;

import Course.Course;
import Date.DateUtil;
import Grades.GradeResult;
import Grades.TermGrade;
import Main.Main;
import Role.RoleUtil;
import Student.Student;
import Term.AcademicTerm;
import User.User;
import java.util.Scanner;

public class AssignmentMenuManager {
    private final Scanner scanner;
    private final Main mainApp;

    public AssignmentMenuManager(Scanner scanner, Main mainApp) {
        this.scanner = scanner;
        this.mainApp = mainApp;
    }

    public void viewAssignmentInfo(User currentUser) {
        System.out.println("\n=== View Assignment Information ===");
        System.out.print("Enter assignment name: ");
        String assignmentName = scanner.nextLine().trim();
        // Validate assignment
        Assignment assignment = mainApp.assignmentValidation(assignmentName);
        // Check if assignment exists
        if (assignment == null) {
            System.out.println("Error: Assignment '" + assignmentName + "' not found.");
            return;
        }

        if (!currentUser.canAccessAssignment(assignment)) {
            System.out.println("Access denied. You do not have permission to view this assignment's information");
            return;
        }
    
    // For assignments, we need a student to show their score
    // For now, just show assignment details without a specific student's score
        System.out.println("\n=== Assignment Details ===");
        System.out.println("Assignment name: " + assignment.getAssignmentName());
        System.out.println("Course: " + assignment.getCourse().getCourseName());
        System.out.println("Category: " + assignment.getCategory());
        System.out.println("Total points: " + assignment.getMaxScore());
        System.out.println("Due date: " + assignment.getDueDate());
        System.out.println("Instructions: " + assignment.getAssignmentDescriptor());
    }

    // only admins and teachers should be able to do this
    public void addNewAssignment(User currentUser) {
        // check if user has correct permissions
        if ((currentUser.getRole() != RoleUtil.Role.ADMIN) && (currentUser.getRole() != RoleUtil.Role.TEACHER)) {
            System.out.println("Access denied. You do not have the required permissions to access this data.");
            return;
        }

        System.out.println("\n=== Add New Assignment ===");
    
        // Get assignment information
        System.out.print("Enter assignment name: ");
        String name = scanner.nextLine().trim();
    
        // Check if assignment already exists
        if (Main.assignmentMap.containsKey(name)) {
            System.out.println("Error: An assignment with the name '" + name + "' already exists.");
            return;
        }

        // Get remaining assignment details
        System.out.print("Enter max score: ");
        int maxScore;
        try {
            maxScore = Integer.parseInt(scanner.nextLine().trim());
            if (maxScore < 0) {
                System.out.println("Error: Max score cannot be less than zero");
                return;
            }
        } 
        catch (NumberFormatException e) {
            System.out.println("Error: Invalid score. Please enter a number.");
            return;
        }

        // Get due date
        System.out.print("Enter due date (YYYY-MM-DD): ");
        String dueDate = scanner.nextLine().trim();

        // Get description
        System.out.print("Enter assignment description: ");
        String description = scanner.nextLine().trim();
    
        // Get the course this assignment belongs to
        System.out.print("Enter course name for this assignment: ");
        String courseName = scanner.nextLine().trim();

        // Validate course
        Course course = mainApp.courseValidation(courseName);

        // Check if course exists
        if (course == null) {
            System.out.println("Error: Course '" + courseName + "' not found.");
            return;
        }

        // Get assignment category
        System.out.print("Enter category (Homework/Quizzes/Exams/Final): ");
        Assignment.AssignmentCategories category = Assignment.AssignmentCategories.valueOf(scanner.nextLine().trim());
    
        // Create and add the assignment
        Assignment newAssignment = new Assignment(name, maxScore, dueDate, description, course, category, null);
        Main.addAssignment(newAssignment);

        System.out.println("Success! Assignment '" + name + "' has been added to " + courseName + ".");
    }
    public void editAssignmentScore(User currentUser) {
        // check user permissions
        if ((currentUser.getRole() != RoleUtil.Role.ADMIN) && (currentUser.getRole() != RoleUtil.Role.TEACHER)) {
            System.out.println("Access denied. You do not have the correct permissions to access this data.");
            return;
        }

        System.out.println("\n=== Edit Assignment Score ===");

        // Get student
        System.out.print("Enter student name: ");
        String studentName = scanner.nextLine().trim();

        // Validate student
        Student student = mainApp.studentValidation(studentName);

        // Check if student exists
        if (student == null) {
            System.out.println("Error: Student '" + studentName + "' not found.");
            return;
        }

        // Get assignment
        System.out.print("Enter assignment name: ");
        String assignmentName = scanner.nextLine().trim();

        // Validate assignment
        Assignment assignment = mainApp.assignmentValidation(assignmentName);

        // Check if assignment exists
        if (assignment == null) {
            System.out.println("Error: Assignment '" + assignmentName + "' not found.");
            return;
        }

        // Get submission info
        AssignmentSubmission submission = student.getSubmission(assignment);
    
        // Show current status
        System.out.println("\n--- Current Submission Status ---");
        System.out.println(submission.getSubmissionInfo());
    
        // Show current score if it exists
        Integer currentScore = student.getScoreForAssignment(assignment);

        // Display current score
        if (currentScore != null) {
            System.out.println("Current score: " + currentScore + "/" + assignment.getMaxScore());
        } 
        else {
            System.out.println("No score recorded yet for this assignment.");
        }
    
        // Check if submitted
        if (!submission.isSubmitted()) {
            System.out.println("\nWARNING: This assignment has not been submitted yet!");
            System.out.print("Continue grading anyway? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (!confirm.equals("yes") && !confirm.equals("y")) {
                System.out.println("Grading cancelled.");
                return;
            }
        }

        // Get new score
        System.out.print("Enter new score (max: " + assignment.getMaxScore() + "): ");
        int newScore;

        // Validate score input --dont think i need a try/catch but it works like this. might optimize later
        try {
            newScore = Integer.parseInt(scanner.nextLine().trim());
        } 
        catch (NumberFormatException e) {
            System.out.println("Error: Invalid score. Please enter a number.");
            return;
        }

        // if new score is greater then max score, then trigger error
        if (newScore < 0 || newScore > assignment.getMaxScore()) {
            System.out.println("Error: Score must be between 0 and " + assignment.getMaxScore());
            return;
        }
    
        // Get teacher comments
        System.out.print("Enter teacher comments (optional, press Enter to skip): ");
        String comments = scanner.nextLine().trim();

        // Update the score in both old system and new submission system
        student.addAssignmentScore(assignment, newScore);
        submission.grade(newScore, comments);

        // Recalculate course grade
        Course course = assignment.getCourse();
        GradeResult gradeResult = Main.calculateStudentCourseGradeWithPercentage(student, course);
        TermGrade termGrade = new TermGrade(student, course, gradeResult.getLetterGrade(), gradeResult.getPercentageGrade(), comments);
        student.addCourseGrade(course, Main.currentTerm, termGrade);

        System.out.println("\n=== Grade Updated Successfully ===");
        System.out.println("Score: " + newScore + "/" + assignment.getMaxScore());
        System.out.println("Status: " + submission.getStatusString());
        if (submission.isLate()) {
            System.out.println("Note: This submission was late.");
        }
        if (!comments.isEmpty()) {
            System.out.println("Comments saved: " + comments);
        }
        System.out.println("New course grade for " + course.getCourseName() + ": " + gradeResult);
    }

    public void viewAssignmentsByDueDate() {
        System.out.println("\n=== View Assignments by Due Date ===");

        System.out.println("Filter by:");
        System.out.println("1. Overdue");
        System.out.println("2. Due Soon (next 7 days)");
        System.out.println("3. Upcoming (beyond 7 days)");
        System.out.println("4. All assignments sorted by date");
        System.out.print("Enter your choice: ");
    
        String choice = scanner.nextLine().trim();
    
        // Get student to check completion status
        System.out.print("Enter student name (or leave blank for all students): ");
        String studentName = scanner.nextLine().trim();
    
        Student student = null;

        // Validate student if name provided
        if (!studentName.isEmpty()) {
            student = mainApp.studentValidation(studentName);
            if (student == null) {
                System.out.println("Error: Student '" + studentName + "' not found.");
                return;
            }
        }
    
        System.out.println("\n--- Assignment List ---");
        boolean foundAny = false;

        // Iterate through all assignments
        for (Assignment assignment : Main.assignmentMap.values()) {
            String dueDate = assignment.getDueDate();
            boolean hasScore = student != null && student.getScoreForAssignment(assignment) != null;
            String status = DateUtil.getAssignmentStatus(dueDate, hasScore);
        
        boolean shouldDisplay = false;
        
        switch (choice) {
            case "1" -> shouldDisplay = status.equals("Overdue");
            case "2" -> shouldDisplay = status.equals("Due Soon");
            case "3" -> shouldDisplay = status.equals("Upcoming");
            case "4" -> shouldDisplay = true;
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }
        
        if (shouldDisplay) {
            foundAny = true;
            System.out.println("\nAssignment: " + assignment.getAssignmentName());
            System.out.println("  Course: " + assignment.getCourse().getCourseName());
            System.out.println("  Category: " + assignment.getCategory());
            System.out.println("  Due Date: " + DateUtil.formatDate(dueDate));
            System.out.println("  Status: " + status);
            System.out.println("  Max Points: " + assignment.getMaxScore());
            
            if (student != null) {
                Integer score = student.getScoreForAssignment(assignment);
                if (score != null) {
                    System.out.println("  " + studentName + "'s Score: " + score + "/" + assignment.getMaxScore());
                } 
                else {
                    System.out.println("  " + studentName + "'s Score: Not submitted");
                }
            }
        }
    }
        // if no assignment found, inform user
        if (!foundAny) {
            System.out.println("No assignments found for the selected filter.");
        }
    }

    public void deleteAssignment(User currentUser) {
        // check user permissions
        if ((currentUser.getRole() != RoleUtil.Role.ADMIN) && (currentUser.getRole() != RoleUtil.Role.TEACHER)) {
            System.out.println("Access denied. You do not have the correct permissions to access this data.");
            return;
        }

        System.out.println("\n=== Delete Assignment ===");
        System.out.print("Enter assignment name to delete: ");
        String assignmentName = scanner.nextLine().trim();
        // Validate assignment
        Assignment assignment = mainApp.assignmentValidation(assignmentName);
        // Check if assignment exists
        if (assignment == null) {
            System.out.println("Error: Assignment '" + assignmentName + "' not found.");
            return;
        }
        // Confirm deletion in case the user is an idiot 
        System.out.print("Are you sure you want to delete assignment '" + assignmentName + "'? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (!confirmation.equals("yes") && !confirmation.equals("y")) {
            System.out.println("Deletion cancelled.");
            return;
        }
    
        // Remove assignment from the system
        Main.assignmentMap.remove(assignmentName);
        System.out.println("Success! Assignment '" + assignmentName + "' has been deleted.");
    }

    public void viewMissingAssignments(User currentUser) {
        if (currentUser.getRole() != RoleUtil.Role.TEACHER && currentUser.getRole() != RoleUtil.Role.ADMIN) {
            System.out.println("Access denied.");
            return;
        }

        System.out.println("\n=== View Missing Assignments ===");
        System.out.print("Enter course name (or leave blank for all courses): ");
        String courseName = scanner.nextLine().trim();

        Course targetCourse = null;
        if (!courseName.isEmpty()) {
            targetCourse = mainApp.courseValidation(courseName);
            if (targetCourse == null) {
                System.out.println("Error: Course '" + courseName + "' not found.");
                return;
            }
        
            // Check teacher permission
            if (currentUser.getRole() == RoleUtil.Role.TEACHER) {
                if (!currentUser.canAccessCourse(targetCourse)) {
                    System.out.println("Access denied. You can only view missing assignments for courses you teach.");
                    return;
                }
            }
        }

        System.out.println("\n--- Missing Assignments Report ---");
        boolean foundAny = false;

        for (Assignment assignment : Main.assignmentMap.values()) {
            // Filter by course if specified
            if (targetCourse != null && !assignment.getCourse().equals(targetCourse)) {
                continue;
            }

            // Skip if teacher doesn't have access to this course
            if (currentUser.getRole() == RoleUtil.Role.TEACHER && !currentUser.canAccessCourse(assignment.getCourse())) {
                continue;
            }

            System.out.println("\nAssignment: " + assignment.getAssignmentName());
            System.out.println("Course: " + assignment.getCourse().getCourseName());
            System.out.println("Due Date: " + assignment.getDueDate());
            System.out.println("Students with missing submissions:");

            boolean foundMissing = false;
            for (Student student : Main.studentMap.values()) {
                // if student is enrolled in the course and has a submission, get it
                if (student.isEnrolledInCourse(assignment.getCourse().getCourseName())) {
                    AssignmentSubmission submission = student.getSubmission(assignment);
                
                    if (submission.getStatus() == AssignmentSubmission.SubmissionStatus.MISSING || submission.getStatus() == AssignmentSubmission.SubmissionStatus.NOT_SUBMITTED) {
                        foundMissing = true;
                        foundAny = true;
                        System.out.println("  - " + student.getStudentName() + " (" + submission.getStatusString() + ")");
                    }
                }
            }

            if (!foundMissing) {
                System.out.println("  (None - all students have submitted)");
            }
        }

        if (!foundAny) {
            System.out.println("No missing assignments found!");
        }
    }
}
