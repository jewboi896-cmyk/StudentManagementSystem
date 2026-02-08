package Student;

import Assignments.Assignment;
import Assignments.AssignmentSubmission;
import Course.Course;
import GPACalculator.GpaCalculator;
import Grades.TermGrade;
import Main.Entry;
import Term.AcademicTerm;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Student {
    private final String name;
    private final int age;
    private final String year;

    private String email;
    private String phone;
    private String address;
    private String studentId;

    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelation;

    private String parentName;
    private String parentEmail;
    private String parentPhone;

    private String medicalNotes;  // Allergies, conditions, etc.
    private String accommodations;  // IEP, 504 plan notes
    
    private final Map<String, Map<AcademicTerm, TermGrade>> courseGrades;
    private final Map<String, Integer> assignmentScore;
    private final Set<String> enrolledCourses;
    private final Map<String, AssignmentSubmission> trackSubmissions;
    
    public Student(String studentName, int studentAge, String studentYear) {
        this.name = studentName;
        this.age = studentAge;
        this.year = studentYear;
        this.courseGrades = new HashMap<>();
        this.assignmentScore = new HashMap<>();
        this.enrolledCourses = new HashSet<>();
        this.trackSubmissions = new HashMap<>();
        this.email = "";
        this.phone = "";
        this.address = "";
        this.studentId = "";
        this.emergencyContactName = "";
        this.emergencyContactPhone = "";
        this.emergencyContactRelation = "";
        this.parentName = "";
        this.parentEmail = "";
        this.parentPhone = "";
        this.medicalNotes = "";
        this.accommodations = "";
    }

    public void addCourseGrade(Course course, AcademicTerm term, TermGrade grade) { 
        String courseName = course.getCourseName();
    
        // Get or create the inner map for this course
        if (!courseGrades.containsKey(courseName)) {
            courseGrades.put(courseName, new HashMap<>());
        }
    
        // Add the grade for this term
        courseGrades.get(courseName).put(term, grade); 
    }

    public TermGrade getGradeForTerm(Course course, AcademicTerm currentTerm) {
        String courseName = course.getCourseName();

        // check if course is empty
        if (!courseGrades.containsKey(courseName)) {
            return null;
        }

        // get inner termGrade map
        Map<AcademicTerm, TermGrade> termGrade = courseGrades.get(courseName);

        // return the grade
        return termGrade.get(currentTerm);
    }

    public TermGrade getCurrentGrade(Course course) {
        return getGradeForTerm(course, Entry.currentTerm);
    }

    public double getTermGPA(AcademicTerm currentTerm) {
        Map<String, String> targetMap = new ConcurrentHashMap<>();
        // for each entry set in the map, get its value
        for (Map.Entry<String, Map<AcademicTerm, TermGrade>> entry: courseGrades.entrySet()) {
            Map<AcademicTerm, TermGrade> innerMap = entry.getValue();
            // if that value is in the current term, put it inside of the empty map(Map<String, String>)
            if (innerMap.containsKey(currentTerm)) {
                TermGrade gpa = innerMap.get(currentTerm);
                String letterGrade = String.valueOf(gpa.getLetterGrade());
                String courseName = entry.getKey();
                targetMap.put(courseName, letterGrade);
            }
        }
        return 0.0;
    }

    public double getCumulativeGPA() {
        Map<String,Map<AcademicTerm,TermGrade>> allGrades = new ConcurrentHashMap<>();
        // Loop through all courses
        for (Map.Entry<String, Map<AcademicTerm, TermGrade>> entry : courseGrades.entrySet()) {
            // get course key
            String courseName = entry.getKey();
            // add to map
            Map<AcademicTerm, TermGrade> termGrades = entry.getValue();
            // loop through course grades for all courses
            for (Map.Entry<AcademicTerm, TermGrade> termEntry : termGrades.entrySet()) {
                // Get the TermGrade object
                TermGrade letterGrade = termEntry.getValue();
                // Put courseName and letterGrade into allGrades
                allGrades.put(courseName, (Map<AcademicTerm, TermGrade>) letterGrade);
            }
        }
        return GpaCalculator.calculateGPA(allGrades);
    }

    public Map<AcademicTerm, TermGrade> getAllGradesForCourse(Course course) {
        String courseName = course.getCourseName();

        // check if course exists
        return courseGrades.getOrDefault(courseName, null);
    }

    public Map<String, Map<AcademicTerm, TermGrade>> getCourseGrades() { return courseGrades; }

    public void addAssignmentScore(Assignment assignment, int score) {
        assignmentScore.put(assignment.getAssignmentName(), score);
        
        // Also update submission if it exists
        String key = getSubmissionKey(assignment);
        AssignmentSubmission submission = trackSubmissions.get(key);
        if (submission != null) {
            submission.setScore(score);
            submission.setStatus(AssignmentSubmission.SubmissionStatus.GRADED);
        }
    }

    public Map<String, Integer> getAssignmentScores() { return assignmentScore; }
    public Integer getScoreForAssignment(Assignment assignment) { return assignmentScore.get(assignment.getAssignmentName()); }
    public void enrollInCourse(Course course) { enrolledCourses.add(course.getCourseName()); }
    public Set<String> getEnrolledCourses() { return enrolledCourses; }
    public boolean isEnrolledInCourse(String courseName) { return enrolledCourses.contains(courseName); }
    public String getStudentName() { return this.name; }
    public int getStudentAge() { return this.age; }
    public String getStudentYear() { return this.year; }
    public String getDetailedInfo() { return "Name: " + this.name + ", Age: " + this.age + ", Year: " + this.year; }

    public void removeAllCourseData(String courseName) {
        enrolledCourses.remove(courseName);
        courseGrades.remove(courseName);
        
        Course course = Entry.courseMap.get(courseName);
        if (course != null) {
            for (Assignment assignment : Entry.assignmentMap.values()) {
                if (assignment.getCourse().equals(course)) {
                    assignmentScore.remove(assignment.getAssignmentName());
                    trackSubmissions.remove(getSubmissionKey(assignment));
                }
            }
        }
    }

    // Submission tracking methods
    public void submitAssignment(Assignment assignment) {
        String key = getSubmissionKey(assignment);
        AssignmentSubmission submission = trackSubmissions.computeIfAbsent(key, k -> new AssignmentSubmission(assignment, this));

        submission.submit();
    }
    
    public AssignmentSubmission getSubmission(Assignment assignment) {
        String key = getSubmissionKey(assignment);
        return trackSubmissions.computeIfAbsent(key, k -> new AssignmentSubmission(assignment, this));
    }
    
    public Map<String, AssignmentSubmission> getAllSubmissions() { return trackSubmissions; }
    private String getSubmissionKey(Assignment assignment) { return assignment.getCourse().getCourseName() + ":" + assignment.getAssignmentName(); }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }
    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }
    public String getEmergencyContactRelation() { return emergencyContactRelation; }
    public void setEmergencyContactRelation(String emergencyContactRelation) { this.emergencyContactRelation = emergencyContactRelation; }
    public String getParentName() { return parentName; }
    public void setParentName(String parentName) { this.parentName = parentName; }
    public String getParentEmail() { return parentEmail; }
    public void setParentEmail(String parentEmail) { this.parentEmail = parentEmail;}
    public String getParentPhone() { return parentPhone; }
    public void setParentPhone(String parentPhone) { this.parentPhone = parentPhone; }
    public String getMedicalNotes() { return medicalNotes; }
    public void setMedicalNotes(String medicalNotes) { this.medicalNotes = medicalNotes; }
    public String getAccommodations() { return accommodations; }
    public void setAccommodations(String accommodations) { this.accommodations = accommodations; }
    
    // Get comprehensive student information
    public String getComprehensiveInfo() {
        StringBuilder info = new StringBuilder();
        info.append("=== Student Information ===\n");
        info.append("Name: ").append(name).append("\n");
        info.append("Student ID: ").append(studentId.isEmpty() ? "Not set" : studentId).append("\n");
        info.append("Age: ").append(age).append("\n");
        info.append("Year: ").append(year).append("\n");
        
        if (!email.isEmpty()) {
            info.append("Email: ").append(email).append("\n");
        }
        if (!phone.isEmpty()) {
            info.append("Phone: ").append(phone).append("\n");
        }
        if (!address.isEmpty()) {
            info.append("Address: ").append(address).append("\n");
        }
        
        info.append("\n=== Parent/Guardian Information ===\n");
        if (!parentName.isEmpty()) {
            info.append("Parent Name: ").append(parentName).append("\n");
        }
        if (!parentEmail.isEmpty()) {
            info.append("Parent Email: ").append(parentEmail).append("\n");
        }
        if (!parentPhone.isEmpty()) {
            info.append("Parent Phone: ").append(parentPhone).append("\n");
        }
        
        info.append("\n=== Emergency Contact ===\n");
        if (!emergencyContactName.isEmpty()) {
            info.append("Contact Name: ").append(emergencyContactName).append("\n");
            info.append("Relation: ").append(emergencyContactRelation).append("\n");
            info.append("Phone: ").append(emergencyContactPhone).append("\n");
        } else {
            info.append("No emergency contact on file\n");
        }
        
        if (!medicalNotes.isEmpty() || !accommodations.isEmpty()) {
            info.append("\n=== Special Information ===\n");
            if (!medicalNotes.isEmpty()) {
                info.append("Medical Notes: ").append(medicalNotes).append("\n");
            }
            if (!accommodations.isEmpty()) {
                info.append("Accommodations: ").append(accommodations).append("\n");
            }
        }
        
        return info.toString();
    }
}