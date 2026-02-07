package Term;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AcademicYear {
    private String yearName;
    private List<AcademicTerm> termList;
    private LocalDate startDate;
    private LocalDate endDate;

    public AcademicYear(String yearName, LocalDate startDate, LocalDate endDate) {
        this.yearName = yearName;
        this.termList = new ArrayList<>();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // getters
    public String getYearName() { return this.yearName; }
    public List<AcademicTerm> getTermList() {return this.termList; }
    public LocalDate getYearStartDate() { return this.startDate; }
    public LocalDate getYearEndDate() { return this.endDate; }

    // setters
    public void setYearName(String yearName) { this.yearName = yearName; }
    public void setYearStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setYearEndDate(LocalDate endDate) { this.endDate = endDate; }

    public AcademicTerm getCurrentAcademicTerm(LocalDate currentDate) {
        for (AcademicTerm term: termList) {
            if (term.isTermCurrent(currentDate)) {
                return term;
            }
        }
        return null;
    }

    public void addAcademicTerm(AcademicTerm newTerm) {
        termList.add(newTerm);
    }
}
