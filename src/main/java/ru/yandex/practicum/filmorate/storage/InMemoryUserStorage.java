package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Long userId) {
        return users.get(userId);
    }

    @Override
    public User create(User user) {
        log.info("Получен запрос на создание пользователя");
        validateUser(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User update(User newUser) {
        log.info("Получен запрос на обновление данных пользователя \"{}\"", newUser.getName());

        if (!users.containsKey(newUser.getId())) {
            log.warn("Пользователь с id {} не найден", newUser.getId());
            throw new ValidationException("Пользователь не найден");
        }
        validateUser(newUser);
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }
        users.put(newUser.getId(), newUser);

        return newUser;
    }

    private void validateUser(User user) {
        String error = findValidationViolations(user);
        if (error != null) {
            log.error("Ошибка валидации: {}", error);
            throw new ValidationException(error);
        }
    }

    private String findValidationViolations(User user) {
        if (user == null) {
            return "Объект не может быть null";
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return "Не передана почта";
        }
        if (!user.getEmail().contains("@")) {
            return "Почта должна содержать символ \"@\"";
        }
        if (user.getLogin() == null) {
            return "Не указан логин";
        }
        if (user.getLogin().contains(" ")) {
            return "Логин не может содержать пробелы";
        }
        if (user.getBirthday() == null) {
            return "Не указана дата рождения";
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            return "Дата рождения не может быть указана в будущем";
        }
        return null;
    }

    private long getNextId() {
        long currentMaxId = users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);

        return ++currentMaxId;
    }
}