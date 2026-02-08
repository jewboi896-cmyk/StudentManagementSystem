package Exceptions.DataExceptions;

public class DataLoadException extends Exception {
    public DataLoadException(String filename, Throwable cause) {
        super("Failed to load data from: " + filename, cause);
    }
}
