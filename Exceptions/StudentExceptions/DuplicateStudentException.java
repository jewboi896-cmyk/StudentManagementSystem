package Exceptions.StudentExceptions;

/*
Throws when trying to add a student that already exists,
student ID already in use,
or duplicate enrollment attempt
 */

public class DuplicateStudentException extends Exception {
    public DuplicateStudentException(String studentName) {
        super("Error: Student " + studentName + " already exists");
    }
}
