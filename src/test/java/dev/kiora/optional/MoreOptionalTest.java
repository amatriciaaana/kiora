package dev.kiora.optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.kiora.result.Result;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class MoreOptionalTest {

    @Test
    void orUsesFallbackOnlyWhenEmpty() {
        Optional<String> fallback = MoreOptional.or(Optional.empty(), () -> "guest");
        Optional<String> existing = MoreOptional.or(Optional.of("aki"), () -> "guest");

        assertEquals("guest", fallback.orElseThrow());
        assertEquals("aki", existing.orElseThrow());
    }

    @Test
    void filterNotRemovesMatchingValues() {
        Optional<String> filtered = MoreOptional.filterNot(Optional.of(""), String::isBlank);
        Optional<String> kept = MoreOptional.filterNot(Optional.of("aki"), String::isBlank);

        assertTrue(filtered.isEmpty());
        assertEquals("aki", kept.orElseThrow());
    }

    @Test
    void toResultReturnsSuccessWhenPresent() {
        Result<String> result = MoreOptional.toResult(Optional.of("aki"), () -> new NoSuchElementException("missing"));

        assertTrue(result.isSuccess());
        assertEquals("aki", result.orElseThrow());
    }

    @Test
    void toResultReturnsFailureWhenEmpty() {
        Result<String> result = MoreOptional.toResult(Optional.empty(), () -> new NoSuchElementException("missing"));

        assertTrue(result.isFailure());
        assertInstanceOf(NoSuchElementException.class, result.errorOrNull());
    }

    @Test
    void streamOfNullableMatchesSingleOrEmptyStream() {
        List<String> present = MoreOptional.streamOfNullable("aki").toList();
        List<Object> empty = MoreOptional.streamOfNullable(null).toList();

        assertEquals(List.of("aki"), present);
        assertFalse(empty.iterator().hasNext());
    }
}
