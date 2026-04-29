package com.mohan.tasktracker.service;

import com.mohan.tasktracker.model.Task;
import com.mohan.tasktracker.model.User;
import com.mohan.tasktracker.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> listForUser(User user) {
        return taskRepository.findByOwnerOrderByDueDateAscIdDesc(user);
    }

    public Task getOwnedById(Long id, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));
        if (!task.getOwner().getId().equals(user.getId())) {
            throw new SecurityException("Not authorized for task " + id);
        }
        return task;
    }

    @Transactional
    public Task create(Task task, User owner) {
        task.setOwner(owner);
        if (task.getStatus() == null) task.setStatus(Task.Status.TODO);
        return taskRepository.save(task);
    }

    @Transactional
    public Task update(Long id, Task updates, User owner) {
        Task existing = getOwnedById(id, owner);
        existing.setTitle(updates.getTitle());
        existing.setDescription(updates.getDescription());
        existing.setStatus(updates.getStatus());
        existing.setDueDate(updates.getDueDate());
        return existing;
    }

    @Transactional
    public void delete(Long id, User owner) {
        Task existing = getOwnedById(id, owner);
        taskRepository.delete(existing);
    }

    public DashboardStats stats(User user) {
        long total = taskRepository.countByOwner(user);
        long todo = taskRepository.countByOwnerAndStatus(user, Task.Status.TODO);
        long inProgress = taskRepository.countByOwnerAndStatus(user, Task.Status.IN_PROGRESS);
        long done = taskRepository.countByOwnerAndStatus(user, Task.Status.DONE);
        long overdue = taskRepository.findByOwnerOrderByDueDateAscIdDesc(user).stream()
                .filter(Task::isOverdue).count();
        return new DashboardStats(total, todo, inProgress, done, overdue);
    }

    public static class DashboardStats {
        public final long total, todo, inProgress, done, overdue;
        public DashboardStats(long total, long todo, long inProgress, long done, long overdue) {
            this.total = total;
            this.todo = todo;
            this.inProgress = inProgress;
            this.done = done;
            this.overdue = overdue;
        }
        public long getTotal() { return total; }
        public long getTodo() { return todo; }
        public long getInProgress() { return inProgress; }
        public long getDone() { return done; }
        public long getOverdue() { return overdue; }
    }
}
