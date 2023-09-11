package org.themullers.smugmugDownloader.scribe;

/**
 * A runtime wrapper exception that is re-thrown whenever an exception is caught in this app.
 */
public class SmugMugException extends RuntimeException {
    public SmugMugException(Exception cause) {
        super(cause);
    }

    public SmugMugException(String message) {
        super(message);
    }

}
