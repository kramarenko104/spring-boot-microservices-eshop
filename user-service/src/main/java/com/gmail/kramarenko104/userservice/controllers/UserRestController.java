package com.gmail.kramarenko104.userservice.controllers;

import com.gmail.kramarenko104.userservice.models.User;
import com.gmail.kramarenko104.userservice.services.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<String> getAllUsers(){
        return (List<String>)((List)userService.getAllUsers())
                .stream()
                .map(user -> user.toString())
                .collect(Collectors.toList());
    }

    @PostMapping()
    @ApiOperation(value = "Create The New User", notes = "Add the new user to user-service DB", response = User.class)
    public HttpEntity<String> createUser(@RequestParam("user") User user){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(user).toString());
    }

    @GetMapping("/{userId}")
    @ApiOperation(value = "Get User by userId", notes = "Get user by userId from user-service DB", response = User.class)
    public HttpEntity<User> getUser(@PathVariable("userId") long userId){
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/api/{userId}")
    public User getUserAPI(@PathVariable("userId") long userId){
        return userService.getUser(userId);
    }

    @GetMapping(params = {"login"})
    @ApiOperation(value = "Get User by login", notes = "Get user by login from user-service DB", response = User.class)
    public HttpEntity<User> getUserByLogin(@RequestParam("login") String login){
        return ResponseEntity.ok(userService.getUserByLogin(login));
    }

    @PutMapping
    @ApiOperation(value = "Update User", notes = "Update user into user-service DB", response = User.class)
    public HttpEntity<User> update(@RequestParam("user") User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("/{userId}")
    @ApiOperation(value = "Delete User by userId", notes = "Delete user by userId from user-service DB")
    public HttpStatus deleteUser(@PathVariable("userId") long userId){
        userService.deleteUser(userId);
        return HttpStatus.OK;
    }
}
