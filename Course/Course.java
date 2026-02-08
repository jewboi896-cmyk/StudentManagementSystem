/*
Course class. dont edit
 */

package Course;

import Term.AcademicTerm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Main.Main.courseMap;

public class Course {
    private final String courseName;
    private String courseInstructor;
    private final String courseID;
    private final int courseCredits;
    private final int courseSize;
    private final Map<String, Double> categoryWeights;
    private List<AcademicTerm> termsOffered;
    private AcademicTerm term;
    private CourseSchedular.DaysOfTheWeek daysOffered;

    public Course(String courseName, String courseInstructor, String courseID, int courseCredits, int courseSize, AcademicTerm term, CourseSchedular.DaysOfTheWeek daysOffered) {
        this.courseName = courseName;
        this.courseInstructor = courseInstructor;
        this.courseID = courseID;
        this.courseCredits = courseCredits;
        this.courseSize = courseSize;
        this.categoryWeights = new HashMap<>();
        this.termsOffered = new ArrayList<>();
        this.term = term;
        this.daysOffered = daysOffered;
    }

    public String getCourseName() { return "" + this.courseName; }
    public String getCourseInstructor() { return "" + this.courseInstructor; }
    public String getCourseID() { return this.courseID; }
    public int getCourseCredits() { return this.courseCredits; }
    public int getCourseSize() { return this.courseSize; }
    public void addCategoryWeight(String category, double weight) { this.categoryWeights.put(category, weight); }
    public Map<String, Double> getCategoryWeights() { return this.categoryWeights; }
    public String getDetailedInfo() { return "Course Name: " + this.courseName + "\nInstructor: " + this.courseInstructor + "\nID: " + this.courseID + "\nCredits: " + this.courseCredits + "\nSize: " + this.courseSize; }
    public String getCourseDescription() { return "This is a course named " + this.courseName + " taught by " + this.courseInstructor + ". The course ID is " + this.courseID + ", it has " + this.courseCredits + " credits, and the class size is " + this.courseSize + "."; }
    public List<AcademicTerm> getTermsOffered() { return this.termsOffered; }
    public void setInstructor(String newInstructor) { this.courseInstructor = newInstructor; }
    public AcademicTerm getAcademicTerm() { return this.term; }
    public AcademicTerm setNewAcademicTerm(AcademicTerm newTerm) { return this.term = newTerm; }
    public CourseSchedular.DaysOfTheWeek getDaysOffered() { return this.daysOffered; }
    public CourseSchedular.DaysOfTheWeek setNewDayOffered(CourseSchedular.DaysOfTheWeek newDay) { return this.daysOffered = newDay; }

    public boolean isCourseActive(AcademicTerm currentTerm) {
        return termsOffered.contains(currentTerm);
    }
    public boolean isInTerm(String courseName) {
        // if course in map, return true. otherwise throw exception
        try {
            if (courseMap.contains(courseName)) {
                return true;
            }
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }
}
