package io.github.amatriciaaana.kiora.result;

/**
 * Wraps checked exceptions when a failed result is converted back into a thrown exception.
 */
public final class ResultException extends RuntimeException {

    public ResultException(Throwable cause) {
        super(cause);
    }
}
