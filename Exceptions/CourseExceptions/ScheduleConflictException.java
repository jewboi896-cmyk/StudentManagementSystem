package Exceptions.CourseExceptions;

public class ScheduleConflictException extends Exception {
    public ScheduleConflictException(String scheduleName) {
        super("Error: Schedule " + scheduleName + " conflicts with another course.");
    }
}
