package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getListOfAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") @Positive Long userId) {
        return userService.getUserOrThrow(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getListOfMutualFriends(@PathVariable("id") @Positive Long userId,
                                             @PathVariable @Positive Long otherId) {
        return userService.getMutualFriends(userId, otherId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") @Positive Long userId) {
        if (userId == null) {
            throw new ValidationException("ID пользователя не передан");
        }
        return userService.getUserFriends(userId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") @Positive Long userId, @PathVariable @Positive Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") @Positive Long userId, @PathVariable @Positive Long friendId) {
        userService.deleteFriend(userId, friendId);
    }
}