package Term;

import java.time.LocalDate;

public class AcademicTerm {
    private String termName;
    private LocalDate termStartDate;
    private LocalDate termEndDate;
    private TermType termType;
    private TermStatus termStatus;
    private boolean isLocked = false;
    // define enums
    public enum TermType {
        SEMESTER,
        QUARTER,
        TRIMESTER  
    }

    public enum TermStatus {
        ACTIVE,
        COMPLETED,
        UPCOMING
    }
    // constructor 
    public AcademicTerm(String termName, LocalDate termStartDate, LocalDate termEndDate, TermType termType, TermStatus termStatus) {
        this.termName = termName;
        this.termStartDate = termStartDate;
        this.termEndDate = termEndDate;
        this.termType = termType;
        this.termStatus = termStatus;
    }

    // getters
    public String getTermName() { return this.termName; }
    public LocalDate getStartDate() { return this.termStartDate; }
    public LocalDate getEndDate() { return this.termEndDate; }
    public TermType getTermType() { return this.termType; }
    public TermStatus getTermStatus() { return this.termStatus; }

    // setters
    public void setTermType(TermType termType) { this.termType = termType; }
    public void setTermStatus(TermStatus termStatus) {
        this.termStatus = termStatus;
    }

    public boolean isTermCurrent(LocalDate currentDate) {
        return ((currentDate.isAfter(termStartDate) || currentDate.isEqual(termStartDate)) && (currentDate.isBefore(termEndDate) || currentDate.isEqual(termEndDate)) && termStatus == TermStatus.ACTIVE);
    }

    public boolean doesDateFallWithinTerm(LocalDate currentDate) {
        return ((currentDate.isAfter(termStartDate) || currentDate.isEqual(termStartDate)) && (currentDate.isBefore(termEndDate) || currentDate.isEqual(termEndDate)) && termStatus == TermStatus.ACTIVE);
    }

    public void lockTerm() {
        this.isLocked = true;
        this.termStatus = TermStatus.COMPLETED;
    }

    public void unlockTerm() {
        this.isLocked = false;
    }

    public boolean isLocked() {
        return this.isLocked;
    }
}
