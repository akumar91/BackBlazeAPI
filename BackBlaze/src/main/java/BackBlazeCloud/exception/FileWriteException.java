package BackBlazeCloud.exception;

public class FileWriteException extends RuntimeException {

    /**
     * default serial  version id
     */
    private static final long serialVersionUID = 1L;

    public FileWriteException(String message) {
        super(message);
    }
}
