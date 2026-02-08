package Exceptions.StudentExceptions;

/*
Throws when user searches for a student that doesn't exist,
user tries to view/edit a non-existent student,
or looking up student by ID/name that isn't in the system
 */

public class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String studentName) {
        super("Error: Student not found with name " + studentName);
    }
}
