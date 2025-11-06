package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Получен запрос на создание пользователя");
        validateUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Получен запрос на обновление данных пользователя \"{}\"", newUser.getName());

        if (!users.containsKey(newUser.getId())) {
            log.warn("Пользователь с id {} не найден", newUser.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        validateUser(newUser);
        users.put(newUser.getId(), newUser);

        return newUser;
    }

    private void validateUser(User user) {
        String error = findValidationViolations(user);
        if (error != null) {
            log.error("Ошибка валидации: {}", error);
            throw new ValidationException(error);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
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
