package ru.yandex.practicum.filmorate.storage.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingDAO;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaRatingsDatabaseDao implements MpaRatingDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaRatingsDatabaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<MpaRating> getAllRatings() {
        return jdbcTemplate.query("select * from AGE_RATING", (e, i) -> MpaRating.builder()
                .id(e.getLong("id"))
                .name(e.getString("name"))
                .build());
    }

    @Override
    public Optional<MpaRating> findById(Long ratingId) {
        return jdbcTemplate.query("select * from AGE_RATING where ID=?", (e, i) -> MpaRating.builder()
                .id(e.getLong("id"))
                .name(e.getString("name"))
                .build(), ratingId).stream().findFirst();
    }
}
