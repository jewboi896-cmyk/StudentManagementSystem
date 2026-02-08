package Grades;

import Course.Course;
import Student.Student;
import Term.AcademicTerm;

public class TermGrade {
    private Student student;
    private Course course;
    private AcademicTerm academicTerm;
    private String lettergrade;
    private int percentageGrade;
    private boolean lockedStatus = false;
    private String comments;

    public TermGrade(Student student, Course course, String termGrade, int percentageGrade, String comments) {
        this.student = student;
        this.course = course;
        this.lettergrade = termGrade;
        this.percentageGrade = percentageGrade;
        this.comments = comments;
    }

    // getters
    public Student getStudent() { return this.student; }
    public Course getCourse() { return this.course; }
    public AcademicTerm getAcacdemicTerm() { return this.academicTerm; }
    public String getLetterGrade() { return this.lettergrade; }
    public int getPercentageGrade() { return this.percentageGrade; }
    public boolean getTermStatus() { return this.lockedStatus; }
    public String getComments() { return this.comments; }

    // setters
    public void setStudent(Student student) { this.student = student; }
    public void setCourse(Course course) { this.course = course; }
    public void setAcademicTerm(AcademicTerm academicTerm) { this.academicTerm = academicTerm; }
    public void setLetterGrade(String letterGrade) { this.lettergrade = letterGrade; }
    public void setPercentGrade(int percentageGrade) { this.percentageGrade = percentageGrade; }
    public void setTermStatus(boolean lockedStatus) { this.lockedStatus = lockedStatus; }
    public void setComments(String comments) { this.comments = comments; }
}
