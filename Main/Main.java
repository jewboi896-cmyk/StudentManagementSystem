package Main;

import Assignments.Assignment;
import Attendance.AttendanceRecord;
import Authentication.AuthService;
import Course.Course;
import GPACalculator.GpaCalculator;
import GradeCalculator.GradeCalculator;
import Grades.GradeResult;
import Grades.TermGrade;
import Menu.Menu;
import Student.Student;
import Term.AcademicTerm;
import Term.AcademicTerm.TermStatus;
import Term.AcademicYear;
import User.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import Course.CourseSchedular;

public class Main {
    // Data structures to hold courses, students, assignments, etc
    public static ConcurrentHashMap<String, Course> courseMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Student> studentMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Assignment> assignmentMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, User> userMap = new ConcurrentHashMap<>();
    public static AuthService authentication = new AuthService();
    public static ConcurrentHashMap<String, AttendanceRecord> attendanceMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, AcademicYear> yearMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, AcademicTerm> termMap = new ConcurrentHashMap<>();
    public static AcademicTerm currentTerm = null;

    public static void main(String[] args) throws Exception {
        Menu menu = new Menu(new Entry());
        menu.start();
    }

    // Method to get specific course attributes
    public ConcurrentHashMap<String, String> getCourseAttributes(Course course) {
        ConcurrentHashMap<String, String> courseAttributes = new ConcurrentHashMap<>();
        // Add course attributes
        courseAttributes.put("Course name: ", course.getCourseName());
        courseAttributes.put("Course instructor: ", course.getCourseInstructor());
        courseAttributes.put("Course ID: ", course.getCourseID());
        courseAttributes.put("Credits: ", String.valueOf(course.getCourseCredits()));
        courseAttributes.put("Course size: ", String.valueOf(course.getCourseSize()));
        courseAttributes.put("Course Description:", course.getCourseDescription());
        return courseAttributes;
    }

    // Method to get specific student attributes
    public ConcurrentHashMap<String, String> getStudentAttributes(Student student) {
        ConcurrentHashMap<String, String> studentAttributes = new ConcurrentHashMap<>();
        // Add student attributes
        studentAttributes.put("Student name: ", student.getStudentName());
        studentAttributes.put("Age: ", String.valueOf(student.getStudentAge()));
        studentAttributes.put("Student Year: ", student.getStudentYear());
        // Calculate and add GPA
        Map<String, Map<AcademicTerm, TermGrade>> studentGrades = student.getCourseGrades();
            // Calculate and add GPA if grades exist
            if (studentGrades != null && !studentGrades.isEmpty()) {
                studentAttributes.put("GPA: ", String.valueOf(GpaCalculator.calculateGPA(studentGrades)));
            }
        return studentAttributes;
    }

    // Method to get specific assignment attributes
    public ConcurrentHashMap<String, String> getAssignmentAttributes(Assignment assignment, Student student) {
        ConcurrentHashMap<String, String> assignmentAttributes = new ConcurrentHashMap<>();
        // Add assignment attributes
        assignmentAttributes.put("Assignment name: ", assignment.getAssignmentName());
        assignmentAttributes.put("Course: ", assignment.getCourse().getCourseName());
        assignmentAttributes.put("Total points: ", String.valueOf(assignment.getMaxScore()));
        assignmentAttributes.put("Points earned: ", String.valueOf(student.getScoreForAssignment(assignment)));
        assignmentAttributes.put("Due date: ", assignment.getDueDate());
        assignmentAttributes.put("Instructions: ", assignment.getAssignmentDescriptor());
        return assignmentAttributes;
    }

    // Method to get specific term attributes
    public ConcurrentHashMap<String, String> getTermAttributes(AcademicTerm term) {
        ConcurrentHashMap<String, String> termAttributes = new ConcurrentHashMap<>();
        // Add term attributes
        termAttributes.put("Term name: ", term.getTermName());
        termAttributes.put("Start date: ", term.getStartDate().toString());
        termAttributes.put("End date: ", term.getEndDate().toString());
        termAttributes.put("Term type: ", term.getTermType().toString());
        termAttributes.put("Status: ", term.getTermStatus().toString());
        termAttributes.put("Locked: ", term.isLocked() ? "Yes" : "No");
        
        // Add current term indicator
        if (term.isTermCurrent(LocalDate.now())) {
            termAttributes.put("Current: ", "Yes - This is the active term");
        }
        
        return termAttributes;
    }

