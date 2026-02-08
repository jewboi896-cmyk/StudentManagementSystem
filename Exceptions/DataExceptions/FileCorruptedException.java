package Exceptions.DataExceptions;

public class FileCorruptedException extends Exception {
    public FileCorruptedException(String fileName) {
        super("Error: File " + fileName + " is corrupted. Tough luck");
    }
}
