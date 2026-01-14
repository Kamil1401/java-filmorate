package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmDAO {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> findById(Long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public Film save(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film update(Film newFilm) {
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return films.values().stream()
//                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    private long getNextId() {
        long currentMaxId = films.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);

        return ++currentMaxId;
    }
}