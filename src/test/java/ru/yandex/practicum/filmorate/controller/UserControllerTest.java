package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFriendshipDAO;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {
    private UserController userController;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController(new UserService(new InMemoryUserStorage(),new InMemoryFriendshipDAO()));
    }

    @Test
    public void getAllUsers_getListOfUsers() {
        User user = User.builder()
                .email("captain-kam@yandex.ru")
                .login("captain-Kam")
                .name("Камиль")
                .birthday(LocalDate.of(1993, 1, 14))
                .build();

        userController.create(user);
        List<User> users = userController.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("captain-Kam", users.getFirst().getLogin());
    }

    @Test
    public void create_addUserObject_useLoginWhenNameIsMissing() {
        User user = User.builder()
                .email("captain-kam@yandex.ru")
                .login("captain-Kam")
                .name("Камиль")
                .birthday(LocalDate.of(1993, 1, 14))
                .build();

        user.setName(" ");
        userController.create(user);
        assertEquals(user.getLogin(), user.getName());

        user.setName(null);
        userController.create(user);
        assertEquals(user.getLogin(), user.getName());
    }
}