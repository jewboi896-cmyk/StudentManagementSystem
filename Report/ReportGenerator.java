package Report;

import Assignments.Assignment;
import Course.Course;
import GPACalculator.GpaCalculator;
import Grades.TermGrade;
import Main.Entry;
import Student.Student;
import Term.AcademicTerm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportGenerator {
    
    // Generate a detailed report for a single student
    public static String generateStudentReport(Student student) {
        StringBuilder report = new StringBuilder();
        
        // Header
        report.append("═══════════════════════════════════════════════════════\n");
        report.append("                    GRADE REPORT                       \n");
        report.append("═══════════════════════════════════════════════════════\n\n");
        
        // Student Info
        report.append("Student: ").append(student.getStudentName()).append("\n");
        report.append("Year: ").append(student.getStudentYear()).append("\n");
        
        // Calculate and display GPA
        Map<String, Map<AcademicTerm, TermGrade>> grades = student.getCourseGrades();
        if (!grades.isEmpty()) {
            double gpa = GpaCalculator.calculateGPA(grades);
            report.append("GPA: ").append(String.format("%.2f", gpa)).append("\n");
        } 
        else { report.append("GPA: N/A (No grades yet)\n"); }
        
        report.append("\n───────────────────────────────────────────────────────\n");
        report.append("                    COURSE GRADES                      \n");
        report.append("───────────────────────────────────────────────────────\n\n");
        
        // List all enrolled courses
        if (student.getEnrolledCourses().isEmpty()) {
            report.append("No courses enrolled.\n");
        } 
        else {
            for (String courseName : student.getEnrolledCourses()) {
                Course course = Entry.courseMap.get(courseName);
                if (course != null) {
                    report.append("Course: ").append(course.getCourseName()).append("\n");
                    report.append("Instructor: ").append(course.getCourseInstructor()).append("\n");
                    report.append("Credits: ").append(course.getCourseCredits()).append("\n");
                    
                    // Current grade
                    String grade = grades.get(courseName).toString();
                    report.append("Current Grade: ").append("No grade yet").append("\n");
                    
                    // List assignments for this course
                    report.append("\nAssignments:\n");
                    List<Assignment> courseAssignments = getAssignmentsForCourse(course);
                    
                    if (courseAssignments.isEmpty()) { report.append("  No assignments yet.\n"); }

                    else {
                        int totalEarned = 0;
                        int totalPossible = 0;
                        
                        for (Assignment assignment : courseAssignments) {
                            Integer score = student.getScoreForAssignment(assignment);
                            report.append("  • ").append(assignment.getAssignmentName());
                            report.append(" (").append(assignment.getCategory()).append("): ");
                            
                            if (score != null) {
                                report.append(score).append("/").append(assignment.getMaxScore());
                                totalEarned += score;
                                totalPossible += assignment.getMaxScore();
                            } 
                            else { report.append("Not submitted"); }
                            report.append("\n");
                        }
                        
                        // Show percentage if there are graded assignments
                        if (totalPossible > 0) {
                            double percentage = (totalEarned / (double) totalPossible) * 100;
                            report.append("\n  Total: ").append(totalEarned).append("/").append(totalPossible);
                            report.append(" (").append(String.format("%.1f", percentage)).append("%)\n");
                        }
                    }
                    
                    report.append("\n");
                }
            }
        }
        
        report.append("═══════════════════════════════════════════════════════\n");
        report.append("              End of Report                            \n");
        report.append("═══════════════════════════════════════════════════════\n");
        
        return report.toString();
    }
    
    // Generate a course report for teachers (class roster with grades)
    public static String generateCourseReport(Course course) {
        StringBuilder report = new StringBuilder();
        
        // Header
        report.append("═══════════════════════════════════════════════════════\n");
        report.append("                    COURSE REPORT                      \n");
        report.append("═══════════════════════════════════════════════════════\n\n");
        
        // Course Info
        report.append("Course: ").append(course.getCourseName()).append("\n");
        report.append("Instructor: ").append(course.getCourseInstructor()).append("\n");
        report.append("Course ID: ").append(course.getCourseID()).append("\n");
        report.append("Credits: ").append(course.getCourseCredits()).append("\n\n");
        
        // Get all enrolled students
        List<Student> enrolledStudents = new ArrayList<>();
        for (Student student : Entry.studentMap.values()) {
            if (student.isEnrolledInCourse(course.getCourseName())) { enrolledStudents.add(student); }
        }
        
        report.append("Total Enrolled: ").append(enrolledStudents.size()).append("\n\n");
        
        report.append("───────────────────────────────────────────────────────\n");
        report.append("                    STUDENT GRADES                     \n");
        report.append("───────────────────────────────────────────────────────\n\n");
        
        if (enrolledStudents.isEmpty()) { report.append("No students enrolled.\n"); }

        else {
            // Grade distribution counters
            int[] gradeDistribution = new int[13]; // A, A-, B+, B, B-, C+, C, C-, D+, D, D-, F, No Grade
            String[] gradeLabels = {"A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-", "F", "No Grade"};
            
            for (Student student : enrolledStudents) {
                String grade = student.getCourseGrades().get(course.getCourseName()).toString();
                
                report.append(String.format("%-25s %-15s Grade: %s\n", 
                    student.getStudentName(), "(" + student.getStudentYear() + ")", grade != null ? grade : "No grade yet"));
                
                // Track grade distribution
                if (grade != null) {
                    for (int i = 0; i < gradeLabels.length - 1; i++) {
                        if (grade.equals(gradeLabels[i])) {
                            gradeDistribution[i]++;
                            break;
                        }
                    }
                } 
                else { gradeDistribution[12]++; }// No grade
            }
            
            // Display grade distribution
            report.append("\n───────────────────────────────────────────────────────\n");
            report.append("                 GRADE DISTRIBUTION                    \n");
            report.append("───────────────────────────────────────────────────────\n\n");
            
            for (int i = 0; i < gradeLabels.length; i++) {
                if (gradeDistribution[i] > 0) {
                    report.append(String.format("%-10s: %d student(s)\n", gradeLabels[i], gradeDistribution[i]));
                }
            }
        }
        
        report.append("\n═══════════════════════════════════════════════════════\n");
        report.append("              End of Report                            \n");
        report.append("═══════════════════════════════════════════════════════\n");
        
        return report.toString();
    }

    public static String generateTermReport(AcademicTerm term) {
        StringBuilder report = new StringBuilder();

        // header
        report.append("═══════════════════════════════════════════════════════\n");
        report.append("                    TERM REPORT                      \n");
        report.append("═══════════════════════════════════════════════════════\n\n");

        // term info
        report.append("Term Name: ").append(term.getTermName()).append("\n");
        report.append("Start Date: ").append(term.getStartDate()).append("\n");
        report.append("End Date: ").append(term.getEndDate()).append("\n");
        report.append("Term Status: ").append(term.getTermStatus()).append("\n\n");

        // get all courses in current term
        List<Course> courses = new ArrayList<>();
        for (Course courseName : Entry.courseMap.values()) {
            if (courseName.isInTerm(courseName.getCourseName())) { courses.add(courseName); }
        }
        // get number of courses in current term
        report.append("Total Courses: ").append(courses.size()).append("\n\n");

        // make sure array isn't empty
        if (courses.isEmpty()) { report.append("No courses found.\n"); }

        // generate course report for each course
        for (Course courseName : Entry.courseMap.values()) {
            generateCourseReport(courseName);
        }

        report.append("\n═══════════════════════════════════════════════════════\n");
        report.append("              End of Report                            \n");
        report.append("═══════════════════════════════════════════════════════\n");

        return report.toString();
    }
    
    // Helper method to get all assignments for a course
    private static List<Assignment> getAssignmentsForCourse(Course course) {
        List<Assignment> assignments = new ArrayList<>();
        for (Assignment assignment : Entry.assignmentMap.values()) {
            if (assignment.getCourse().equals(course)) { assignments.add(assignment); }
        }
        return assignments;
    }
}
