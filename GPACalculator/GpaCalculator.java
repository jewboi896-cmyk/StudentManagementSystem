/*
This file is a helper class to do GPA calculations
 */

package GPACalculator;

import Course.Course;
import Grades.TermGrade;
import Main.Main;
import Term.AcademicTerm;

import java.util.Map;
import java.util.Map.Entry;

public class GpaCalculator {
    
    // Convert letter grade to grade points
    public static double letterGradeToPoints(String letterGrade) {
        return switch (letterGrade) {
            case "A" -> 4.0;
            case "A-" -> 3.7;
            case "B+" -> 3.3;
            case "B" -> 3.0;
            case "B-" -> 2.7;
            case "C+" -> 2.3;
            case "C" -> 2.0;
            case "C-" -> 1.7;
            case "D+" -> 1.3;
            case "D" -> 1.0;
            case "D-" -> 0.7;
            case "F" -> 0.0;
            case "N/A" -> 0.0;
            default -> throw new IllegalArgumentException("Invalid grade: " + letterGrade);
        };
    }

    // takes courses/grades and calculates GPA
    public static double calculateGPA(Map<String,Map<AcademicTerm,TermGrade>> studentGrades) {
        double totalPoints = 0.0;
        int totalCredits = 0;
        // Iterate through courses and their corresponding grades
        for (Entry<String, Map<AcademicTerm, TermGrade>> gradeEntry : studentGrades.entrySet()) {
            String courseName = gradeEntry.getKey();
            Map<AcademicTerm, TermGrade> termGrades = gradeEntry.getValue();
            // Iterate through all terms for this course
            for (TermGrade termGrade : termGrades.values()) {

                // skip N/A grades
                if (termGrade.equals("N/A")) continue;

                Course selectedCourse = Main.courseMap.get(courseName);

                if (selectedCourse != null) {
                    double gradePoints = letterGradeToPoints(String.valueOf(termGrade));
                    int credits = selectedCourse.getCourseCredits();

                    totalPoints += gradePoints * credits;
                    totalCredits += credits;
                }
            }
        }
        // Avoid division by zero
        return totalCredits == 0 ? 0.0 : totalPoints / totalCredits;
    }
}
