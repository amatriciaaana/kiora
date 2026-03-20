package dev.kiora.path;

/**
 * Signals that a path string does not follow path syntax rules.
 */
public final class PathSyntaxException extends IllegalArgumentException {

    public PathSyntaxException(String message) {
        super(message);
    }
}
