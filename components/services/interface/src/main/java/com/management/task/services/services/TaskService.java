package com.management.task.services.services;

import com.management.task.services.requests.TaskRequest;
import com.management.task.services.responses.TaskResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface TaskService {

    TaskResponse createTask(TaskRequest taskRequest);

    TaskResponse findTaskById(UUID id);

    Page<TaskResponse> getAllTasks (int page, int size);

    void deleteTaskById(UUID id);

    TaskResponse updateTask(UUID id, TaskRequest taskRequest);
}
