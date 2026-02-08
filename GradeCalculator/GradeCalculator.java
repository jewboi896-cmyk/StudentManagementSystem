/*
This file handles grade calculation 
 */

package GradeCalculator;

import Assignments.Assignment;
import Student.Student;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradeCalculator {
    
    // Calculate assignment grade as a percentage
    public static int calculateAssignmentGrade(int studentScore, int maxScore) {
        if (maxScore == 0) {
            throw new IllegalArgumentException("Max score cannot be zero.");
        }
        return (int) ((studentScore / (double) maxScore) * 100);
    }

    // Calculate overall course grade based on a list of assignments
    public static int calculateCourseGrade(List<Assignment> assignments, Student student) {
        int totalScore = 0;
        int totalMaxScore = 0;
        // Iterate through assignments to sum scores
        for (Assignment assignment : assignments) {
            Integer assignmentScore = student.getScoreForAssignment(assignment);  
                if (assignmentScore != null) {
                    totalScore += assignmentScore;
                    totalMaxScore += assignment.getMaxScore();
                }
        }
        // Avoid division by zero
        if (totalMaxScore == 0) { return 0; }

        return (int) ((totalScore / (double) totalMaxScore) * 100);
    }
    // Convert percentage to letter grade
    public static String percentageToLetterGrade(int percentage) {
        // percentage cannot be greater than 100 or less then zero, obviously
        if (percentage > 100 || percentage < 0) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100.");
        }

        if (percentage >= 93) { return "A"; }
        else if (percentage >= 90) { return "A-"; } 
        else if (percentage >= 87) { return "B+"; } 
        else if (percentage >= 83) { return "B"; } 
        else if (percentage >= 80) { return "B-"; } 
        else if (percentage >= 77) { return "C+"; } 
        else if (percentage >= 73) { return "C"; } 
        else if (percentage >= 70) { return "C-"; } 
        else if (percentage >= 67) { return "D+"; } 
        else if (percentage >= 63) { return "D"; } 
        else if (percentage >= 60) { return "D-"; }
        return "F";
    }
// Calculate weighted course grade based on category weights
    public static int calculateWeightedCourseGrade(List<Assignment> assignments, Student student, Map<String, Double> categoryWeights) {
        Map<String, List<Assignment>> assignmentsByCategory = new HashMap<>();

        double totalWeightedScore = 0.0;
        double totalWeightUsed = 0.0;
    
        // Group assignments by category
        for (Assignment assignment : assignments) {
            String assignmentCategory = String.valueOf(assignment.getCategory());
            // Initialize list if category not present
            if (!assignmentsByCategory.containsKey(assignmentCategory)) {
                assignmentsByCategory.put(assignmentCategory, new java.util.ArrayList<>());
            }
            assignmentsByCategory.get(assignmentCategory).add(assignment);
        }

        // Calculate weighted average for each category
        for (String assignmentCategory : assignmentsByCategory.keySet()) {
            List<Assignment> categoryAssignments = assignmentsByCategory.get(assignmentCategory);
            int categoryTotalScore = 0;
            int categoryTotalMaxScore = 0;
        // Sum scores for assignments in the category
        for (Assignment assignment : categoryAssignments) {
            Integer studentScore = student.getScoreForAssignment(assignment);
            if (studentScore != null) {
                categoryTotalScore += studentScore;
                categoryTotalMaxScore += assignment.getMaxScore();
            }
        }
        // Calculate category percentage and apply weight
        if (categoryTotalMaxScore > 0 && categoryWeights.containsKey(assignmentCategory)) {
            double categoryPercentage = (categoryTotalScore / (double) categoryTotalMaxScore) * 100;
            double weight = categoryWeights.get(assignmentCategory);
            totalWeightedScore += categoryPercentage * weight;
            totalWeightUsed += weight;
        }
    }  
    // Normalize if total weight used is less than 1
    if (totalWeightUsed == 0) { return 0; }

    // Scale the total weighted score to a percentage
    return (int) (totalWeightedScore / totalWeightUsed);
    }
}
