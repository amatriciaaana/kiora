package io.github.amatriciaaana.kiora.result;

/**
 * Factory methods for converting checked exception code into {@link Result} values.
 */
public final class Try {
    private Try() {
    }

    public static <T> Result<T> of(CheckedSupplier<T> supplier) {
        try {
            return Result.success(supplier.get());
        } catch (Throwable throwable) {
            return Result.failure(throwable);
        }
    }

    public static Result<Unit> run(CheckedRunnable runnable) {
        try {
            runnable.run();
            return Result.success(Unit.INSTANCE);
        } catch (Throwable throwable) {
            return Result.failure(throwable);
        }
    }

    @FunctionalInterface
    public interface CheckedSupplier<T> {
        T get() throws Exception;
    }

    @FunctionalInterface
    public interface CheckedRunnable {
        void run() throws Exception;
    }
}
