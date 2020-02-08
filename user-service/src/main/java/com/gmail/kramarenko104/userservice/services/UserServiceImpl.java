package com.gmail.kramarenko104.userservice.services;

import com.gmail.kramarenko104.userservice.models.User;
import com.gmail.kramarenko104.userservice.models.UserDTO;
import com.gmail.kramarenko104.userservice.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final static String SALT = "34Ru9k";

    private UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDTO createUser(User user){
        User criptUser = user;
        criptUser.setPassword(hashString(user.getPassword()));
        return userRepo.save(criptUser).getDTO();
    }

    @Override
    public Optional<UserDTO> getUser(long id){
        return userRepo.findById(id)
                .map(u -> Optional.ofNullable(u.getDTO()))
                .orElse(Optional.ofNullable(null));
    }

    @Override
    public Optional<UserDTO> getUserByLogin(String login){
        return userRepo.findByLogin(login)
                .map(u -> Optional.ofNullable(u.getDTO()))
                .orElse(Optional.ofNullable(null));
    }

    @Override
    public UserDTO updateUser(User newUser) {
        return userRepo.updateUser(newUser.getLogin(),
                newUser.getPassword(), newUser.getName(),
                newUser.getAddress(), newUser.getComment(),
                newUser.getUser_id())
                    .getDTO();
    }

    @Override
    public void deleteUser(long id){
        userRepo.deleteById(id);
    }

    @Override
    public Optional<List<UserDTO>> getAllUsers(){
        List<User> users = (List) userRepo.findAll();
        List<UserDTO> usersDTO = null;
        if (users != null) {
            usersDTO = new ArrayList<>();
            for (User user : users) {
                usersDTO.add(user.getDTO());
            }
        }
        return Optional.ofNullable(usersDTO);
    }

    private String hashString(String hash) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(StandardCharsets.UTF_8.encode(hash + SALT));
        return String.format("%032x", new BigInteger(md5.digest()));
    }

}
