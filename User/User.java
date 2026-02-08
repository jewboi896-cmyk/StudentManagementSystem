package User;

import Assignments.Assignment;
import Course.Course;
import Grades.TermGrade;
import Main.Entry;
import Password.PasswordUtil;
import Role.RoleUtil;
import Student.Student;
import Term.AcademicTerm;
import java.util.Map;

public class User {
    private final String username;
    private final String password;
    private final RoleUtil.Role role;
    private final String assocID;

    public User(String username, String password, RoleUtil.Role role, String assocID) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.assocID = assocID;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public RoleUtil.Role getRole() { return role; }
    public String getAssocID() { return assocID; }
    public boolean checkPassword(String inputPassword) { return PasswordUtil.verifyPassword(inputPassword, this.password); }

    public boolean canAccessStudent(String studentID) {
        return switch (this.role) {
            case ADMIN, TEACHER -> true;
            case STUDENT -> this.assocID.equals(studentID);
            case PARENT -> {
                // Assuming assocID for parents is a comma-separated list of student IDs
                String[] childrenIDs = this.assocID.split(",");
                for (String id : childrenIDs) {
                    if (id.trim().equals(studentID)) {
                        yield true;
                    }
                }
                yield false;
            }
        };
    
    }

    public boolean hasRole(RoleUtil.Role checkRole) { return this.role == checkRole; }

    public boolean canAccessCourse(Course course) {
        return switch (this.role) {
            case ADMIN -> true;
            case TEACHER -> this.assocID.equals(course.getCourseInstructor());
            case STUDENT -> {
                Student student = Entry.studentMap.get(this.assocID);
                yield student != null && student.isEnrolledInCourse(course.getCourseName());
            }
            case PARENT -> {
                // For parents, check if their child is enrolled
                String[] childrenIDs = this.assocID.split(",");
                for (String childId : childrenIDs) {
                    Student child = Entry.studentMap.get(childId.trim());
                    if (child != null && child.isEnrolledInCourse(course.getCourseName())) {
                        yield true;
                    }
                }
                yield false;
            }
        };
    }

    public boolean canAccessAssignment(Assignment assignment) { return canAccessCourse(assignment.getCourse()); }

    public boolean canAccessTerm(String term) {
        return switch(this.role) {
            case ADMIN, TEACHER -> true;
            case STUDENT -> {
                // get student ID from student map
                Student student = Entry.studentMap.get(this.assocID);
                // if student doesn't exist, yield false but continue with rest of method
                if (student == null) { yield false; }
                // check if student has grades within current term
                Map<String, Map<AcademicTerm, TermGrade>> courseGrades = student.getCourseGrades();
                // iterate through all courses that the student is enrolled in
                for (Map<AcademicTerm, TermGrade> termGrades: courseGrades.values()) {
                    // check if course has grade
                    if (termGrades.containsKey(term)) {
                        yield true;
                    }
                }
                yield false;
            }
            case PARENT -> false; 
        };
    }
}