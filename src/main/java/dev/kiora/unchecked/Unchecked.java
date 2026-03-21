package dev.kiora.unchecked;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Adapters for using checked-exception code in standard Java functional APIs.
 */
public final class Unchecked {
    private Unchecked() {
    }

    public static <T> T get(CheckedSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (RuntimeException exception) {
            throw exception;
        } catch (Error error) {
            throw error;
        } catch (Exception exception) {
            throw new UncheckedException(exception);
        }
    }

    public static void run(CheckedRunnable runnable) {
        try {
            runnable.run();
        } catch (RuntimeException exception) {
            throw exception;
        } catch (Error error) {
            throw error;
        } catch (Exception exception) {
            throw new UncheckedException(exception);
        }
    }

    public static <T> Supplier<T> supplier(CheckedSupplier<T> supplier) {
        return () -> get(supplier);
    }

    public static Runnable runnable(CheckedRunnable runnable) {
        return () -> run(runnable);
    }

    public static <T, R> Function<T, R> function(CheckedFunction<T, R> function) {
        return value -> get(() -> function.apply(value));
    }

    public static <T> Consumer<T> consumer(CheckedConsumer<T> consumer) {
        return value -> run(() -> consumer.accept(value));
    }

    public static <T> Predicate<T> predicate(CheckedPredicate<T> predicate) {
        return value -> get(() -> predicate.test(value));
    }

    @FunctionalInterface
    public interface CheckedSupplier<T> {
        T get() throws Exception;
    }

    @FunctionalInterface
    public interface CheckedRunnable {
        void run() throws Exception;
    }

    @FunctionalInterface
    public interface CheckedFunction<T, R> {
        R apply(T value) throws Exception;
    }

    @FunctionalInterface
    public interface CheckedConsumer<T> {
        void accept(T value) throws Exception;
    }

    @FunctionalInterface
    public interface CheckedPredicate<T> {
        boolean test(T value) throws Exception;
    }
}
