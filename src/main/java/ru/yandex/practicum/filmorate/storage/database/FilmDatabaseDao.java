package ru.yandex.practicum.filmorate.storage.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmDAO;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilmDatabaseDao implements FilmDAO {
    private final JdbcTemplate jdbcTemplate;
    public static final ResultSetExtractor<List<Film>> LIST_RESULT_SET_EXTRACTOR = (rs) -> {
        Map<Long, Film> map = new HashMap<>();
        while (rs.next()) {
            Long filmId = rs.getLong("id");
            Film film = map.computeIfAbsent(filmId, (k) -> {
                try {
                    return Film.builder()
                            .id(filmId)
                            .name(rs.getString("name"))
                            .description(rs.getString("description"))
                            .releaseDate(rs.getDate("release_date").toLocalDate())
                            .duration(rs.getInt("duration"))
                            .genres(new HashSet<>())
                            .build();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            if (rs.getObject("GENRE_ID") != null) {
                Genre genre = Genre.builder()
                        .id(rs.getLong("GENRE_ID"))
                        .name(rs.getString("GENRE_NAME"))
                        .build();
                film.getGenres().add(genre);
            }
            if (rs.getObject("RATING_ID") != null) {
                film.setMpa(MpaRating.builder()
                        .id(rs.getLong("RATING_ID"))
                        .name(rs.getString("RATING_NAME"))
                        .build());
            }
        }

        return map.values().stream().peek(film -> film.setGenres(
                film.getGenres().stream()
                        .sorted(Comparator.comparing(Genre::getId))
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        )).toList();
    };

    @Autowired
    public FilmDatabaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAllFilms() {
        return jdbcTemplate.query("""
                select F.*,
                       AR.ID as RATING_ID,
                       AR.NAME as RATING_NAME,
                       G2.ID as GENRE_ID,
                       G2.NAME as GENRE_NAME
                from FILMS F
                left join AGE_RATING AR on AR.ID = f.AGE_RATING_ID
                left join FILM_GENRES FG on FG.FILM_ID = f.ID
                left join GENRES G2 on FG.GENRE_ID = G2.ID""", LIST_RESULT_SET_EXTRACTOR);
    }

    @Override
    public Optional<Film> findById(Long filmId) {
        return Objects.requireNonNull(jdbcTemplate.query("""
                select F.*,
                       AR.ID as RATING_ID,
                       AR.NAME as RATING_NAME,
                       G2.ID as GENRE_ID,
                       G2.NAME as GENRE_NAME
                from FILMS F
                left join AGE_RATING AR on AR.ID = f.AGE_RATING_ID
                left join FILM_GENRES FG on FG.FILM_ID = f.ID
                left join GENRES G2 on FG.GENRE_ID = G2.ID
                where F.ID=?
                """, LIST_RESULT_SET_EXTRACTOR, filmId)).stream().findFirst();
    }



    @Override
    public Film save(Film film) {
        SimpleJdbcInsert filmInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("id");
        Long id = filmInsert.executeAndReturnKey(toMap(film)).longValue();
        SimpleJdbcInsert genresInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILM_GENRES")
                .usingGeneratedKeyColumns("id");
        film.getGenres().forEach(genreId -> {
            genresInsert.execute(Map.of(
                    "FILM_ID", id,
                    "GENRE_ID", genreId.getId()
            ));
        });
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Фильм не сохранен"));
    }

    private Map<String, ?> toMap(Film film) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", film.getName());
        params.put("description", film.getDescription());
        params.put("release_date", film.getReleaseDate());
        params.put("duration", film.getDuration());

        if (film.getMpa() != null) {
            params.put("age_rating_id", film.getMpa().getId());
        }
        return params;
    }

    @Override
    public Film update(Film newFilm) {
        jdbcTemplate.update("update FILMS " +
                        "set DESCRIPTION = ?, NAME = ?, DURATION = ?, AGE_RATING_ID = ? ,RELEASE_DATE = ? where ID = ?",
                newFilm.getDescription(),
                newFilm.getName(),
                newFilm.getDuration(),
                newFilm.getMpa(),
                newFilm.getReleaseDate(),
                newFilm.getId()
        );

        return findByIdOrThrow(newFilm.getId());
    }

    private Film findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("Фильм не найден"));
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        List<Long> films = jdbcTemplate
                .queryForList("select FILM_ID " +
                                  "from   FILM_LIKES " +
                                  "group by FILM_ID  " +
                                  "order by count(id) desc " +
                                  "limit ?",
                        Long.class, count);
        return films.stream().map(this::findByIdOrThrow).toList();
    }
}