package io.github.amatriciaaana.kiora.result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class ResultTest {

    @Test
    void mapsSuccessfulValues() {
        Result<Integer> result = Result.success(20)
                .map(value -> value + 1)
                .flatMap(value -> Result.success(value * 2));

        assertTrue(result.isSuccess());
        assertEquals(42, result.orElseThrow());
    }

    @Test
    void keepsFailuresAcrossMapOperations() {
        IOException cause = new IOException("disk");

        Result<Integer> result = Result.<Integer>failure(cause)
                .map(value -> value + 1)
                .flatMap(value -> Result.success(value * 2));

        assertTrue(result.isFailure());
        assertEquals(cause, result.errorOrNull());
        assertEquals(7, result.orElse(7));
    }

    @Test
    void recoversFromFailure() {
        Result<Integer> result = Result.<Integer>failure(new IOException("disk"))
                .recover(error -> 42);

        assertTrue(result.isSuccess());
        assertEquals(42, result.orElseThrow());
    }

    @Test
    void tryOfCapturesCheckedExceptions() {
        Result<String> result = Try.of(() -> {
            throw new IOException("boom");
        });

        assertTrue(result.isFailure());
        assertInstanceOf(IOException.class, result.errorOrNull());
    }

    @Test
    void tryRunReturnsUnitForVoidWork() {
        Result<Unit> result = Try.run(() -> {
        });

        assertTrue(result.isSuccess());
        assertTrue(result.toOptional().isPresent());
        assertEquals(Unit.INSTANCE, result.orElseThrow());
    }

    @Test
    void failedCheckedExceptionWrapsIntoResultException() {
        Result<String> result = Result.failure(new IOException("boom"));

        ResultException exception = assertThrows(ResultException.class, result::orElseThrow);
        assertInstanceOf(IOException.class, exception.getCause());
    }

    @Test
    void failedRuntimeExceptionIsRethrownDirectly() {
        IllegalStateException cause = new IllegalStateException("boom");
        Result<String> result = Result.failure(cause);

        IllegalStateException exception = assertThrows(IllegalStateException.class, result::orElseThrow);
        assertEquals(cause, exception);
    }

    @Test
    void successfulNullValueStaysOptionalEmpty() {
        Result<String> result = Result.success(null);

        assertTrue(result.isSuccess());
        assertFalse(result.toOptional().isPresent());
    }
}
