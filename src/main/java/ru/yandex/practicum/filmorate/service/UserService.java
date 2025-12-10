package ru.yandex.practicum.filmorate.service;

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
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public void addFriend(Long userId, Long friendId) {
        if (userId == null || friendId == null) {
            throw new ValidationException("ID не передан");
        }
        if (userId.equals(friendId)) {
            throw new ValidationException("Нельзя добавить в друзья самого себя");
        }
        User user1 = userStorage.getUser(userId);
        User user2 = userStorage.getUser(friendId);

        if (user1 == null || user2 == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (user1.getFriends().contains(user2.getId()) || user2.getFriends().contains(user1.getId())) {
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
        User user1 = userStorage.getUser(userId);
        User user2 = userStorage.getUser(friendId);

        if (user1 == null || user2 == null) {
            throw new NotFoundException("Пользователь не найден");
        }
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
        User user1 = userStorage.getUser(userId);
        User user2 = userStorage.getUser(anotherUserId);

        if (user1 == null || user2 == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        Set<Long> mutual = new HashSet<>(user1.getFriends());
        mutual.retainAll(user2.getFriends());

        return mutual.stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
}