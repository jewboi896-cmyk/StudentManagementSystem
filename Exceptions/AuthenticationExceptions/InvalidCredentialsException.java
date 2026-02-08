package Exceptions.AuthenticationExceptions;

public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException(String credentials) {
        super("Error: The credentials " + credentials + " you have entered are invalid");
    }
}
