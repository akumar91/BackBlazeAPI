package BackBlazeCloud.exception;

public class ValidationException extends RuntimeException {

    /**
     * default serial  version id
     */
    private static final long serialVersionUID = 1L;

    public ValidationException(String message) {
        super(message);
    }
}