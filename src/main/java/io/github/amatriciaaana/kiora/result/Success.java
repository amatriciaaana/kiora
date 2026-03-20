package io.github.amatriciaaana.kiora.result;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Successful result value.
 *
 * @param <T> value type
 */
public record Success<T>(T value) implements Result<T> {

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public T orElse(T fallback) {
        return value;
    }

    @Override
    public T orElseGet(Function<? super Throwable, ? extends T> fallback) {
        Objects.requireNonNull(fallback, "fallback must not be null");
        return value;
    }

    @Override
    public T orElseThrow() {
        return value;
    }

    @Override
    public Throwable errorOrNull() {
        return null;
    }

    @Override
    public Optional<T> toOptional() {
        return Optional.ofNullable(value);
    }

    @Override
    public <R> Result<R> map(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");
        return Result.success(mapper.apply(value));
    }

    @Override
    public <R> Result<R> flatMap(Function<? super T, Result<R>> mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");
        return Objects.requireNonNull(mapper.apply(value), "mapper result must not be null");
    }

    @Override
    public Result<T> recover(Function<? super Throwable, ? extends T> recovery) {
        Objects.requireNonNull(recovery, "recovery must not be null");
        return this;
    }
}
