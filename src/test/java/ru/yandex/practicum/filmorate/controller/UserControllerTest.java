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
    private UserController controller;
    private User user;

    @BeforeEach
    public void beforeEach() {
        controller = new UserController();
        user = new User();
        user.setEmail("captain-kam@yandex.ru");
        user.setLogin("captain-Kam");
        user.setName("Камиль");
        user.setBirthday(LocalDate.of(1993, 1, 14));
    }

    @Test
    public void getAllUsers_getListOfUsers() {
        controller.create(user);
        List<User> users = controller.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("captain-Kam", users.getFirst().getLogin());
    }

    @Test
    public void create_addUserObject_ifThereIsASymbol_At() {
        user.setEmail("captain-kam.yandex.ru");
        ValidationException exception = assertThrows(ValidationException.class, () -> controller.create(user));

        assertEquals("Почта должна содержать символ \"@\"", exception.getMessage());
    }

    @Test
    public void create_addUserObject_noSpacesInTheLogin() {
        user.setLogin("captain Kam");
        ValidationException exception = assertThrows(ValidationException.class, () -> controller.create(user));

        assertEquals("Логин не может содержать пробелы", exception.getMessage());
    }

    @Test
    public void create_addUserObject_birthdayInTheFuture() {
        user.setBirthday(LocalDate.of(2030, 1, 14));
        ValidationException exception = assertThrows(ValidationException.class, () -> controller.create(user));

        assertEquals("Дата рождения не может быть указана в будущем", exception.getMessage());
    }

    @Test
    public void create_addUserObject_useLoginWhenNameIsMissing() {
        user.setName(" ");
        controller.create(user);
        assertEquals(user.getLogin(), user.getName());

        user.setName(null);
        controller.create(user);
        assertEquals(user.getLogin(), user.getName());
    }
}