package com.gmail.kramarenko104.userservice.services;

import com.gmail.kramarenko104.userservice.models.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(User user);

    Optional<User> getUser(long id);

    Optional<User> getUserByLogin(String login);

    User updateUser(User user);

    void deleteUser(long id);

    Optional<List<User>> getAllUsers();

    Optional<String> getAllUsersJSON();

}
