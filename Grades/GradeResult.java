package Grades;

public class GradeResult {
    private String letterGrade;
    private int percentageGrade;

    public GradeResult(String letterGrade, int percentageGrade) {
        this.letterGrade = letterGrade;
        this.percentageGrade = percentageGrade;
    }

    public String getLetterGrade() { return this.letterGrade; }
    public int getPercentageGrade() { return this.percentageGrade; }

    @Override
    public String toString() { return letterGrade + "(" + percentageGrade + "%)"; }
}
