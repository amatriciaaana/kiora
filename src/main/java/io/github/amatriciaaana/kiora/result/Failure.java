package io.github.amatriciaaana.kiora.result;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Failed result cause.
 *
 * @param <T> value type
 */
public record Failure<T>(Throwable cause) implements Result<T> {

    public Failure {
        Objects.requireNonNull(cause, "cause must not be null");
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public T orElse(T fallback) {
        return fallback;
    }

    @Override
    public T orElseGet(Function<? super Throwable, ? extends T> fallback) {
        Objects.requireNonNull(fallback, "fallback must not be null");
        return fallback.apply(cause);
    }

    @Override
    public T orElseThrow() {
        if (cause instanceof RuntimeException runtimeException) {
            throw runtimeException;
        }
        if (cause instanceof Error error) {
            throw error;
        }
        throw new ResultException(cause);
    }

    @Override
    public Throwable errorOrNull() {
        return cause;
    }

    @Override
    public Optional<T> toOptional() {
        return Optional.empty();
    }

    @Override
    public <R> Result<R> map(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");
        return new Failure<>(cause);
    }

    @Override
    public <R> Result<R> flatMap(Function<? super T, Result<R>> mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");
        return new Failure<>(cause);
    }

    @Override
    public Result<T> recover(Function<? super Throwable, ? extends T> recovery) {
        Objects.requireNonNull(recovery, "recovery must not be null");
        return Result.success(recovery.apply(cause));
    }
}
