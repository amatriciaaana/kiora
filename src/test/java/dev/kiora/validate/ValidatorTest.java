package dev.kiora.validate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.kiora.result.Result;
import java.util.List;
import org.junit.jupiter.api.Test;

class ValidatorTest {

    @Test
    void returnsSuccessWhenAllChecksPass() {
        User user = new User("Aki", 20, "aki@example.com");

        Result<User> result = Validator.of(user)
                .notNull(User::email, "email")
                .notBlank(User::name, "name")
                .rangeInt(User::age, 18, 65, "age")
                .toResult();

        assertTrue(result.isSuccess());
        assertEquals(user, result.orElseThrow());
    }

    @Test
    void collectsMultipleValidationErrors() {
        Result<User> result = Validator.of(new User(" ", 12, null))
                .notBlank(User::name, "name")
                .rangeInt(User::age, 18, 65, "age")
                .notNull(User::email, "email")
                .toResult();

        assertTrue(result.isFailure());

        ValidationException error = assertInstanceOf(ValidationException.class, result.errorOrNull());
        assertEquals(
                List.of(
                        "name must not be blank",
                        "age must be between 18 and 65",
                        "email must not be null"
                ),
                error.errors()
        );
    }

    @Test
    void supportsPredicateChecksOnWholeObject() {
        Result<User> result = Validator.of(new User("Aki", 30, "example.com"))
                .check(user -> user.email().contains("@"), "email must contain @")
                .toResult();

        assertTrue(result.isFailure());
        assertEquals(List.of("email must contain @"), Validator.of(new User("Aki", 30, "example.com"))
                .check(user -> user.email().contains("@"), "email must contain @")
                .errors());
    }

    @Test
    void exposesImmutableErrorsView() {
        Validator<User> validator = Validator.of(new User(null, 20, null))
                .notNull(User::name, "name");

        List<String> errors = validator.errors();

        assertEquals(List.of("name must not be null"), errors);
        assertFalse(errors instanceof java.util.ArrayList);
    }

    private record User(String name, int age, String email) {
    }
}
