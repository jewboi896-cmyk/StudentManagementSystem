package Exceptions.DataExceptions;

public class DataIntegrityException extends RuntimeException {
    public DataIntegrityException(String data) {
        super("Error: Data " + data + " is corrupted. Tough luck");
    }
}
