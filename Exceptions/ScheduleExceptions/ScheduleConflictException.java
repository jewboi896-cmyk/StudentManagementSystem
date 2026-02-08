package Exceptions.ScheduleExceptions;

public class ScheduleConflictException extends Exception {
    public ScheduleConflictException(String currentCourse, String newCourse) {
        super("Error: The timing for course " + currentCourse + " will conflict with the timing for selected course " + newCourse);
    }
}
