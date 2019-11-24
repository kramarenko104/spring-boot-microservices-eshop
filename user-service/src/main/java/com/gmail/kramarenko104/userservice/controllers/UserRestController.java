package com.gmail.kramarenko104.userservice.controllers;

import com.gmail.kramarenko104.userservice.models.User;
import com.gmail.kramarenko104.userservice.repositories.UserRepo;
import com.sun.org.apache.xerces.internal.xs.ShortList;
import org.hibernate.mapping.Collection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CollectionCertStoreParameters;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserRestController {

    private final static String SALT = "34Ru9k";

    UserRepo userRepo;

    public UserRestController(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    @GetMapping()
    public List<String> getAllUsers(){
        return (List<String>)((List)userRepo.findAll())
                .stream()
                .map(user -> user.toString())
                .collect(Collectors.toList());
    }

    @PostMapping()
    public HttpEntity<String> createUser(@RequestParam("user") User user){
        User criptUser = user;
        criptUser.setPassword(hashString(user.getPassword()));
        return new ResponseEntity<>(userRepo.save(criptUser).toString(), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public HttpEntity<String> getUser(@PathVariable("userId") long userId){
        User user = userRepo.findById(userId).isPresent()? userRepo.findById(userId).get():new User();
        return new ResponseEntity<>(user.toString(), HttpStatus.OK);
    }

    @GetMapping("/api/{userId}")
    public User getUserAPI(@PathVariable("userId") long userId){
        User user = userRepo.findById(userId).isPresent()? userRepo.findById(userId).get():new User();
        return user;
    }

    @GetMapping(params = {"login"})
    public HttpEntity<String> getUserByLogin(@RequestParam("login") String login){
        return new ResponseEntity<>(userRepo.findByLogin(login).toString(), HttpStatus.OK);
    }

    @PutMapping
    public HttpEntity<String> update(@RequestParam("user") User user) {
        User newUser = userRepo.updateUser(user.getLogin(), user.getPassword(), user.getName(),
                user.getAddress(), user.getComment(), user.getUser_id());
        return new ResponseEntity<>(newUser.toString(), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public HttpStatus deleteUser(@PathVariable("userId") long userId){
        userRepo.deleteById(userId);
        return HttpStatus.OK;
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
