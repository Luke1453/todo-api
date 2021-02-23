package com.lukcrow.todoapi.controllers;

import com.lukcrow.todoapi.repositories.TodoRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.lukcrow.todoapi.models.Todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/api/v1")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    // get all todos
    // Returns: Json with all todos
    // throws: ResourceNotFoundException the resource not found exception
    @GetMapping("todos")
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    // get all todos of certain user
    // PathVariable: userId
    // Returns: Json with all users todos
    // throws: ResourceNotFoundException the resource not found exception
    @GetMapping("users/{userId}/todos")
    public List<Todo> getTodosByUserId(@PathVariable(value = "userId") int userId) throws ResourceNotFoundException {
        List<Todo> todos = todoRepository.findByUserID(userId);

        if (todos.isEmpty()) {
            throw new ResourceNotFoundException("Todos not found check userID (or user has no todos)");
        }
        return todos;
    }

    // get certain todos of certain user
    // PathVariable: userId, todoId
    // Returns: Json with todo info
    // throws: ResourceNotFoundException the resource not found exception
    @GetMapping("users/{userId}/todos/{todoId}")
    public ResponseEntity<Todo> getTodoByIDAndUserID(@PathVariable(value = "userId") int userId,
            @PathVariable(value = "todoId") int todoId) throws ResourceNotFoundException {
        List<Todo> todos = todoRepository.findByUserID(userId);

        if (todos.isEmpty()) {
            throw new ResourceNotFoundException("Todos not found check userID (or user has no todos)");
        }

        List<Todo> result = todos.stream().filter(item -> item.getId() == todoId).collect(Collectors.toList());
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("User has no todo with such ID");
        }
        return ResponseEntity.ok().body(result.get(0));
    }

    // Create todo
    // Request body: JSON with todo info
    // Returns: Json with todo Info and id
    // throws: ResourceNotFoundException the resource not found exception
    @PostMapping("createTodo")
    public Todo createUser(@Valid @RequestBody Todo newTodo) {

        return todoRepository.save(newTodo);
    }

    // Update todo object.
    // PathVariable: userId, todoId
    // Request body: JSON with updated todo info
    // Returns: Json with updated todo Info
    // throws: ResourceNotFoundException the resource not found exception
    @PutMapping("users/{userId}/updateTodo/{todoId}")
    public ResponseEntity<Todo> updateTodo(@PathVariable(value = "userId") int userId,
            @PathVariable(value = "todoId") int todoId, @Valid @RequestBody Todo updatedTodo)
            throws ResourceNotFoundException {

        List<Todo> todos = todoRepository.findByUserID(userId);

        if (todos.isEmpty()) {
            throw new ResourceNotFoundException("Todos not found check userID (or user has no todos)");
        }

        List<Todo> result = todos.stream().filter(item -> item.getId() == todoId).collect(Collectors.toList());
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("User has no todo with such ID");
        }
        Todo oldTodo = result.get(0);

        if (updatedTodo.getTitle().length() != 0) {
            oldTodo.setTitle(updatedTodo.getTitle());
        }
        if (updatedTodo.getBody().length() != 0) {
            oldTodo.setBody(updatedTodo.getBody());
        }
        if (updatedTodo.getAddedDate().length() != 0) {
            oldTodo.setAddedDate(updatedTodo.getAddedDate());
        }
        if (updatedTodo.getDeadlineDate().length() != 0) {
            oldTodo.setDeadlineDate(updatedTodo.getDeadlineDate());
        }
        final Todo todo = todoRepository.save(oldTodo);
        return ResponseEntity.ok(todo);
    }

    // delete todo object.
    // PathVariable: userId, todoId
    // Returns: Json with a response
    // throws: ResourceNotFoundException the resource not found exception
    @DeleteMapping("users/{userId}/deleteTodo/{todoId}")
    public Map<String, Boolean> deleteTodo(@PathVariable(value = "userId") int userId,
            @PathVariable(value = "todoId") int todoId) throws Exception {

        Todo todo = todoRepository.findByUserIDAndId(userId, todoId);
                // .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));

        todoRepository.delete(todo);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}