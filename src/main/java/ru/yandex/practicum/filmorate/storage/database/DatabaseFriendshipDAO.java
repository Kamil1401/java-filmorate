package ru.yandex.practicum.filmorate.storage.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.FriendshipDAO;

import java.util.HashMap;
import java.util.List;

@Repository
public class DatabaseFriendshipDAO implements FriendshipDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseFriendshipDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertFriendShip(Long userId, Long friendId) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("id")
                .withTableName("friendship");
        HashMap<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("friend_id", friendId);
        insert.execute(params);

    }

    @Override
    public Boolean deleteFriendShip(Long userId, Long friendId) {
        return jdbcTemplate.update("delete from  FRIENDSHIP " +
                        " where USER_ID = ? and FRIEND_ID = ?",
                userId, friendId) > 0;
    }

    @Override
    public List<Long> getAllFriends(Long userId) {
        return jdbcTemplate.queryForList(
                "select FRIEND_ID  from FRIENDSHIP where USER_ID = ?",
                Long.class, userId);
    }
}
