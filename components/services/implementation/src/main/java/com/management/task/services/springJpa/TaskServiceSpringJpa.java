package com.management.task.services.springJpa;

import com.management.task.UserAware;
import com.management.task.services.Enums.TaskType;
import com.management.task.services.entity.TaskEntity;
import com.management.task.services.entity.UserEntity;
import com.management.task.services.exceptions.taskexceptions.TaskApiException;
import com.management.task.services.exceptions.taskexceptions.TaskBadRequestException;
import com.management.task.services.exceptions.taskexceptions.TaskNotFoundException;
import com.management.task.services.exceptions.userexceptions.UserApiException;
import com.management.task.services.exceptions.userexceptions.UserBadRequestException;
import com.management.task.services.exceptions.userexceptions.UserNotFoundException;
import com.management.task.services.repository.TaskRepository;
import com.management.task.services.repository.UserRepository;
import com.management.task.services.requests.TaskRequest;
import com.management.task.services.responses.TaskResponse;
import com.management.task.services.responses.UserResponse;
import com.management.task.services.services.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TaskServiceSpringJpa implements TaskService, UserAware {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public TaskResponse createTask(TaskRequest taskRequest) {

        UserResponse currentUser = getCurrentUser();
        log.info("current user: ", currentUser.email());
        UserEntity assignTo = null;

        if (taskRequest.getTaskId() != null) {
            throw new TaskBadRequestException("Task ID must be null");
        }

        if (taskRequest.getEstimation() < 0) {
            taskRequest.setEstimation(0);
        }

        try{
            UserEntity createdBy = new UserEntity(getCurrentUser());
            if (taskRequest.getAssignedTo() != null) {
                assignTo = userRepository.findById(taskRequest.getAssignedTo()).orElseThrow(() ->
                        new TaskNotFoundException("User not found"));
            }

            return taskRepository.save(new TaskEntity(taskRequest, createdBy, assignTo)).toTaskResponse();
        }catch (Exception ex) {
            if (ex instanceof TaskNotFoundException) {
                throw new TaskNotFoundException(ex.getMessage());
            }
            throw new TaskApiException("Error occurred while saving task");
        }
    }

    @Override
    public TaskResponse findTaskById(UUID taskId) {
        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(() ->
                new TaskNotFoundException("Task not found with id: " + taskId));
        return taskEntity.toTaskResponse();
    }

    @Override
    public Page<TaskResponse> getAllTasks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<TaskResponse> result = new ArrayList<>();
        long count;
        try {
            count = taskRepository.count();
            Page<TaskEntity> taskEntities = taskRepository.findAll(pageable);
            taskEntities.forEach(userEntity -> {
                result.add(userEntity.toTaskResponse());
            });
        } catch (Exception ex) {
            throw new TaskApiException("Error occurred while trying to get all tasks", ex);
        }
        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public void deleteTaskById(UUID taskId) {
        taskRepository.findById(taskId).orElseThrow(() ->
                new TaskNotFoundException("Task not found with id: " + taskId));

        try {
            taskRepository.deleteById(taskId);
        } catch (Exception ex) {
            throw new TaskApiException("Error occurred while trying to delete task", ex);
        }
    }

    @Override
    public TaskResponse updateTask(UUID taskId, TaskRequest taskRequest) {
        UserEntity assignTo = null;
        if (taskRequest.getTaskId() != null && !taskRequest.getTaskId().equals(taskId)) {
            throw new TaskBadRequestException("Task Id in path doesn't match id of task object");
        }

        if (taskRequest.getEstimation() < 0){
            taskRequest.setEstimation(0);
        }
        if (taskRequest.getCreatedBy() != null && !taskRequest.getCreatedBy().equals(getCurrentUser().userId())) {
            throw new UserBadRequestException("You can't modify task created by user");
        }

        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));


        if (taskRequest.getCreatedAt() != null || taskRequest.getUpdatedAt() != null) {
            taskRequest.setCreatedAt(taskEntity.getCreatedAt());
            taskRequest.setUpdatedAt(LocalDateTime.now());
        }

        if (taskRequest.getAssignedTo() != null) {
            assignTo = userRepository.findById(taskRequest.getAssignedTo()).orElseThrow(() ->
                        new TaskNotFoundException("Assigned to User not found"));
        }

        if ((taskRequest.getTaskType().equals(TaskType.EPIC) && !taskEntity.getTaskType().equals(TaskType.EPIC)) ||
                (!taskRequest.getTaskType().equals(TaskType.EPIC) && taskEntity.getTaskType().equals(TaskType.EPIC))) {
            throw new TaskBadRequestException("Task type not supported");
        }
        try{
            return taskRepository.save(new TaskEntity(taskRequest, taskEntity.getCreatedBy(), assignTo)).toTaskResponse();
        }catch (Exception ex) {
            throw new TaskApiException("Error while updating task", ex);
        }
    }
}
