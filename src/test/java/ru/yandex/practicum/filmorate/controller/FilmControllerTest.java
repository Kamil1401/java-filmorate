package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    private FilmController controller;
    private Film testFilm;

    @BeforeEach
    public void beforeEach() {
        controller = new FilmController();
        testFilm = new Film();
        testFilm.setTitle("Иллюзия обмана");
        testFilm.setDescription("Команда лучших иллюзионистов мира проворачивает дерзкие ограбления " +
                "прямо во время своих шоу, играя в кошки-мышки с агентами ФБР.");
        testFilm.setRelease(LocalDate.of(2013, 6, 12));
        testFilm.setDuration(Duration.ofMinutes(115));
    }

    @Test
    public void getAllFilms_getListOfFilms() {
        controller.create(testFilm);
        List<Film> films = controller.getAllFilms();

        assertEquals(1, films.size());
        assertEquals("Иллюзия обмана", films.getFirst().getTitle());
    }

    @Test
    public void create_addFilmObject_titleIsNotBlank() {
        testFilm.setTitle(" ");
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Film result = controller.create(testFilm);
        });

        assertEquals("Название фильма обязательно", exception.getMessage());
    }

    @Test
    public void create_addFilmObject_descriptionNoMoreThan200Chars() {
        testFilm.setDescription("«Иллю́зия обма́на» (дословно «Теперь ты меня видишь»; англ. Now You See Me)[4] — американский фильм-ограбление 2013 года, шестой полнометражный фильм французского режиссёра Луи Летерье по сценарию Эда Соломона, Боаза Якина и Эдварда Рикурта и по рассказу Якина и Рикурта[5]. Это первая часть серии «Иллюзия обмана». В фильме задействованы Джесси Айзенберг, Марк Руффало, Вуди Харрельсон, Мелани Лоран, Айла Фишер, Common, Дэйв Франко, Майкл Кейн и Морган Фримен. Сюжет повествует об агенте ФБР и детективе Интерпола, которые выслеживают и пытаются привлечь к ответственности команду фокусников, которые совершают ограбления банков во время своих выступлений и вознаграждают зрителей деньгами.\n" +
                "\n" +
                "Премьера состоялась в Нью-Йорке 21 мая 2013 года, а его официальный релиз в США состоялся 31 мая 2013 года на Summit Entertainment[6]. Фильм получил смешанные отзывы, причём критика была сосредоточена на концовке[7], но стал успешным в прокате, собрав 351,7 миллиона долларов по всему миру при бюджете в 75 миллионов долларов[1]. Фильм получил приз зрительских симпатий в номинации «лучший триллер», а также был номинирован на премию журнала Empire за лучший триллер и премию «Сатурн» за лучший фильм в жанре триллер и лучшую музыку.");
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Film result = controller.create(testFilm);
        });

        assertEquals("Превышено максимальное количество символов в описании", exception.getMessage());
    }

    @Test
    public void create_addFilmObject_theReleaseWasAfterTheSetDate() {
        testFilm.setRelease(LocalDate.of(1895, 8, 29));
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Film result = controller.create(testFilm);
        });

        assertEquals("Дата релиза не должна быть раньше 28.12.1895", exception.getMessage());
    }

    @Test
    public void create_addFilmObject_durationIsPositive() {
        testFilm.setDuration(Duration.ofMinutes(-120));
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Film result = controller.create(testFilm);
        });

        assertEquals("Продолжительность фильма должна быть положительной",
                exception.getMessage());
    }
}