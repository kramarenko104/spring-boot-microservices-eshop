package com.gmail.kramarenko104.userservice.services;

import com.gmail.kramarenko104.userservice.models.User;
import com.gmail.kramarenko104.userservice.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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
    public User getUser(long id){
        return (userRepo.findById(id).isPresent()?userRepo.findById(id).get():null);
    }

    @Override
    public User getUserByLogin(String login){
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
    public List<User> getAllUsers(){
        return (List) userRepo.findAll();
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
