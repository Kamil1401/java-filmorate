package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.constraints.Positive;

import java.util.List;

public interface FriendshipDAO {

    void insertFriendship(Long id, Long id1);

    Boolean deleteFriendship(Long id, Long friendId);

    List<Long> getAllFriends(@Positive Long userId);

}
