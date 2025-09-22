package com.management.task.services.responses;

import com.management.task.services.Enums.Priority;
import com.management.task.services.Enums.TaskStatus;
import com.management.task.services.Enums.TaskType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponse(
        UUID taskID,
        String title,
        String description,
        TaskStatus taskStatus,
        Priority priority,
        TaskType taskType,
        int estimation,
        UserResponse createdBy,
        UserResponse assignedTo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
