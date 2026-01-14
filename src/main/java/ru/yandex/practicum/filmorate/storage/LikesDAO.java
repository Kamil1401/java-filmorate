package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

public interface LikesDAO {

    List<Like> getAllLikes(Long filmId);

    void insertLike(Like build);

    void deleteLike(Long filmId, Long userId);
}
