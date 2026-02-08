package Exceptions.UserInputExceptions;

public class InvalidInputFormatException extends Exception {
    public InvalidInputFormatException(String input) {
        super("Error: Input " + input + " is invalid");
    }
}
