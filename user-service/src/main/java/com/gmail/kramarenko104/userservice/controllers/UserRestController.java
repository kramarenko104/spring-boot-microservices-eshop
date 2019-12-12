package com.gmail.kramarenko104.userservice.controllers;

import com.gmail.kramarenko104.userservice.models.User;
import com.gmail.kramarenko104.userservice.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserRestController {

    UserServiceImpl userService;

    @Autowired
    public UserRestController(UserServiceImpl userService){
        this.userService = userService;
    }

    @GetMapping()
    public List<String> getAllUsers(){
        return (List<String>)((List)userService.getAllUsers())
                .stream()
                .map(user -> user.toString())
                .collect(Collectors.toList());
    }

    @PostMapping()
    public HttpEntity<String> createUser(@RequestParam("user") User user){
        return new ResponseEntity<>(userService.createUser(user).toString(), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public HttpEntity<String> getUser(@PathVariable("userId") long userId){
        return new ResponseEntity<>(userService.getUser(userId).toString(), HttpStatus.OK);
    }

    @GetMapping("/api/{userId}")
    public User getUserAPI(@PathVariable("userId") long userId){
        return userService.getUser(userId);
    }

    @GetMapping(params = {"login"})
    public HttpEntity<String> getUserByLogin(@RequestParam("login") String login){
        return new ResponseEntity<>(userService.getUserByLogin(login).toString(), HttpStatus.OK);
    }

    @PutMapping
    public HttpEntity<String> update(@RequestParam("user") User user) {
        return new ResponseEntity<>(userService.updateUser(user).toString(), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public HttpStatus deleteUser(@PathVariable("userId") long userId){
        userService.deleteUser(userId);
        return HttpStatus.OK;
    }
}
