package io.github.amatriciaaana.kiora.optional;

import io.github.amatriciaaana.kiora.result.Result;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Small utility methods that make {@link Optional} composition less repetitive.
 */
public final class MoreOptional {
    private MoreOptional() {
    }

    public static <T> Optional<T> ofNullable(T value) {
        return Optional.ofNullable(value);
    }

    public static <T> Optional<T> or(Optional<T> optional, Supplier<? extends T> fallbackSupplier) {
        Objects.requireNonNull(optional, "optional must not be null");
        Objects.requireNonNull(fallbackSupplier, "fallbackSupplier must not be null");
        return optional.isPresent() ? optional : Optional.ofNullable(fallbackSupplier.get());
    }

    public static <T> Optional<T> filterNot(Optional<T> optional, Predicate<? super T> predicate) {
        Objects.requireNonNull(optional, "optional must not be null");
        Objects.requireNonNull(predicate, "predicate must not be null");
        return optional.filter(predicate.negate());
    }

    public static <T> Result<T> toResult(Optional<T> optional, Supplier<? extends Throwable> errorSupplier) {
        Objects.requireNonNull(optional, "optional must not be null");
        Objects.requireNonNull(errorSupplier, "errorSupplier must not be null");
        return optional.<Result<T>>map(Result::success)
                .orElseGet(() -> Result.failure(errorSupplier.get()));
    }

    public static <T> Stream<T> streamOfNullable(T value) {
        return Optional.ofNullable(value).stream();
    }
}
