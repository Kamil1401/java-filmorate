package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    private UserController userController;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
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
    public void create_addUserObject_ifThereIsASymbol_At() {
        User user = User.builder()
                .email("captain-kam@yandex.ru")
                .login("captain-Kam")
                .name("Камиль")
                .birthday(LocalDate.of(1993, 1, 14))
                .build();

        user.setEmail("captain-kam.yandex.ru");
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));

        assertEquals("Почта должна содержать символ \"@\"", exception.getMessage());
    }

    @Test
    public void create_addUserObject_noSpacesInTheLogin() {
        User user = User.builder()
                .email("captain-kam@yandex.ru")
                .login("captain-Kam")
                .name("Камиль")
                .birthday(LocalDate.of(1993, 1, 14))
                .build();

        user.setLogin("captain Kam");
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));

        assertEquals("Логин не может содержать пробелы", exception.getMessage());
    }

    @Test
    public void create_addUserObject_birthdayInTheFuture() {
        User user = User.builder()
                .email("captain-kam@yandex.ru")
                .login("captain-Kam")
                .name("Камиль")
                .birthday(LocalDate.of(1993, 1, 14))
                .build();

        user.setBirthday(LocalDate.of(2030, 1, 14));
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));

        assertEquals("Дата рождения не может быть указана в будущем", exception.getMessage());
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