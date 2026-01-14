package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingDAO {
    List<Rating> getAllRatings();
    Optional<Rating> findById(Long ratingId);
}
