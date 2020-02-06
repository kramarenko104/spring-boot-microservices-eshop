package com.gmail.kramarenko104.userservice.services;

import com.gmail.kramarenko104.userservice.models.User;
import com.gmail.kramarenko104.userservice.repositories.UserRepo;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    public User createUser(User user){
        User criptUser = user;
        criptUser.setPassword(hashString(user.getPassword()));
        return userRepo.save(criptUser);
    }

    @Override
    public Optional<User> getUser(long id){
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> getUserByLogin(String login){
        return userRepo.findByLogin(login);
    }

    @Override
    public User updateUser(User newUser) {
        return userRepo.updateUser(newUser.getLogin(),
                newUser.getPassword(), newUser.getName(),
                newUser.getAddress(), newUser.getComment(), newUser.getUser_id());
    }

    @Override
    public void deleteUser(long id){
        userRepo.deleteById(id);
    }

    @Override
    public Optional<List<User>> getAllUsers(){
        return Optional.of((List) userRepo.findAll());
    }

    @Override
    public Optional<String> getAllUsersJSON(){
        Iterable<User> users =  userRepo.findAll();
        JSONArray usersArr = new JSONArray();
        if (users != null) {
            for (User user : users) {
                usersArr.put(user.toJSON());
            }
        }
        return Optional.ofNullable(usersArr.isEmpty() ? null : usersArr.toString());
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