    // validate that course exists and get its key
    public Course courseValidation(String courseName) {
        // Iterate through course map to find matching course
        for (String key : courseMap.keySet()) {
            // Check for case-insensitive match
            if (key.equalsIgnoreCase(courseName)) { return courseMap.get(key); }
        }
        return null;
    }

    // validate that student exists and get its key
    public Student studentValidation(String studentName) {
        // Iterate through student map to find matching student
        for (String key : studentMap.keySet()) {
            // Check for case-insensitive match
            if (key.equalsIgnoreCase(studentName)) { return studentMap.get(key); }
        }
        return null;
    }

    // validate that assignment exists and get its key
    public Assignment assignmentValidation(String assignmentName) {
        // Iterate through assignment map to find matching assignment
        for (String key : assignmentMap.keySet()) {
            // Check for case-insensitive match
            if (key.equalsIgnoreCase(assignmentName)) { return assignmentMap.get(key); }
        }
        return null;
    }

    // validate that term exists and get its key
    public AcademicTerm termValidation(String termName) {
        // Iterate through term map to find matching term
        for (String key : termMap.keySet()) {
            // Check for case-insensitive match
            if (key.equalsIgnoreCase(termName)) { return termMap.get(key); }
        }
        return null;
    }

    // display course attributes
    public void displayCourseAttributes(Course course) {
        ConcurrentHashMap<String, String> courseAttributes = getCourseAttributes(course);
        // Iterate and print attributes
        for (String key : courseAttributes.keySet()) { 
            System.out.println(key + " " + courseAttributes.get(key)); 
        }
    }

    // display student attributes
    public void displayStudentAttributes(Student student) {
        ConcurrentHashMap<String, String> studentAttributes = getStudentAttributes(student);
        // Iterate and print attributes
        for (String key : studentAttributes.keySet()) {
            System.out.println(key + " " + studentAttributes.get(key));
        }
    }

    // display assignment attributes
    public void displayAssignmentAttributes(Assignment assignment, Student student) {
        ConcurrentHashMap<String, String> assignmentAttributes = getAssignmentAttributes(assignment, student);
        // Iterate and print attributes
        for (String key : assignmentAttributes.keySet()) {
            System.out.println(key + " " + assignmentAttributes.get(key));
        }
    }

    // display term attributes
    public void displayTermAttributes(AcademicTerm term) {
        ConcurrentHashMap<String, String> termAttributes = getTermAttributes(term);
        // Iterate and print attributes
        for (String key : termAttributes.keySet()) { 
            System.out.println(key + termAttributes.get(key)); 
        }
    }

    // display all attributes
    public void displayAllAttributes(Course course, Student student, Assignment assignment) {
        displayCourseAttributes(course);
        displayStudentAttributes(student);
        displayAssignmentAttributes(assignment, student);
    }

    // add a course to the map
    public static void addCourse(Course course) { courseMap.put(course.getCourseName(), course); }

    // remove a course from the map
    public static void removeCourse(Course course) {
        courseMap.remove(course.getCourseName());
        System.out.println("Course removed: " + course.getCourseName());
    }

    // add a student to the map
    public static void addStudent(Student student) { studentMap.put(student.getStudentName(), student); }

    // remove a student from the map
    public static void removeStudent(Student student) {
        studentMap.remove(student.getStudentName());
        System.out.println("Student removed: " + student.getStudentName());
    }

    // add an assignment to the map
    public static void addAssignment(Assignment assignment) { assignmentMap.put(assignment.getAssignmentName(), assignment); }

    // remove an assignment from the map
    public static void removeAssignment(Assignment assignment) {
        assignmentMap.remove(assignment.getAssignmentName());
        System.out.println("Assignment removed: " + assignment.getAssignmentName());
    }

    // add a term to the map
    public static void addTerm(AcademicTerm term) { termMap.put(term.getTermName(), term); }

    // remove a term from the map
    public static void removeTerm(AcademicTerm term) {
        termMap.remove(term.getTermName());
        System.out.println("Term removed: " + term.getTermName());
    }

    // init all terms
    private static void initTerms() {
        // create term obj
        AcademicTerm term1 = new AcademicTerm("Term1", LocalDate.parse("2025-09-04"), LocalDate.parse("2025-11-30"), AcademicTerm.TermType.SEMESTER, TermStatus.ACTIVE);
        // add term to map
        addTerm(term1);
    }

