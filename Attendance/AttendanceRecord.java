/*
This is a helper class for managing all attendence related things. dont modify 
 */

package Attendance;

import Course.Course;
import Student.Student;
import Term.AcademicTerm;
import java.time.LocalDate;

public class AttendanceRecord {
    private final Student student;
    private final Course course;
    private final LocalDate date;
    private final String period;    
    private AttendanceStatus attendenceStatus;
    private String notes;
    private AcademicTerm currentAcademicTerm;
    // create attendence statuses
    public enum AttendanceStatus {
        PRESENT,
        ABSENT,
        TARDY,
        EXCUSED
    }
    
    public AttendanceRecord(Student student, Course course, LocalDate date, String period, AcademicTerm term) {
        this.student = student;
        this.course = course;
        this.date = date;
        this.period = period;
        this.attendenceStatus = AttendanceStatus.PRESENT; 
        this.notes = "";
        this.currentAcademicTerm = term;
    }
    
    // Getters
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public LocalDate getDate() { return date; }
    public String getPeriod() { return period; }
    public AttendanceStatus getStatus() { return attendenceStatus; }
    public String getNotes() { return notes; }
    
    // Setters
    public void setStatus(AttendanceStatus status) { this.attendenceStatus = status; }
    public void setNotes(String notes) { this.notes = notes; }
    
    // get status as a string
    public String getStatusString() {
        return switch (attendenceStatus) {
            case PRESENT -> "Present";
            case ABSENT -> "Absent";
            case TARDY -> "Tardy";
            case EXCUSED -> "Excused Absence";
        };
    }
    
    // Create unique key for this attendance record
    public String getKey() {
        return student.getStudentName() + ":" + course.getCourseName() + ":" + date.toString() + ":" + period;
    }
}