package com.siegfred.userserver.controller;

import com.siegfred.userserver.domain.entity.User;
import com.siegfred.userserver.domain.exception.InvalidParameterException;
import com.siegfred.userserver.domain.exception.NoSuchUserException;
import com.siegfred.userserver.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> findUser(@PathVariable("userId") UUID userId) {
        Optional<User> user = userService.findUser(userId);

        if (user.isPresent()) {
            return ResponseEntity.ok().body(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<User> findUsers() {
        return userService.getUsers();
    }


    @PostMapping
    public User createUser(@RequestBody User user) {
        try {
            return userService.createUser(user.getUsername(), user.getPassword());
        } catch (InvalidParameterException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable("userId") UUID userId, @RequestBody User user) {
        user.setUuid(userId);

        try {
            user = userService.updateUser(user);
        } catch (NoSuchUserException e) {
            new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (InvalidParameterException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return user;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteUser(@PathVariable("userId") UUID userId, @RequestParam(value = "quiet", defaultValue = "false") boolean isQuiet) {
        try {
            userService.deleteUser(userId);
        } catch (NoSuchUserException e) {
            if (!isQuiet) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
    }

}
