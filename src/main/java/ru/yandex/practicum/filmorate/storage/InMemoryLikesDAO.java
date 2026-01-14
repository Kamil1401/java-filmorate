package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.*;

public class InMemoryLikesDAO implements LikesDAO {
    long id = 0;
    List<Like> likes = new ArrayList<>();

    @Override
    public List<Like> getAllLikes(Long filmId) {
        return likes;
    }

    @Override
    public void insertLike(Like build) {
        build.setId(id++);
        likes.add(build);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        likes.removeIf(like -> like.getFilmId().equals(filmId) && like.getUserId().equals(userId));
    }
}
