package dev.kiora.unchecked;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class UncheckedTest {

    @Test
    void wrapsCheckedExceptionsFromGet() {
        UncheckedException exception = assertThrows(UncheckedException.class,
                () -> Unchecked.get(() -> {
                    throw new IOException("boom");
                }));

        assertInstanceOf(IOException.class, exception.getCause());
    }

    @Test
    void rethrowsRuntimeExceptionsFromGet() {
        IllegalStateException cause = new IllegalStateException("boom");

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> Unchecked.get(() -> {
                    throw cause;
                }));

        assertSame(cause, exception);
    }

    @Test
    void adaptsCheckedFunctionsForStreams() {
        List<Integer> values = Stream.of("1", "2", "3")
                .map(Unchecked.function(Integer::parseInt))
                .toList();

        assertEquals(List.of(1, 2, 3), values);
    }

    @Test
    void adaptsCheckedConsumers() {
        List<String> values = new ArrayList<>();

        Stream.of("a", "b").forEach(Unchecked.consumer(value -> values.add(value.toUpperCase())));

        assertEquals(List.of("A", "B"), values);
    }

    @Test
    void adaptsCheckedPredicates() {
        List<String> values = Stream.of("one", "three", "four")
                .filter(Unchecked.predicate(value -> value.length() > 3))
                .toList();

        assertEquals(List.of("three", "four"), values);
    }

    @Test
    void runsCheckedRunnable() {
        List<String> values = new ArrayList<>();

        Unchecked.run(() -> values.add("done"));

        assertEquals(List.of("done"), values);
    }
}
