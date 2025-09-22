package com.management.task.services.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.management.task.services.Enums.Priority;
import com.management.task.services.Enums.TaskStatus;
import com.management.task.services.Enums.TaskType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskRequest {

    @Schema(hidden = true)
    private UUID taskId;

    @Schema(description = "The title of the task", example = "Title")
    private String title;

    @Schema(description = "The description of the task",example = "Description")
    private String description;

    @Schema(description = "The status of the task, allowed values are BACKLOG/IN_PROGRESS/COMPLATE/DONE")
    private TaskStatus taskStatus;

    @Schema(description = "The priority of the task, allowed values are HIGH/MEDIUM/LOW")
    private Priority priority;

    @Schema(description = "The type of the task, allowed values are STORY/DEFECT/EPIC")
    private TaskType taskType;

    @Schema(description = "Story point of the task")
    private int estimation;

    @Schema(hidden = true)
    private UUID createdBy;

    @Schema(description = "The user which assigned this task")
    private UUID assignedTo;

    @Schema(hidden = true)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
