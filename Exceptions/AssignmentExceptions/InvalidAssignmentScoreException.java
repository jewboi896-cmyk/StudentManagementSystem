package Exceptions.AssignmentExceptions;

public class InvalidAssignmentScoreException extends Exception {
    public InvalidAssignmentScoreException(int score, int maxScore) {
        super("Invalid score: " + score + " (max: " + maxScore + ")");
    }
}
