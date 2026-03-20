package dev.kiora.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class TextsTest {

    @Test
    void identifiesBlankAndEmptyValues() {
        assertTrue(Texts.isBlank(null));
        assertTrue(Texts.isBlank("  "));
        assertFalse(Texts.isBlank("aki"));

        assertTrue(Texts.isEmpty(null));
        assertTrue(Texts.isEmpty(""));
        assertFalse(Texts.isEmpty(" "));
    }

    @Test
    void nullIfBlankDropsOnlyBlankValues() {
        assertNull(Texts.nullIfBlank(null));
        assertNull(Texts.nullIfBlank("\n\t"));
        assertEquals("aki", Texts.nullIfBlank("aki"));
    }

    @Test
    void emptyToNullDropsOnlyEmptyValues() {
        assertNull(Texts.emptyToNull(null));
        assertNull(Texts.emptyToNull(""));
        assertEquals(" ", Texts.emptyToNull(" "));
    }

    @Test
    void blankToDefaultUsesFallbackOnlyForBlankInput() {
        assertEquals("guest", Texts.blankToDefault(null, "guest"));
        assertEquals("guest", Texts.blankToDefault("   ", "guest"));
        assertEquals("aki", Texts.blankToDefault("aki", "guest"));
    }

    @Test
    void linesSplitsTextSafely() {
        assertEquals(List.of(), Texts.lines(null));
        assertEquals(List.of(), Texts.lines(""));
        assertEquals(List.of("a", "b", "c"), Texts.lines("a\nb\nc"));
    }
}
