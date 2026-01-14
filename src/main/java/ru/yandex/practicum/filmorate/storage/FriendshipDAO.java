package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.constraints.Positive;

import java.util.List;

public interface FriendshipDAO {
    void insertFriendShip(Long id, Long id1);
    Boolean deleteFriendShip(Long id, Long friendId);
    List<Long> getAllFriends(@Positive Long userId);

}
