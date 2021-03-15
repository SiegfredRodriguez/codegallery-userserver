package com.siegfred.userserver.domain.service;

import com.siegfred.userserver.domain.entity.User;
import com.siegfred.userserver.domain.exception.InvalidParameterException;
import com.siegfred.userserver.domain.exception.NoSuchUserException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private TreeMap<UUID, User> users = new TreeMap<>();

    public User createUser(String username, String password) {

        if (Objects.isNull(username) || Objects.isNull(password)) {
            throw new InvalidParameterException();
        }

        User user = new User();

        user.setUuid(UUID.randomUUID());
        user.setUsername(username);

        // Don't do this in real world, never store plain text password
        user.setPassword(password);

        users.put(user.getUuid(), user);

        return user;
    }

    public User updateUser(User user) {

        if (!Objects.isNull(user)) {
            findUser(user.getUuid()).orElseThrow(NoSuchUserException::new);
        } else {
            throw new InvalidParameterException();
        }

        users.put(user.getUuid(), user);

        return findUser(user.getUuid()).get();
    }

    public void deleteUser(UUID userId) {

        User user = findUser(userId).orElseThrow(NoSuchUserException::new);

        users.remove(user.getUuid());

    }

    public Optional<User> findUser(UUID uuid) {

        if (Objects.isNull(uuid)) {
            return Optional.ofNullable(null);
        }

        return Optional.ofNullable(users.get(uuid));
    }

    public List<User> getUsers() {
        return users.values().stream().collect(Collectors.toList());
    }

}
