package ru.yandex.practicum.filmorate.storage.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDAO;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class DataBaseUserDAO implements UserDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired

    public DataBaseUserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query("select * from USERS", (rs, rowNum) ->
                User.builder()
                        .id(rs.getLong("id"))
                        .email(rs.getString("email"))
                        .birthday(rs.getDate("BIRTH_DATE").toLocalDate())
                        .login(rs.getString("login"))
                        .name(rs.getString("name"))
                        .build()
        );
    }

    @Override
    public Optional<User> findById(Long userId) {
        return jdbcTemplate.query("select * from USERS where ID=?", (rs, rowNum) ->
                                User.builder()
                                        .id(rs.getLong("id"))
                                        .email(rs.getString("email"))
                                        .birthday(rs.getDate("birth_date").toLocalDate())
                                        .login(rs.getString("login"))
                                        .name(rs.getString("name"))
                                        .build(), userId)
                                        .stream().findFirst();
    }

    @Override
    public User save(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("id");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", user.getEmail());
        hashMap.put("birth_date", Date.valueOf(user.getBirthday()));
        hashMap.put("login", user.getLogin());
        hashMap.put("name", user.getName());
        Number id = simpleJdbcInsert.executeAndReturnKey(hashMap);
        user.setId(id.longValue());
        return user;
    }
}
