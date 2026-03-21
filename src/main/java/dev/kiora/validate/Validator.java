package dev.kiora.validate;

import dev.kiora.result.Result;
import dev.kiora.text.Texts;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public final class Validator<T> {

    private final T value;
    private final List<String> errors;

    private Validator(T value) {
        this.value = value;
        this.errors = new ArrayList<>();
    }

    public static <T> Validator<T> of(T value) {
        return new Validator<>(value);
    }

    public Validator<T> check(boolean condition, String message) {
        if (!condition) {
            errors.add(message);
        }
        return this;
    }

    public Validator<T> check(Predicate<? super T> predicate, String message) {
        return check(predicate.test(value), message);
    }

    public Validator<T> notNull(String fieldName) {
        return check(value != null, fieldName + " must not be null");
    }

    public <V> Validator<T> notNull(Function<? super T, ? extends V> extractor, String fieldName) {
        return check(extractor.apply(value) != null, fieldName + " must not be null");
    }

    public Validator<T> notBlank(Function<? super T, String> extractor, String fieldName) {
        return check(!Texts.isBlank(extractor.apply(value)), fieldName + " must not be blank");
    }

    public <V> Validator<T> satisfies(
            Function<? super T, ? extends V> extractor,
            Predicate<? super V> predicate,
            String message
    ) {
        return check(predicate.test(extractor.apply(value)), message);
    }

    public Validator<T> rangeInt(ToIntFunction<? super T> extractor, int minInclusive, int maxInclusive, String fieldName) {
        int extracted = extractor.applyAsInt(value);
        return check(
                extracted >= minInclusive && extracted <= maxInclusive,
                fieldName + " must be between " + minInclusive + " and " + maxInclusive
        );
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public List<String> errors() {
        return List.copyOf(errors);
    }

    public Result<T> toResult() {
        if (isValid()) {
            return Result.success(value);
        }
        return Result.failure(new ValidationException(errors));
    }
}
