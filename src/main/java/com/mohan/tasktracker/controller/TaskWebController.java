package com.mohan.tasktracker.controller;

import com.mohan.tasktracker.model.Task;
import com.mohan.tasktracker.model.User;
import com.mohan.tasktracker.service.TaskService;
import com.mohan.tasktracker.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class TaskWebController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskWebController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    private User currentUser(Principal principal) {
        return userService.getByUsername(principal.getName());
    }

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        User user = currentUser(principal);
        model.addAttribute("stats", taskService.stats(user));
        model.addAttribute("tasks", taskService.listForUser(user));
        return "dashboard";
    }

    @GetMapping("/tasks/new")
    public String newTask(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("statuses", Task.Status.values());
        return "task-form";
    }

    @PostMapping("/tasks")
    public String createTask(@Valid @ModelAttribute("task") Task task,
                             BindingResult br,
                             Principal principal,
                             Model model) {
        if (br.hasErrors()) {
            model.addAttribute("statuses", Task.Status.values());
            return "task-form";
        }
        taskService.create(task, currentUser(principal));
        return "redirect:/dashboard";
    }

    @GetMapping("/tasks/{id}/edit")
    public String editTask(@PathVariable Long id, Principal principal, Model model) {
        Task task = taskService.getOwnedById(id, currentUser(principal));
        model.addAttribute("task", task);
        model.addAttribute("statuses", Task.Status.values());
        return "task-form";
    }

    @PostMapping("/tasks/{id}")
    public String updateTask(@PathVariable Long id,
                             @Valid @ModelAttribute("task") Task task,
                             BindingResult br,
                             Principal principal,
                             Model model) {
        if (br.hasErrors()) {
            model.addAttribute("statuses", Task.Status.values());
            return "task-form";
        }
        taskService.update(id, task, currentUser(principal));
        return "redirect:/dashboard";
    }

    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable Long id, Principal principal) {
        taskService.delete(id, currentUser(principal));
        return "redirect:/dashboard";
    }
}
