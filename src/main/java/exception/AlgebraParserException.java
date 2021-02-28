package exception;

public class AlgebraParserException extends Error {

    public AlgebraParserException(String message) {
        super(message);
    }

    public AlgebraParserException(String message, Exception e) {
        super(message, e);
    }
}
