package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingDAO;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RatingService {
    private final RatingDAO ratingStorage;


    public List<Rating> getAllRatings() {
        return ratingStorage.getAllRatings();
    }

    public Rating getRatingById(Long ratingId) {
        return ratingStorage.findById(ratingId)
                .orElseThrow(() -> new NotFoundException("Рейтинг не найден"));
    }
}