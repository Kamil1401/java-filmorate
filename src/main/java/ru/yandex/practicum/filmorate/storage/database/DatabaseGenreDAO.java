package ru.yandex.practicum.filmorate.storage.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenresDAO;

import java.util.List;
import java.util.Optional;

@Repository
public class DatabaseGenreDAO implements GenresDAO {
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public DatabaseGenreDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("select * from genres",(e,i) -> Genre.builder()
                .id(e.getLong("id"))
                .name(e.getString("name"))
                .build());
    }

    @Override
    public Optional<Genre> findById(Long genreId) {
        return jdbcTemplate.query("select * from genres where ID=?",(e,i) -> Genre.builder()
                    .id(e.getLong("id"))
                    .name(e.getString("name"))
                    .build(),genreId).stream().findFirst();
    }
}