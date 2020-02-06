package com.gmail.kramarenko104.userservice.controllers;

import com.gmail.kramarenko104.userservice.models.User;
import com.gmail.kramarenko104.userservice.services.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Api(value = "users", tags ="users")
public class UserRestController {

    UserServiceImpl userService;

    @Autowired
    public UserRestController(UserServiceImpl userService){
        this.userService = userService;
    }

    @GetMapping()
    @ApiOperation(value = "Get All Users", notes = "Get all users from user-service DB", tags = "getAllUsers", response = User.class)
    public ResponseEntity<String> getAllUsersJSON(){
        return userService.getAllUsersJSON()
                .map(foundUsers -> ResponseEntity.ok(foundUsers))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    @ApiOperation(value = "Create The New User", notes = "Add the new user to user-service DB", response = User.class)
    public ResponseEntity<String> createUser(@RequestParam("user") User user){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(user).toJSON().toString());
    }

    @GetMapping("/{userId}")
    @ApiOperation(value = "Get User by userId", notes = "Get user by userId from user-service DB", response = User.class)
    public ResponseEntity<String> getUser(@PathVariable("userId") long userId){
        return userService.getUser(userId)
                .map(foundUser -> ResponseEntity.ok(foundUser.toJSON().toString()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(params = {"login"})
    @ApiOperation(value = "Get User by login", notes = "Get user by login from user-service DB", response = User.class)
    public ResponseEntity<String> getUserByLogin(@RequestParam("login") String login){
        return userService.getUserByLogin(login)
                .map(foundUser -> ResponseEntity.ok(foundUser.toJSON().toString()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    @ApiOperation(value = "Update User", notes = "Update user into user-service DB", response = User.class)
    public ResponseEntity<String> update(@RequestParam("user") User user) {
        return ResponseEntity.ok(userService.updateUser(user).toJSON().toString());
    }

    @DeleteMapping("/{userId}")
    @ApiOperation(value = "Delete User by userId", notes = "Delete user by userId from user-service DB")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") long userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/{userId}")
    public Optional<User> getUserAPI(@PathVariable("userId") long userId){
        return userService.getUser(userId);
    }

    @GetMapping("/api")
    public Optional<List<User>> getAllUsers(){
        return userService.getAllUsers();
    }
}
