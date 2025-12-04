package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController(new InMemoryFilmStorage(),
                new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
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
    public void create_addFilmObject_titleIsNotBlank() {
        Film testFilm = Film.builder()
                .name("Иллюзия обмана")
                .description("Команда лучших иллюзионистов мира проворачивает дерзкие ограбления " +
                        "прямо во время своих шоу, играя в кошки-мышки с агентами ФБР.")
                .releaseDate(LocalDate.of(2013, 6, 12))
                .duration(115)
                .build();

        testFilm.setName(" ");
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(testFilm));

        assertEquals("Название фильма обязательно", exception.getMessage());
    }

    @Test
    public void create_addFilmObject_descriptionNoMoreThan200Chars() {
        Film testFilm = Film.builder()
                .name("Иллюзия обмана")
                .description("Команда лучших иллюзионистов мира проворачивает дерзкие ограбления " +
                        "прямо во время своих шоу, играя в кошки-мышки с агентами ФБР.")
                .releaseDate(LocalDate.of(2013, 6, 12))
                .duration(115)
                .build();

        testFilm.setDescription("«Иллю́зия обма́на» (дословно «Теперь ты меня видишь»; англ. Now You See Me)" +
                " — американский фильм-ограбление 2013 года, шестой полнометражный фильм французского режиссёра " +
                "Луи Летерье по сценарию Эда Соломона, Боаза Якина и Эдварда Рикурта и по рассказу Якина и Рикурта. " +
                "Это первая часть серии «Иллюзия обмана». В фильме задействованы Джесси Айзенберг, Марк Руффало, " +
                "Вуди Харрельсон, Мелани Лоран, Айла Фишер, Common, Дэйв Франко, Майкл Кейн и Морган Фримен. " +
                "Сюжет повествует об агенте ФБР и детективе Интерпола, которые выслеживают и пытаются привлечь " +
                "к ответственности команду фокусников, которые совершают ограбления банков во время своих выступлений " +
                "и вознаграждают зрителей деньгами.");
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(testFilm));

        assertEquals("Превышено максимальное количество символов в описании", exception.getMessage());
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

    @Test
    public void create_addFilmObject_durationIsPositive() {
        Film testFilm = Film.builder()
                .name("Иллюзия обмана")
                .description("Команда лучших иллюзионистов мира проворачивает дерзкие ограбления " +
                        "прямо во время своих шоу, играя в кошки-мышки с агентами ФБР.")
                .releaseDate(LocalDate.of(2013, 6, 12))
                .duration(115)
                .build();

        testFilm.setDuration(-120);
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(testFilm));

        assertEquals("Продолжительность фильма должна быть положительной",
                exception.getMessage());
    }
}