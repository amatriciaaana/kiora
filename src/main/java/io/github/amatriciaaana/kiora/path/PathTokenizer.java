package io.github.amatriciaaana.kiora.path;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class PathTokenizer {
    private PathTokenizer() {
    }

    static List<PathToken> tokenize(String path) {
        Objects.requireNonNull(path, "path must not be null");
        if (path.isBlank()) {
            throw new PathSyntaxException("Path must not be blank");
        }
        if (path.charAt(0) == '.') {
            throw new PathSyntaxException("Path must not start with '.': " + path);
        }
        if (path.charAt(0) == '[') {
            throw new PathSyntaxException("Path must not start with '[': " + path);
        }
        if (path.endsWith(".")) {
            throw new PathSyntaxException("Path must not end with '.': " + path);
        }
        if (path.contains("..")) {
            throw new PathSyntaxException("Path must not contain empty segments: " + path);
        }

        List<PathToken> tokens = new ArrayList<>();
        StringBuilder currentKey = new StringBuilder();

        for (int i = 0; i < path.length(); i++) {
            char ch = path.charAt(i);

            if (ch == '.') {
                handleDot(path, tokens, currentKey, i);
                continue;
            }

            if (ch == '[') {
                handleIndex(path, tokens, currentKey, i);
                i = path.indexOf(']', i);
                continue;
            }

            if (ch == ']') {
                throw new PathSyntaxException("Unexpected ']' in path: " + path);
            }

            currentKey.append(ch);
        }

        if (currentKey.length() > 0) {
            tokens.add(new KeyToken(currentKey.toString()));
        }

        if (tokens.isEmpty()) {
            throw new PathSyntaxException("Path must contain at least one token: " + path);
        }

        return List.copyOf(tokens);
    }

    private static void handleDot(String path, List<PathToken> tokens, StringBuilder currentKey, int index) {
        if (currentKey.length() > 0) {
            tokens.add(new KeyToken(currentKey.toString()));
            currentKey.setLength(0);
            return;
        }

        char previous = path.charAt(index - 1);
        if (previous == ']') {
            return;
        }

        throw new PathSyntaxException("Path must not contain empty segments: " + path);
    }

    private static void handleIndex(String path, List<PathToken> tokens, StringBuilder currentKey, int start) {
        if (currentKey.length() > 0) {
            tokens.add(new KeyToken(currentKey.toString()));
            currentKey.setLength(0);
        } else {
            char previous = path.charAt(start - 1);
            if (previous != ']') {
                throw new PathSyntaxException("Path must not contain empty segments: " + path);
            }
        }

        int end = path.indexOf(']', start);
        if (end < 0) {
            throw new PathSyntaxException("Unclosed index in path: " + path);
        }

        String indexText = path.substring(start + 1, end);
        if (indexText.isEmpty() || indexText.chars().allMatch(Character::isDigit) == false) {
            throw new PathSyntaxException("List index must be numeric in path: " + path);
        }

        tokens.add(new IndexToken(Integer.parseInt(indexText)));
    }

    sealed interface PathToken permits KeyToken, IndexToken {
    }

    record KeyToken(String key) implements PathToken {
    }

    record IndexToken(int index) implements PathToken {
    }
}
