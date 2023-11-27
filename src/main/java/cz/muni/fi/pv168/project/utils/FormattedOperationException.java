package cz.muni.fi.pv168.project.utils;

/**
 * Exception thrown in case there is a problem with a bulk operation.
 */
public class FormattedOperationException extends RuntimeException {
    public FormattedOperationException(String message) {
        super(message);
    }

    public FormattedOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
