package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreDTO;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.FilmDAO;
import ru.yandex.practicum.filmorate.storage.LikesDAO;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    @Qualifier("databaseFilmDao")
    private final FilmDAO filmStorage;
    private final UserService userService;
    @Qualifier("databaseLikesDAO")
    private final LikesDAO likesDAO;
    private final RatingService ratingService;
    private final GenreService genreService;


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
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не должна быть раньше 28.12.1895");
        }
        if (film.getGenres() == null) {
            film.setGenres(new HashSet<>());
        } else {
            film.setGenres(new HashSet<>(film.getGenres()));
            film.getGenres().stream()
                    .map(GenreDTO::getId)
                    .forEach(genreService::getGenreById);
        }
        if (film.getMpa() != null) {
            ratingService.getRatingById(film.getMpa().getId());
        }

        return filmStorage.save(film);
    }

    public Film update(Film newFilm) {
        log.info("Получен запрос на обновление данных фильма");
        getFilmOrThrow(newFilm.getId());
        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не должна быть раньше 28.12.1895");
        }
        if (newFilm.getGenres() == null) {
            newFilm.setGenres(Set.of());
        }
        return newFilm;
    }

    public void addLike(Long filmId, Long userId) {
        if (filmId == null || userId == null) {
            throw new ValidationException("ID фильма или пользователя не передан");
        }
        Film film = getFilmOrThrow(filmId);
        userService.getUserOrThrow(userId);
        Set<Long> likedUsers = likesDAO.getAllLikes(filmId)
                .stream().map(Like::getUserId)
                .collect(Collectors.toSet());
        if (likedUsers.contains(userId)) {
            throw new ValidationException("Вы уже поставили лайк фильму " + '\"' + film.getName() + '\"');
        }
        likesDAO.insertLike(Like.builder().filmId(filmId).userId(userId).build());
    }

    public void deleteLike(Long filmId, Long userId) {
        if (filmId == null || userId == null) {
            throw new ValidationException("ID фильма и/или пользователя должны быть переданы");
        }
        Film film = getFilmOrThrow(filmId);
        userService.getUserOrThrow(userId);

        likesDAO.deleteLike(filmId, userId);
    }
}