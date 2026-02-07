/*
This file defines helper methods for anything assignment related. don't touch this file
 */

package Assignments;

import Course.Course;
import Term.AcademicTerm.TermStatus;

public class Assignment {
    private final String name;
    private final int maxScore;
    private final String dueDate;
    private final String description;
    private final Course course;
    private final AssignmentCategories category;
    private TermStatus currentAcademicTerm;

    public enum AssignmentCategories {
        HOMEWORK("Homework"), QUIZ("Quiz"), EXAM("Exam"),
        MIDTERM("Midterm"), FINAL("Final"), PROJECT("Project"),
        LAB("Lab"), PARTICIPATION("Participation"),
        PRESENTATION("Presentation"),
        ESSAY("Essay"), DISCUSSION("Discussion"),
        EXTRA_CREDIT("Extra-Credit"), CUSTOM("Custom");

        private final String displayName;
        AssignmentCategories(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }

        public static AssignmentCategories fromString(String displayName) {
            for (AssignmentCategories categories : AssignmentCategories.values()) {
                if (categories.displayName.equals(displayName)) {
                    return categories;
                }
            }
            return AssignmentCategories.CUSTOM;
        }
    }

    public Assignment(String assignmentName, int maxAssignmentScore, String assignmentDueDate, String assignmentDescription, Course course, AssignmentCategories category, TermStatus currentAcademicTerm) {
        this.name = assignmentName;
        this.maxScore = maxAssignmentScore;
        this.dueDate = assignmentDueDate;
        this.description = assignmentDescription;
        this.course = course;
        this.category = category;
        this.currentAcademicTerm = currentAcademicTerm;
    }

    public Course getCourse() { return this.course; }
    public String getAssignmentName() { return this.name; }
    public int getMaxScore() { return this.maxScore; }
    public String getDueDate() { return this.dueDate; }
    public String getAssignmentDescriptor() { return this.description; }
    public AssignmentCategories getCategory() { return this.category; }
    public TermStatus getCurrentAcademicTerm() { return this.currentAcademicTerm; }
    public String getDetailedInfo() { return "Assignment Name: " + this.name + ", Max Score: " + this.maxScore + ", Student Score: " + ", Due Date: " + this.dueDate + ", Description: " + this.description; }
}