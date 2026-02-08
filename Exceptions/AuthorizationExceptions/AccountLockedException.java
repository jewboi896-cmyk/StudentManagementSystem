package Exceptions.AuthorizationExceptions;

public class AccountLockedException extends Exception {
    public AccountLockedException(String userName, Throwable reason) {
        super("Error: User " + userName + " is locked for " + reason);
    }
}
