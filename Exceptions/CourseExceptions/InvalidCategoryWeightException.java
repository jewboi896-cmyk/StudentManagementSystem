package Exceptions.CourseExceptions;

public class InvalidCategoryWeightException extends RuntimeException {
    public InvalidCategoryWeightException(String categoryWeight) {
        super("Error: Course " + categoryWeight + " is invalid");
    }
}
