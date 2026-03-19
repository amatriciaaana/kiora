package dev.codex.pathmap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class DeepMapTest {

    @Test
    void readsNestedValuesWithDotAndIndexSyntax() {
        DeepMap deepMap = DeepMap.of(samplePayload());

        assertEquals("Aki", deepMap.getString("user.profile.name").orElseThrow());
        assertEquals(1200, deepMap.getInt("items[0].price").orElseThrow());
        assertTrue(deepMap.getBoolean("settings.flags.beta").orElseThrow());
    }

    @Test
    void returnsNestedMapAsDeepMap() {
        DeepMap deepMap = DeepMap.of(samplePayload());

        DeepMap profile = deepMap.getMap("user.profile").orElseThrow();

        assertEquals("Aki", profile.getString("name").orElseThrow());
    }

    @Test
    void reportsMissingPathsWithoutThrowing() {
        DeepMap deepMap = DeepMap.of(samplePayload());

        assertFalse(deepMap.hasPath("items[1].price"));
        assertTrue(deepMap.get("items[1].price").isEmpty());
        assertTrue(deepMap.getString("user.profile.nickname").isEmpty());
    }

    @Test
    void throwsOnRequireWhenPathIsMissing() {
        DeepMap deepMap = DeepMap.of(samplePayload());

        assertThrows(NoSuchElementException.class, () -> deepMap.require("user.profile.nickname"));
    }

    @Test
    void rejectsInvalidPathSyntax() {
        DeepMap deepMap = DeepMap.of(samplePayload());

        PathSyntaxException brackets = assertThrows(PathSyntaxException.class, () -> deepMap.get("items[].price"));
        PathSyntaxException leadingDot = assertThrows(PathSyntaxException.class, () -> deepMap.get(".user.profile"));
        PathSyntaxException duplicateDot = assertThrows(PathSyntaxException.class, () -> deepMap.get("user..profile"));

        assertInstanceOf(IllegalArgumentException.class, brackets);
        assertInstanceOf(IllegalArgumentException.class, leadingDot);
        assertInstanceOf(IllegalArgumentException.class, duplicateDot);
    }

    private static Map<String, Object> samplePayload() {
        return Map.of(
                "user", Map.of(
                        "profile", Map.of("name", "Aki")
                ),
                "items", List.of(
                        Map.of("price", 1200)
                ),
                "settings", Map.of(
                        "flags", Map.of("beta", true)
                )
        );
    }
}
