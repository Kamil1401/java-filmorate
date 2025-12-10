package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(Long filmId) {
        return films.get(filmId);
    }

    @Override
    public Film create(Film film) {
        log.info("Получен запрос на создание фильма");
        validateFilm(film);
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        film.setId(getNextId());
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film update(Film newFilm) {
        log.info("Получен запрос на обновление данных фильма");

        if (!films.containsKey(newFilm.getId())) {
            log.warn("Фильм с id {} не найден", newFilm.getId());
            throw new NotFoundException("Фильм не найден");
        }
        validateFilm(newFilm);
        films.put(newFilm.getId(), newFilm);

        return newFilm;
    }

    private void validateFilm(Film film) {
        String error = findValidationViolations(film);
        if (error != null) {
            log.error("Ошибка валидации: {}", error);
            throw new ValidationException(error);
        }
    }

    private String findValidationViolations(Film film) {
        if (film == null) {
            return "Объект не может быть null";
        }
        if (film.getName() == null || film.getName().isBlank()) {
            return "Название фильма обязательно";
        }
        if (film.getDescription() == null) {
            return "Не заполнено описание";
        }
        if (film.getDescription().length() > 200) {
            return "Превышено максимальное количество символов в описании";
        }
        if (film.getReleaseDate() == null) {
            return "Не указана дата релиза";
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            return "Дата релиза не должна быть раньше 28.12.1895";
        }
        if (film.getDuration() == 0) {
            return "Не указана продолжительность фильма";
        }
        if (film.getDuration() <= 0) {
            return "Продолжительность фильма должна быть положительной";
        }
        return null;
    }

    private long getNextId() {
        long currentMaxId = films.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);

        return ++currentMaxId;
    }
}