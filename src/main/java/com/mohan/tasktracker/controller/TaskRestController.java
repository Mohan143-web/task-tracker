package com.mohan.tasktracker.controller;

import com.mohan.tasktracker.model.Task;
import com.mohan.tasktracker.model.User;
import com.mohan.tasktracker.service.TaskService;
import com.mohan.tasktracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskRestController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    private User currentUser(Principal principal) {
        return userService.getByUsername(principal.getName());
    }

    @GetMapping
    public List<Task> list(Principal principal) {
        return taskService.listForUser(currentUser(principal));
    }

    @GetMapping("/{id}")
    public Task get(@PathVariable Long id, Principal principal) {
        return taskService.getOwnedById(id, currentUser(principal));
    }

    @PostMapping
    public Task create(@Valid @RequestBody Task task, Principal principal) {
        return taskService.create(task, currentUser(principal));
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @Valid @RequestBody Task task, Principal principal) {
        return taskService.update(id, task, currentUser(principal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Principal principal) {
        taskService.delete(id, currentUser(principal));
        return ResponseEntity.noContent().build();
    }
}
