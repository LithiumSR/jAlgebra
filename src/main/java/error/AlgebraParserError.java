package error;

public class AlgebraParserError extends Error {

    public AlgebraParserError(String message) {
        super(message);
    }

    public AlgebraParserError(String message, Exception e) {
        super(message, e);
    }
}
