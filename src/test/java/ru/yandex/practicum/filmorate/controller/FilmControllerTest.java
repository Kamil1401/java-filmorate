package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaRatingService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFriendshipDAO;
import ru.yandex.practicum.filmorate.storage.InMemoryLikesDAO;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.database.GenreDatabaseDao;
import ru.yandex.practicum.filmorate.storage.database.MpaRatingsDatabaseDao;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(),
                new UserService(new InMemoryUserStorage(), new InMemoryFriendshipDAO()), new InMemoryLikesDAO(),
                new MpaRatingService(new MpaRatingsDatabaseDao(new JdbcTemplate())),
                new GenreService(new GenreDatabaseDao(new JdbcTemplate()))));
    }

    @Test
    public void getAllFilms_getListOfFilms() {
        Film testFilm = Film.builder()
                .name("Иллюзия обмана")
                .description("Команда лучших иллюзионистов мира проворачивает дерзкие ограбления " +
                        "прямо во время своих шоу, играя в кошки-мышки с агентами ФБР.")
                .releaseDate(LocalDate.of(2013, 6, 12))
                .duration(115)
                .build();

        filmController.create(testFilm);
        List<Film> films = filmController.getAllFilms();

        assertEquals(1, films.size());
        assertEquals("Иллюзия обмана", films.getFirst().getName());
    }

    @Test
    public void create_addFilmObject_theReleaseWasAfterTheSetDate() {
        Film testFilm = Film.builder()
                .name("Иллюзия обмана")
                .description("Команда лучших иллюзионистов мира проворачивает дерзкие ограбления " +
                        "прямо во время своих шоу, играя в кошки-мышки с агентами ФБР.")
                .releaseDate(LocalDate.of(2013, 6, 12))
                .duration(115)
                .build();

        testFilm.setReleaseDate(LocalDate.of(1895, 8, 29));
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(testFilm));

        assertEquals("Дата релиза не должна быть раньше 28.12.1895", exception.getMessage());
    }
}