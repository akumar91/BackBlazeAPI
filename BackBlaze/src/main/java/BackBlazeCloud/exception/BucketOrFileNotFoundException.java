package BackBlazeCloud.exception;

public class BucketOrFileNotFoundException extends RuntimeException {

    /**
     * default serial  version id
     */
    private static final long serialVersionUID = 1L;

    public BucketOrFileNotFoundException(String message) {
        super(message);
    }
}
