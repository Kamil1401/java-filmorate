package ru.yandex.practicum.filmorate.storage.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingDAO;

import java.util.List;
import java.util.Optional;

@Repository
public class DatabaseRatingsDAO implements RatingDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseRatingsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Rating> getAllRatings() {
        return jdbcTemplate.query("select * from AGE_RATING", (e, i) -> Rating.builder()
                .id(e.getLong("id"))
                .name(e.getString("name"))
                .build());
    }

    @Override
    public Optional<Rating> findById(Long ratingId) {
        return jdbcTemplate.query("select * from AGE_RATING where ID=?", (e, i) -> {
            return Rating.builder()
                    .id(e.getLong("id"))
                    .name(e.getString("name"))
                    .build();
        }, ratingId).stream().findFirst();
    }
}
