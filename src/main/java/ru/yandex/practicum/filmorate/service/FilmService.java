package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long filmId, Long userId) {
        if (filmId == null || userId == null) {
            throw new ValidationException("ID фильма и пользователя должны быть переданы");
        }
        Film film = filmStorage.getFilm(filmId);

        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (userStorage.getUser(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (film.getLikes().contains(userId)) {
            throw new ValidationException("Вы уже поставили лайк фильму " + '\"' + film.getName() + '\"');
        }
        film.getLikes().add(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        if (filmId == null || userId == null) {
            throw new ValidationException("ID фильма и пользователя должны быть переданы");
        }
        Film film = filmStorage.getFilm(filmId);

        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }
        if (userStorage.getUser(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        film.getLikes().remove(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing(this::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public int getLikesCount(Film film) {
        if (film == null) {
            throw new ValidationException("Фильм не передан");
        }
        return film.getLikes() == null ? 0 : film.getLikes().size();
    }
}