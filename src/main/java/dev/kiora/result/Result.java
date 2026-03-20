package dev.kiora.result;

import java.util.Optional;
import java.util.function.Function;

/**
 * Represents either a success value or a failure cause.
 *
 * @param <T> value type
 */
public sealed interface Result<T> permits Success, Failure {

    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    static <T> Result<T> failure(Throwable cause) {
        return new Failure<>(cause);
    }

    boolean isSuccess();

    default boolean isFailure() {
        return !isSuccess();
    }

    T orElse(T fallback);

    T orElseGet(Function<? super Throwable, ? extends T> fallback);

    T orElseThrow();

    Throwable errorOrNull();

    Optional<T> toOptional();

    <R> Result<R> map(Function<? super T, ? extends R> mapper);

    <R> Result<R> flatMap(Function<? super T, Result<R>> mapper);

    Result<T> recover(Function<? super Throwable, ? extends T> recovery);
}
