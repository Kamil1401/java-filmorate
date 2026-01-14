package ru.yandex.practicum.filmorate.storage;

import java.util.*;

public class InMemoryFriendshipDAO implements FriendshipDAO {

    HashMap<Long, Set<Long>> map = new HashMap<>();

    @Override
    public void insertFriendShip(Long id, Long id1) {
        Set<Long> longs = map.get(id);
        if (longs == null) {
            longs = new HashSet<>();
            map.put(id, longs);

        }
        longs.add(id1);
    }

    @Override
    public Boolean deleteFriendShip(Long id, Long friendId) {
        Set<Long> longs = map.get(id);

        if (longs == null) {
            return false;
        }
        return longs.remove(friendId);
    }

    @Override
    public List<Long> getAllFriends(Long userId) {
        Set<Long> longs = map.get(userId);

        if (longs == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(longs);
    }
}
