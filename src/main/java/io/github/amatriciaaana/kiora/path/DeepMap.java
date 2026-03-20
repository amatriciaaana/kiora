package io.github.amatriciaaana.kiora.path;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

/**
 * Safe path-based access to nested {@code Map<String, ?>} and {@code List<?>} structures.
 */
public final class DeepMap {

    private final Map<String, ?> source;

    private DeepMap(Map<String, ?> source) {
        Objects.requireNonNull(source, "source must not be null");
        this.source = Collections.unmodifiableMap(new LinkedHashMap<>(source));
    }

    public static DeepMap of(Map<String, ?> source) {
        return new DeepMap(source);
    }

    public Optional<Object> get(String path) {
        return resolve(path);
    }

    public <T> Optional<T> get(String path, Class<T> type) {
        Objects.requireNonNull(type, "type must not be null");
        return resolve(path).filter(type::isInstance).map(type::cast);
    }

    public Optional<String> getString(String path) {
        return get(path, String.class);
    }

    public Optional<Integer> getInt(String path) {
        return resolve(path)
                .filter(Number.class::isInstance)
                .map(Number.class::cast)
                .map(Number::intValue);
    }

    public Optional<Boolean> getBoolean(String path) {
        return get(path, Boolean.class);
    }

    public Optional<DeepMap> getMap(String path) {
        return resolve(path)
                .filter(Map.class::isInstance)
                .map(Map.class::cast)
                .map(DeepMap::uncheckedOf);
    }

    public boolean hasPath(String path) {
        return resolve(path).isPresent();
    }

    public Object require(String path) {
        return resolve(path)
                .orElseThrow(() -> new NoSuchElementException("No value present at path: " + path));
    }

    private Optional<Object> resolve(String path) {
        List<PathTokenizer.PathToken> tokens = PathTokenizer.tokenize(path);
        Object current = source;

        for (PathTokenizer.PathToken token : tokens) {
            if (token instanceof PathTokenizer.KeyToken keyToken) {
                if (current instanceof Map<?, ?> map) {
                    if (map.containsKey(keyToken.key()) == false) {
                        return Optional.empty();
                    }
                    current = map.get(keyToken.key());
                    continue;
                }
                return Optional.empty();
            }

            PathTokenizer.IndexToken indexToken = (PathTokenizer.IndexToken) token;
            if (current instanceof List<?> list) {
                if (indexToken.index() < 0 || indexToken.index() >= list.size()) {
                    return Optional.empty();
                }
                current = list.get(indexToken.index());
                continue;
            }
            return Optional.empty();
        }

        return Optional.ofNullable(current);
    }

    @SuppressWarnings("unchecked")
    private static DeepMap uncheckedOf(Map<?, ?> map) {
        return new DeepMap((Map<String, ?>) map);
    }
}
