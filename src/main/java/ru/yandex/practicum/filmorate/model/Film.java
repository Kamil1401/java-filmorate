package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private long id;
    private String title;
    private String description;
    private LocalDate release;
    private Duration duration;
}
