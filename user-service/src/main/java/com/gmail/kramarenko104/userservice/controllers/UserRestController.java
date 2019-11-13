package com.gmail.kramarenko104.userservice.controllers;

import com.gmail.kramarenko104.userservice.models.User;
import com.gmail.kramarenko104.userservice.repositories.UserRepo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserRestController {

    private final static String SALT = "34Ru9k";

    UserRepo userRepo;

    public UserRestController(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    @GetMapping()
    public HttpEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>((List)userRepo.findAll(), HttpStatus.OK);
    }

    @PostMapping()
    public HttpEntity<User> createUser(@RequestParam("user") User user){
        User criptUser = user;
        criptUser.setPassword(hashString(user.getPassword()));
        return new ResponseEntity<>(userRepo.save(criptUser), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public HttpEntity<User> getUser(@PathVariable("userId") long userId){
        User user = userRepo.findById(userId).isPresent()? userRepo.findById(userId).get():null;
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(params = {"login"})
    public HttpEntity<User> getUserByLogin(@RequestParam("login") String login){
        return new ResponseEntity<>(userRepo.findByLogin(login), HttpStatus.OK);
    }

    @PutMapping
    public HttpEntity<User> update(@RequestParam("user") User user) {
        User newUser = userRepo.updateUser(user.getLogin(), user.getPassword(), user.getName(),
                user.getAddress(), user.getComment(), user.getUser_id());
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") long userId){
        userRepo.deleteById(userId);
    }

    public String hashString(String hash) {
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
