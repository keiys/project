package com.management.task.services.controller;

import com.management.task.ExceptionResponse;
import com.management.task.services.constants.RoutConstants;
import com.management.task.services.repository.TaskRepository;
import com.management.task.services.requests.TaskRequest;
import com.management.task.services.requests.UserRequest;
import com.management.task.services.responses.TaskResponse;
import com.management.task.services.responses.UserResponse;
import com.management.task.services.security.RequiredAdminUser;
import com.management.task.services.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(RoutConstants.BASE_URL + "${task.service.verion}" + RoutConstants.TASKS)
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Operation(summary = "Create task with specified parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="201", description = "User created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))
            }),
            @ApiResponse(responseCode ="400", description = "Invalid request to sent endpoint", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
            @ApiResponse(responseCode ="500", description = "Error occurred while creating task", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
    })
    @PostMapping
    @RequiredAdminUser
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@RequestBody TaskRequest taskRequest) {
        log.info("Received request to create a new task");
        TaskResponse task = taskService.createTask(taskRequest);
        log.info("Created a new task");
        return task;
    }

    @Operation(summary = "Get task by specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="201", description = "Task found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))
            }),
            @ApiResponse(responseCode ="400", description = "Invalid request to sent endpoint", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
            @ApiResponse(responseCode ="404", description = "Entity not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
            @ApiResponse(responseCode ="500", description = "Error occurred while getting task", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
    })
    @GetMapping("/{taskid}")
    public TaskResponse getTaskByID(@PathVariable UUID taskid) {
        log.info("Received request to get task by ID {}", taskid);
        TaskResponse task = taskService.findTaskById(taskid);
        log.info("Task {}", task);
        return task;
    }

    @Operation(summary = "Get all tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200", description = "Request was successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))
            }),
            @ApiResponse(responseCode ="500", description = "Error occurred while getting all tasks", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
    })
    @GetMapping
    public Page<TaskResponse> getAllTasks(@RequestParam int page, @RequestParam int size) {
        log.info("Received request to get all users");
        Page<TaskResponse> allTasks = taskService.getAllTasks(page, size);
        log.info("All tasks: {}", allTasks);
        return allTasks;

    }

    @Operation(summary = "Delete task by given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="204", description = "Task deleted", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))
            }),
            @ApiResponse(responseCode ="404", description = "Entity not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
            @ApiResponse(responseCode ="500", description = "Error occurred while deleting task", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
    })
    @DeleteMapping("/{taskid}")
    @RequiredAdminUser
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@RequestParam UUID taskid) {
        log.info("Received request to delete user {}", taskid);
        taskService.deleteTaskById(taskid);
        log.info("Task deleted: {}", taskid);
    }

    @Operation(summary = "Update task with specified parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="200", description = "Task updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))
            }),
            @ApiResponse(responseCode ="400", description = "Invalid request to sent endpoint", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
            @ApiResponse(responseCode ="404", description = "Entity not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
            @ApiResponse(responseCode ="500", description = "Error occurred while updating task", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }),
    })
    @PutMapping("/{taskId}")
    @RequiredAdminUser
    public TaskResponse updateTask(@PathVariable UUID taskId, @RequestBody @Valid TaskRequest taskRequest) {
        log.info("Received request to update task with ID {}", taskId);
        TaskResponse task = taskService.updateTask(taskId, taskRequest);
        log.info("Task updated: {}", taskRequest);
        return task;
    }
}