    // Initialize courses
    private static void initCourses() {
        // Create a sample course
        Course course1 = new Course("Introduction to Programming", "Dr. Smith", "CS101", 4, 30, currentTerm, CourseSchedular.DaysOfTheWeek.MONDAY);
        // Add category weights
        course1.addCategoryWeight("Homework", 0.1);
        course1.addCategoryWeight("Quizzes", 0.1);
        course1.addCategoryWeight("Exams", 0.4);
        course1.addCategoryWeight("Final", 0.2);
        course1.addCategoryWeight("Midterm", 0.2);
        // add course to the map
        addCourse(course1);
    }

    // Initialize students
    private static void initStudents() {
        // Create sample students
        Student student1 = new Student("Alice Johnson", 20, "Sophomore");
        Student student2 = new Student("Bob Smith", 18, "Freshman");

        // add course grade for the student
        Course course1 = courseMap.get("Introduction to Programming");
        
        // enroll students in course
        student1.enrollInCourse(course1);
        student2.enrollInCourse(course1);

        // add assignment score for the student
        Assignment assignment1 = assignmentMap.get("Homework 1");
        student1.addAssignmentScore(assignment1, 28);
        student2.addAssignmentScore(assignment1, 25);

        // get course grade as a percent
        GradeResult aliceGradeResult = calculateStudentCourseGradeWithPercentage(student1, course1);
        GradeResult bobGradeResult = calculateStudentCourseGradeWithPercentage(student1, course1);

        // extract values
        String aliceLetter = aliceGradeResult.getLetterGrade();
        String bobLetter = bobGradeResult.getLetterGrade();
        int alicePercentage = aliceGradeResult.getPercentageGrade();
        int bobPercentage = bobGradeResult.getPercentageGrade();

        // create TermGrade objects
        TermGrade aliceTermGrade = new TermGrade(student1, course1, aliceGradeResult.getLetterGrade(), aliceGradeResult.getPercentageGrade(), "");
        TermGrade bobTermGrade = new TermGrade(student2, course1, bobGradeResult.getLetterGrade(), bobGradeResult.getPercentageGrade(), "");

        // add course grades
        student1.addCourseGrade(course1, currentTerm, aliceTermGrade);
        student2.addCourseGrade(course1, currentTerm, bobTermGrade);

        // add students to the map
        addStudent(student1);
        addStudent(student2);
    }

    // Initialize assignments
    private static void initAssignments() {
        // Create a sample assignment
        Assignment assignment1 = new Assignment("Homework 1", 30, "2023-09-15", "Write a program to sort an array", courseMap.get("Introduction to Programming"), Assignment.AssignmentCategories.HOMEWORK, TermStatus.ACTIVE);
        // add assignment to the map
        addAssignment(assignment1);
    }

    // Initialize all data
    public static void initAllData() {
        initTerms();
        initCourses();
        initAssignments();
        initStudents();
    }

    public static GradeResult calculateStudentCourseGradeWithPercentage(Student student, Course course) {
        List<Assignment> courseAssignments = new ArrayList<>();
        for (Assignment assignment: assignmentMap.values()) {
            if (assignment.getCourse().equals(course)) {
                courseAssignments.add(assignment);
            }
        }

        // Check if there are assignments for the course
        if (courseAssignments.isEmpty()) {
            return new GradeResult("N/A", 0);
        }

        // Use weighted calculation if course has category weights
        Map<String, Double> categoryWeights = course.getCategoryWeights();

        // Calculate percentage grade
        int percentageGrade = (categoryWeights != null && !categoryWeights.isEmpty()) ? GradeCalculator.calculateWeightedCourseGrade(courseAssignments, student, categoryWeights) : GradeCalculator.calculateCourseGrade(courseAssignments, student);

        // Convert to letter grade
        String letterGrade = GradeCalculator.percentageToLetterGrade(percentageGrade);

        // Return both values wrapped in a GradeResult object
        return new GradeResult(letterGrade, percentageGrade);
    }

    public static AttendanceRecord getAttendanceRecord(Student student, Course course, LocalDate date, String period) {
        String key = student.getStudentName() + ":" + course.getCourseName() + ":" + date.toString() + ":" + period;
        return attendanceMap.get(key);
    }
    public static AcademicTerm getTermByDate(LocalDate date, AcademicTerm terms) {
        for (AcademicTerm term : Entry.termMap.values()) {
            if (term.doesDateFallWithinTerm(date)) {
                return term;
            }
        }
        return null;  // No term found for this date
    }
}