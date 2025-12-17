package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;


    public List<Film> getListOfAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmOrThrow(Long filmId) {
        return filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    public Film create(Film film) {
        log.info("Получен запрос на создание фильма");
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не должна быть раньше 28.12.1895");
        }
        return filmStorage.save(film);
    }

    public Film update(Film newFilm) {
        log.info("Получен запрос на обновление данных фильма");
        getFilmOrThrow(newFilm.getId());
        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не должна быть раньше 28.12.1895");
        }
        return newFilm;
    }

    public void addLike(Long filmId, Long userId) {
        if (filmId == null || userId == null) {
            throw new ValidationException("ID фильма или пользователя не передан");
        }
        Film film = getFilmOrThrow(filmId);
        userService.getUserOrThrow(userId);
        if (film.getLikes().contains(userId)) {
            throw new ValidationException("Вы уже поставили лайк фильму " + '\"' + film.getName() + '\"');
        }
        film.getLikes().add(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        if (filmId == null || userId == null) {
            throw new ValidationException("ID фильма и/или пользователя должны быть переданы");
        }
        Film film = getFilmOrThrow(filmId);
        userService.getUserOrThrow(userId);

        film.getLikes().remove(userId);
    }
}