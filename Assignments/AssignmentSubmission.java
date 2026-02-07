/*
This is a helper class to assist in handling assignment submissions. this file shouldnt be touched
 */

package Assignments;

import Date.DateUtil;
import Student.Student;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AssignmentSubmission {
    private final Assignment assignment;
    private final Student student;
    private Integer score;
    private SubmissionStatus status;
    private LocalDateTime submittedAt;
    private String teacherComments;
    
    public enum SubmissionStatus {
        NOT_SUBMITTED,
        SUBMITTED,
        LATE,
        GRADED,
        MISSING
    }
    
    public AssignmentSubmission(Assignment assignment, Student student) {
        this.assignment = assignment;
        this.student = student;
        this.score = null;
        this.status = SubmissionStatus.NOT_SUBMITTED;
        this.submittedAt = null;
        this.teacherComments = "";
    }
    
    // Mark as submitted
    public void submit() {
        this.submittedAt = LocalDateTime.now();
        
        // Check if it's late
        if (DateUtil.isOverdue(assignment.getDueDate())) {
            this.status = SubmissionStatus.LATE;
        } 
        else { this.status = SubmissionStatus.SUBMITTED; }
    }
    
    // Grade the submission
    public void grade(int score, String comments) {
        this.score = score;
        this.teacherComments = comments;
        this.status = SubmissionStatus.GRADED;
    }
    // using this format just because it saves a little space, its not an optimization. im just lazy
    public void markAsMissing() { this.status = SubmissionStatus.MISSING; }
    public Assignment getAssignment() { return assignment;}
    public Student getStudent() { return student; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public SubmissionStatus getStatus() { return status; }
    public void setStatus(SubmissionStatus status) { this.status = status; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public String getTeacherComments() { return teacherComments; }
    public void setTeacherComments(String comments) { this.teacherComments = comments; }
    public boolean isLate() { return status == SubmissionStatus.LATE; }
    
    public boolean isSubmitted() {
        return status == SubmissionStatus.SUBMITTED || status == SubmissionStatus.LATE || status == SubmissionStatus.GRADED;
    }
    
    // Get submission info as string
    public String getSubmissionInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Status: ").append(getStatusString()).append("\n");
        
        if (submittedAt != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
            info.append("Submitted: ").append(submittedAt.format(formatter)).append("\n");
        }
        
        if (score != null) {
            info.append("Score: ").append(score).append("/").append(assignment.getMaxScore()).append("\n");
        }
        
        if (teacherComments != null && !teacherComments.isEmpty()) {
            info.append("Teacher Comments: ").append(teacherComments).append("\n");
        }
        
        return info.toString();
    }
    
    // Get status as readable string
    public String getStatusString() {
        return switch (status) {
            case NOT_SUBMITTED -> "Not Submitted";
            case SUBMITTED -> "Submitted";
            case LATE -> "Late";
            case GRADED -> "Graded";
            case MISSING -> "Missing";
        };
    }
}