# kiora

`kiora` is a Java utility library aimed at reducing repetitive code in everyday applications.

The library currently includes four focused packages:

- `io.github.amatriciaaana.kiora.path` for safe access to nested `Map<String, Object>` and `List<?>` structures
- `io.github.amatriciaaana.kiora.result` for lightweight success/failure handling around exception-heavy code
- `io.github.amatriciaaana.kiora.optional` for small `Optional` helpers that remove repeated boilerplate
- `io.github.amatriciaaana.kiora.text` for null-safe text utilities around blank and empty strings

The longer-term direction is to keep `kiora` broad enough to host additional small utilities without turning it into a framework.

## Requirements

- Development and CI: JDK 25 LTS
- Consumer runtime: Java 17+

That setup keeps the project aligned with the latest LTS while remaining easy to adopt in existing Java 17 environments.

## Path Example

```java
import io.github.amatriciaaana.kiora.path.DeepMap;
import java.util.List;
import java.util.Map;

Map<String, Object> payload = Map.of(
        "user", Map.of(
                "profile", Map.of("name", "Aki")
        ),
        "items", List.of(
                Map.of("price", 1200)
        )
);

DeepMap deepMap = DeepMap.of(payload);

String name = deepMap.getString("user.profile.name").orElse("unknown");
int price = deepMap.getInt("items[0].price").orElse(0);
boolean hasCoupon = deepMap.hasPath("items[0].coupon");
```

## Result Example

```java
import io.github.amatriciaaana.kiora.result.Result;
import io.github.amatriciaaana.kiora.result.Try;

Result<Integer> parsed = Try.of(() -> Integer.parseInt("42"))
        .map(value -> value + 1);

int value = parsed.orElse(0);
```

## Optional Example

```java
import io.github.amatriciaaana.kiora.optional.MoreOptional;
import io.github.amatriciaaana.kiora.result.Result;
import java.util.NoSuchElementException;
import java.util.Optional;

Optional<String> nickname = MoreOptional.or(Optional.empty(), () -> "guest");
Optional<String> visible = MoreOptional.filterNot(nickname, String::isBlank);
Result<String> required = MoreOptional.toResult(visible, () -> new NoSuchElementException("nickname"));
```

## Text Example

```java
import io.github.amatriciaaana.kiora.text.Texts;

String nickname = Texts.blankToDefault("  ", "guest");
String normalized = Texts.nullIfBlank("\n");
boolean blank = Texts.isBlank(nickname);
```

## Text API Surface

- `Texts.isBlank(value)`
- `Texts.isEmpty(value)`
- `Texts.nullIfBlank(value)`
- `Texts.emptyToNull(value)`
- `Texts.blankToDefault(value, fallback)`
- `Texts.lines(value)`

## Build

If you want to build with the bundled local JDK:

```bash
export JAVA_HOME="$(pwd)/.tools/jdk-25"
export PATH="$JAVA_HOME/bin:$PATH"
```

Then use your preferred build tool in CI or local development.
