package exc;

public class JadwalBentrokException extends Exception {
    public JadwalBentrokException(String message) {
        super(message);
    }
    
    public JadwalBentrokException(String message, Throwable cause) {
        super(message, cause);
    }
}