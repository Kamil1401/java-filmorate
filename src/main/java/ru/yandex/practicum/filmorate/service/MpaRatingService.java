package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingDAO;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaRatingService {
    private final MpaRatingDAO mpaRatingStorage;


    public List<MpaRating> getAllRatings() {
        return mpaRatingStorage.getAllRatings();
    }

    public MpaRating getRatingById(Long ratingId) {
        return mpaRatingStorage.findById(ratingId)
                .orElseThrow(() -> new NotFoundException("Рейтинг не найден"));
    }
}