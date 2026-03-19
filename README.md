# pathmap

`pathmap` is a small Java library for safe access to nested `Map<String, Object>` and `List<?>` structures with a simple path syntax.

It targets the annoying gap between:

- raw `Map` traversal with repetitive casts and null checks
- full JSON binding when the payload is dynamic and not worth modeling yet

## Why This Library

This library is aimed at a broad Java audience:

- backend services handling dynamic API payloads
- configuration-heavy applications
- prototypes that are not ready for full DTO modeling
- migration code that still works with `Map<String, Object>`

The design goal is simple: keep the API tiny, dependency-free, and useful in any Java 17+ codebase.

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
