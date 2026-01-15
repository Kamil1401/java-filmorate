package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDAO {

    List<Film> getAllFilms();

    Optional<Film> findById(Long filmId);

    Film save(Film film);

    Film update(Film newFilm);

    List<Film> getPopularFilms(int count);

}