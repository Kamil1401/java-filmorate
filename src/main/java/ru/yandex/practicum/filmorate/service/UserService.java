package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;


    public List<User> getListOfAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserOrThrow(Long userId) {
        return userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public User create(User user) {
        log.info("Получен запрос на создание пользователя");
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        return userStorage.save(user);
    }

    public User update(User newUser) {
        log.info("Получен запрос на обновление данных пользователя");
        getUserOrThrow(newUser.getId());
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }

        return newUser;
    }

    public void addFriend(Long userId, Long friendId) {
        if (userId == null || friendId == null) {
            throw new ValidationException("ID не передан");
        }
        if (userId.equals(friendId)) {
            throw new ValidationException("Нельзя добавить в друзья самого себя");
        }
        User user1 = getUserOrThrow(userId);
        User user2 = getUserOrThrow(friendId);

        if (user1.getFriends().contains(friendId) || user2.getFriends().contains(userId)) {
            throw new FriendAlreadyExistsException("Пользователи уже являются друзьями");
        }
        user1.getFriends().add(user2.getId());
        user2.getFriends().add(user1.getId());
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (userId == null || friendId == null) {
            throw new ValidationException("ID не передан");
        }
        if (userId.equals(friendId)) {
            throw new ValidationException("Нельзя удалить самого себя у себя из друзей");
        }
        User user1 = getUserOrThrow(userId);
        User user2 = getUserOrThrow(friendId);

        user1.getFriends().remove(user2.getId());
        user2.getFriends().remove(user1.getId());
    }

    public List<User> getMutualFriends(Long userId, Long anotherUserId) {
        if (userId == null || anotherUserId == null) {
            throw new ValidationException("ID не передан");
        }
        if (userId.equals(anotherUserId)) {
            throw new ValidationException("Нельзя найти общих друзей с самим собой");
        }
        User user1 = getUserOrThrow(userId);
        User user2 = getUserOrThrow(anotherUserId);

        Set<Long> mutual = new HashSet<>(user1.getFriends());
        mutual.retainAll(user2.getFriends());

        return mutual.stream()
                .map(this::getUserOrThrow)
                .collect(Collectors.toList());
    }
}