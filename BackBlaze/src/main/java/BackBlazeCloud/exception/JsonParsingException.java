package BackBlazeCloud.exception;

public class JsonParsingException extends RuntimeException {

    /**
     * default serial  version id
     */
    private static final long serialVersionUID = 1L;

    public JsonParsingException(String message) {
        super(message);
    }
}
