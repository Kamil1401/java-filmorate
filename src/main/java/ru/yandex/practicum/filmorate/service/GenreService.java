package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenresDAO;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenresDAO genresStorage;


    public List<Genre> getAllGenres() {
        return genresStorage.getAllGenres();
    }

    public Genre getGenreById(Long genreId) {
        return genresStorage.findById(genreId)
                .orElseThrow(() -> new NotFoundException("Жанр не найден"));
    }
}
