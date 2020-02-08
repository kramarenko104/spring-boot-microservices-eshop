package com.gmail.kramarenko104.userservice.services;

import com.gmail.kramarenko104.userservice.models.User;
import com.gmail.kramarenko104.userservice.models.UserDTO;
import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDTO createUser(User user);

    Optional<UserDTO> getUser(long id);

    Optional<UserDTO> getUserByLogin(String login);

    UserDTO updateUser(User user);

    void deleteUser(long id);

    Optional<List<UserDTO>> getAllUsers();

}
