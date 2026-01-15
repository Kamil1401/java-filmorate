DROP TABLE IF EXISTS film_genres;
DROP TABLE IF EXISTS film_likes;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS friendship;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS age_rating;


create table users
(
    id         bigint primary key auto_increment,
    email      varchar(255) not null,
    login      varchar(255) not null,
    name       varchar(255) not null,
    birth_date date         not null
);


create table friendship
(
    id        bigint primary key auto_increment,
    user_id   bigint references users (id) not null,
    friend_id bigint references users (id) not null
);


create table genres
(
    id   bigint primary key,
    name varchar(255)
);


create table age_rating
(
    id   bigint primary key,
    name varchar(255)
);


create table films
(
    id            bigint primary key auto_increment,
    description   varchar(200)                      not null,
    name          varchar(200)                      not null,
    release_date  date                              not null,
    duration      int                               not null,
    age_rating_id bigint references age_rating (id),
    type          varchar(100)
);


create table film_likes
(
    id      bigint primary key auto_increment,
    user_id bigint references users (id) not null,
    film_id bigint references films (id) not null
);


create table film_genres
(
    id       bigint primary key auto_increment,
    film_id  bigint references films (id)  not null,
    genre_id bigint references genres (id) not null
);