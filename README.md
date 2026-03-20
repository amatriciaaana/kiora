# kiora

`kiora` is a Java utility library aimed at reducing repetitive code in everyday applications.

The first feature in the library is path-based access for nested `Map<String, Object>` and `List<?>` structures. It targets the gap between:

- raw `Map` traversal with repetitive casts and null checks
- full JSON binding when the payload is dynamic and not worth modeling yet

## Current Scope

The current package is `io.github.amatriciaaana.kiora.path`.

It provides:

- `DeepMap` for safe nested lookup
- `PathSyntaxException` for invalid path input

The longer-term direction is to keep `kiora` broad enough to host additional small utilities beyond path access.

## Requirements

- Development and CI: JDK 25 LTS
- Consumer runtime: Java 17+

That setup keeps the project aligned with the latest LTS while remaining easy to adopt in existing Java 17 environments.

## Path Syntax

- `user.profile.name`
- `items[0].price`
- `settings.flags.beta`

Current constraints:

- `.` separates map keys
- `[n]` accesses list elements
- map keys themselves must not contain `.` or `[]`

## Example

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

## API Surface

- `DeepMap.of(Map<String, ?> source)`
- `Optional<Object> get(String path)`
- `Optional<T> get(String path, Class<T> type)`
- `Optional<String> getString(String path)`
- `Optional<Integer> getInt(String path)`
- `Optional<Boolean> getBoolean(String path)`
- `Optional<DeepMap> getMap(String path)`
- `boolean hasPath(String path)`
- `Object require(String path)`

## Build

If you want to build with the bundled local JDK:

```bash
export JAVA_HOME="$(pwd)/.tools/jdk-25"
export PATH="$JAVA_HOME/bin:$PATH"
```

Then use your preferred build tool in CI or local development.
