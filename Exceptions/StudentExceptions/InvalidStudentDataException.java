package Exceptions.StudentExceptions;

/*
Throws when student age is negative or unrealistic,
student name is empty or contains invalid characters,
or when required student information is missing
 */

public class InvalidStudentDataException extends Exception {
    public InvalidStudentDataException(String studentName) {
        super("Error: Student " + studentName + " is invalid");
    }
}
