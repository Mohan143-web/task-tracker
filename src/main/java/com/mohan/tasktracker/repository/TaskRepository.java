package com.mohan.tasktracker.repository;

import com.mohan.tasktracker.model.Task;
import com.mohan.tasktracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByOwnerOrderByDueDateAscIdDesc(User owner);
    List<Task> findByOwnerAndStatus(User owner, Task.Status status);
    long countByOwnerAndStatus(User owner, Task.Status status);
    long countByOwner(User owner);
}
