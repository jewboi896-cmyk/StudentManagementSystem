package Exceptions.UserInputExceptions;

public class MenuChoiceException extends Exception {
    public MenuChoiceException(String choice) {
        super("Error: Menu Choice " + choice + " is invalid. Tough luck");
    }
}
