package com.gmail.kramarenko104.userservice.services;

import com.gmail.kramarenko104.userservice.models.User;
import java.util.List;

public interface UserService {

    User createUser(User user);

    User getUser(long id);

    User getUserByLogin(String login);

    User updateUser(User user);

    void deleteUser(long id);

    List<User> getAllUsers();

}
