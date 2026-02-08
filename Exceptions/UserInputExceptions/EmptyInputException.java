package Exceptions.UserInputExceptions;

public class EmptyInputException extends Exception {
    public EmptyInputException(String input) {
        super("Error: Empty input " + input + " . Tough luck");
    }
}
