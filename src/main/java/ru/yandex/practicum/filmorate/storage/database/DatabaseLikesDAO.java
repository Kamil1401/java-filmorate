package ru.yandex.practicum.filmorate.storage.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.LikesDAO;

import java.util.HashMap;
import java.util.List;

@Repository
public class DatabaseLikesDAO implements LikesDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseLikesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Like> getAllLikes(Long filmId) {
        return jdbcTemplate.query("select * from FILM_LIKES where FILM_ID = ?", (rs, rowNum) ->
                        Like.builder()
                                .id(rs.getLong("id"))
                                .filmId(rs.getLong("film_id"))
                                .userId(rs.getLong("user_id"))
                                .build(), filmId);
    }

    @Override
    public void insertLike(Like like) {
        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("film_id", like.getFilmId());
        hashmap.put("user_id", like.getUserId());
        new SimpleJdbcInsert(jdbcTemplate).usingGeneratedKeyColumns("id")
                .withTableName("FILM_LIKES")
                .executeAndReturnKey(hashmap);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        jdbcTemplate.update("delete from PUBLIC.FILM_LIKES where film_id = ? and user_id = ?", filmId, userId);
    }
}
