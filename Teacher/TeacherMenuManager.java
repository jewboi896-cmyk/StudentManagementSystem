package Teacher;

import User.User;
import Course.Course;
import Main.Main;
import Role.RoleUtil;

import java.util.Scanner;

public class TeacherMenuManager {
    private final Scanner scanner;
    private final Main mainApp;

    public TeacherMenuManager(Scanner scanner, Main mainApp) {
        this.scanner = scanner;
        this.mainApp = mainApp;
    }

    public void assignTeacherToCourse(User currentUser) {
        // Only admins can assign teachers
        if (currentUser.getRole() != RoleUtil.Role.ADMIN) {
            System.out.println("Access denied. Only administrators can assign teachers to courses.");
            return;
        }

        System.out.println("\n=== Assign Teacher to Course ===");

        // Get teacher name
        System.out.print("Enter teacher name: ");
        String teacherName = scanner.nextLine().trim();

        // Validate that a user with teacher role exists with this assocID
        if (!isValidTeacher(teacherName)) {
            System.out.println("Error: No teacher found with the name '" + teacherName + "'.");
            System.out.println("Note: The teacher must have a user account in the system.");
            return;
        }

        // Get course name
        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine().trim();

        // Validate course
        Course course = mainApp.courseValidation(courseName);
        if (course == null) {
            System.out.println("Error: Course '" + courseName + "' not found.");
            return;
        }

        // Check if course already has this teacher
        if (course.getCourseInstructor().equals(teacherName)) {
            System.out.println("Error: " + teacherName + " is already assigned to " + courseName + ".");
            return;
        }

        // Show current instructor
        String currentInstructor = course.getCourseInstructor();
        System.out.println("\nCurrent instructor: " + currentInstructor);
        System.out.print("Replace " + currentInstructor + " with " + teacherName + "? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (!confirmation.equals("yes") && !confirmation.equals("y")) {
            System.out.println("Assignment cancelled.");
            return;
        }

        // Update the course instructor
        course.setInstructor(teacherName);
        System.out.println("Success! " + teacherName + " has been assigned to " + courseName + ".");
    }

    public void removeTeacherFromCourse(User currentUser) {
        // Only admins can remove teachers
        if (currentUser.getRole() != RoleUtil.Role.ADMIN) {
            System.out.println("Access denied. Only administrators can remove teachers from courses.");
            return;
        }

        System.out.println("\n=== Remove Teacher from Course ===");

        // Get course name
        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine().trim();

        // Validate course
        Course course = mainApp.courseValidation(courseName);
        if (course == null) {
            System.out.println("Error: Course '" + courseName + "' not found.");
            return;
        }

        // Show current instructor
        String currentInstructor = course.getCourseInstructor();
        System.out.println("\nCurrent instructor: " + currentInstructor);
        System.out.print("Remove " + currentInstructor + " from this course? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (!confirmation.equals("yes") && !confirmation.equals("y")) {
            System.out.println("Removal cancelled.");
            return;
        }

        // Set instructor to "Unassigned" or empty
        course.setInstructor("Unassigned");
        System.out.println("Success! " + currentInstructor + " has been removed from " + courseName + ".");
        System.out.println("Course is now unassigned. Use 'Assign Teacher to Course' to assign a new instructor.");
    }

    public void viewTeacherCourses(User currentUser) {
        // Admins and teachers can view
        if (currentUser.getRole() != RoleUtil.Role.ADMIN && currentUser.getRole() != RoleUtil.Role.TEACHER) {
            System.out.println("Access denied. You do not have the required permissions to access this data.");
            return;
        }

        System.out.println("\n=== View Teacher's Courses ===");

        String teacherName;
        
        // If user is a teacher, show their courses automatically
        if (currentUser.getRole() == RoleUtil.Role.TEACHER) {
            teacherName = currentUser.getAssocID();
            System.out.println("Viewing your assigned courses...");
        } else {
            // Admin can view any teacher's courses
            System.out.print("Enter teacher name: ");
            teacherName = scanner.nextLine().trim();

            if (!isValidTeacher(teacherName)) {
                System.out.println("Error: No teacher found with the name '" + teacherName + "'.");
                return;
            }
        }

        // Find all courses taught by this teacher
        System.out.println("\n--- Courses taught by " + teacherName + " ---");
        System.out.println("----------------------------------------");

        boolean foundCourses = false;

        for (Course course : Main.courseMap.values()) {
            if (course.getCourseInstructor().equals(teacherName)) {
                foundCourses = true;
                System.out.println("\nCourse: " + course.getCourseName());
                System.out.println("  Course ID: " + course.getCourseID());
                System.out.println("  Credits: " + course.getCourseCredits());
                System.out.println("  Class Size: " + course.getCourseSize());

                // Count enrolled students
                int enrolledCount = 0;
                for (Student.Student student : Main.studentMap.values()) {
                    if (student.isEnrolledInCourse(course.getCourseName())) {
                        enrolledCount++;
                    }
                }
                System.out.println("  Enrolled Students: " + enrolledCount);
            }
        }

        if (!foundCourses) {
            System.out.println("No courses assigned to " + teacherName + ".");
        }

        System.out.println("----------------------------------------");
    }

    public void listAllTeachers(User currentUser) {
        // Only admins can list all teachers
        if (currentUser.getRole() != RoleUtil.Role.ADMIN) {
            System.out.println("Access denied. Only administrators can view all teachers.");
            return;
        }

        System.out.println("\n=== All Teachers in System ===");
        System.out.println("----------------------------------------");

        boolean foundTeachers = false;

        // Find all users with TEACHER role
        for (User user : Main.authentication.getAllUsers()) {
            if (user.getRole() == RoleUtil.Role.TEACHER) {
                foundTeachers = true;
                String teacherName = user.getAssocID();
                System.out.println("\nTeacher: " + teacherName);
                System.out.println("  Username: " + user.getUsername());

                // Count courses taught
                int courseCount = 0;
                for (Course course : Main.courseMap.values()) {
                    if (course.getCourseInstructor().equals(teacherName)) {
                        courseCount++;
                    }
                }
                System.out.println("  Courses Teaching: " + courseCount);
            }
        }

        if (!foundTeachers) {
            System.out.println("No teachers found in the system.");
        }

        System.out.println("----------------------------------------");
    }

    // Helper method to validate if a teacher exists in the system
    private boolean isValidTeacher(String teacherName) {
        for (User user : Main.authentication.getAllUsers()) {
            if (user.getRole() == RoleUtil.Role.TEACHER && user.getAssocID().equals(teacherName)) {
                return true;
            }
        }
        return false;
    }
}