package Exceptions.DataExceptions;

public class DataSaveException extends Exception {
    public DataSaveException(String fileName) {
        super("Error: File " + fileName + " has not been able to be saved correctly");
    }
}
