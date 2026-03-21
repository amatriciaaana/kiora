package dev.kiora.validate;

import java.util.List;

public final class ValidationException extends IllegalArgumentException {

    private final List<String> errors;

    public ValidationException(List<String> errors) {
        super(String.join(", ", List.copyOf(errors)));
        this.errors = List.copyOf(errors);
    }

    public List<String> errors() {
        return errors;
    }
}
