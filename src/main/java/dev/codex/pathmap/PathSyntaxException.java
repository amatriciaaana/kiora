package dev.codex.pathmap;

/**
 * Signals that a path string does not follow pathmap syntax rules.
 */
public final class PathSyntaxException extends IllegalArgumentException {

    public PathSyntaxException(String message) {
        super(message);
    }
}
