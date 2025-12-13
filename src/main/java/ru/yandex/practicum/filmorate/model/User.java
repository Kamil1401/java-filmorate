package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
public class User {
    private Long id;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Email некорректный")
    private String email;

    @NotBlank(message = "Логин обязателен")
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы")
    private String login;

    private String name;

    @NotNull(message = "Дата рождения обязательна")
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    private Set<Long> friends;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        User otherUser = (User) object;
        return Objects.equals(id, otherUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}