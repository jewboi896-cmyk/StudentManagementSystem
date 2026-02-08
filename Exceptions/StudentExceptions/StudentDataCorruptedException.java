package Exceptions.StudentExceptions;

/*
Thrown when loaded student data has null required fields,
student object is in an impossible state,
or data integrity violation detected
 */

public class StudentDataCorruptedException extends RuntimeException {
    public StudentDataCorruptedException(String message) {
        super(message);
    }
}
