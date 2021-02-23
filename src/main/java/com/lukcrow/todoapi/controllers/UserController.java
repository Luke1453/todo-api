package com.lukcrow.todoapi.controllers;

import com.lukcrow.todoapi.repositories.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.lukcrow.todoapi.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

@RestController
@RequestMapping("/api/v1")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  // get all users
  // Returns: Json with info of all users
  // throws: ResourceNotFoundException the resource not found exception
  @GetMapping("users")
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  // get user info
  // PathVariable: id = User Id
  // Returns: Json with User Info
  // throws: ResourceNotFoundException the resource not found exception
  @GetMapping("users/{id}")
  public ResponseEntity<User> getUsersById(@PathVariable(value = "id") int userId) throws ResourceNotFoundException {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));
    return ResponseEntity.ok().body(user);
  }

  // Login user
  // Request body: JSON with user email and password
  // Returns: Json with User Info
  // throws: ResourceNotFoundException the resource not found exception
  @GetMapping("loginUser")
  @ResponseBody
  public ResponseEntity<User> logInUser(@RequestParam String email, @RequestParam String password)
      throws ResourceNotFoundException {

    List<User> users = userRepository.findByEmail(email);
    if (users.isEmpty()) {
      throw new ResourceNotFoundException("User not found: Wrong Email");
    }

    List<User> result = users.stream().filter(item -> item.getPassword().equals(password)).collect(Collectors.toList());
    if (result.isEmpty()) {
      throw new ResourceNotFoundException("User not found: Wrong Password");
    }

    return ResponseEntity.ok().body(result.get(0));
  }

  // Register user
  // Request body: JSON with user email and password
  // Returns: Json with User Info and id
  // throws: ResourceNotFoundException the resource not found exception
  @PostMapping("registerUser")
  public User createUser(@Valid @RequestBody User user) {
    List<User> users = userRepository.findByEmail(user.getEmail());
    if (!users.isEmpty()) {
      throw new ResourceNotFoundException("User with this email already exists");
    }

    return userRepository.save(user);
  }

  // Update user object.
  // PathVariable: id = User Id
  // Request body: JSON with new user email and password
  // Returns: Json with updated User Info
  // throws: ResourceNotFoundException the resource not found exception
  @PutMapping("changeUserPassword/{id}")
  public ResponseEntity<User> updateUser(@PathVariable(value = "id") int userId, @Valid @RequestBody User userDetails)
      throws ResourceNotFoundException {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));

    if (userDetails.getEmail().length() != 0) {
      user.setEmail(userDetails.getEmail());
    }
    if (userDetails.getPassword().length() != 0) {
      user.setPassword(userDetails.getPassword());
    }
    final User updatedUser = userRepository.save(user);
    return ResponseEntity.ok(updatedUser);
  }

  // delete user object.
  // PathVariable: id = User Id
  // Returns: Json with a response
  // throws: ResourceNotFoundException the resource not found exception
  @DeleteMapping("deleteUser/{id}")
  public Map<String, Boolean> deleteUser(@PathVariable(value = "id") int userId) throws Exception {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));

    userRepository.delete(user);
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return response;
  }
}