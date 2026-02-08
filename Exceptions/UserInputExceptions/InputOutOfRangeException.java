package Exceptions.UserInputExceptions;

public class InputOutOfRangeException extends Exception {
    public InputOutOfRangeException(String input) {
        super("Error: Input " + input + " is invalid. Tough luck");
    }
}
