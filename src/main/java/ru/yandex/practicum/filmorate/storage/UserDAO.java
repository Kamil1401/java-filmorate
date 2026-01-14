package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {

    List<User> getAllUsers();

    Optional<User> findById(Long userId);

    User save(User user);

}