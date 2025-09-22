package com.management.task.services.entity;

import com.management.task.services.Enums.Priority;
import com.management.task.services.Enums.TaskStatus;
import com.management.task.services.Enums.TaskType;
import com.management.task.services.constants.DatabaseConstants;
import com.management.task.services.requests.TaskRequest;
import com.management.task.services.responses.TaskResponse;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = DatabaseConstants.TASKS_TABLE_NAME, schema = DatabaseConstants.SCHEMA_NAME)
public class TaskEntity {

    @Id
    @UuidGenerator
    @Column(name = "task_id")
    private UUID taskId;

    private String title;

    private String description;

    @Column(name = "task_status")
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type")
    private TaskType taskType;

    private int estimation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private UserEntity  createdBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "assigned_to", nullable = false)
    private UserEntity  assignedTo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    public TaskEntity(TaskRequest taskRequest, UserEntity createdBy, UserEntity assignedTo) {
        this.taskId = taskRequest.getTaskId();
        this.title = taskRequest.getTitle();
        this.description = taskRequest.getDescription();
        this.taskStatus = taskRequest.getTaskStatus();
        this.priority = taskRequest.getPriority();
        this.taskType = taskRequest.getTaskType();
        this.estimation = taskRequest.getEstimation();
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public TaskResponse toTaskResponse(){
        return new TaskResponse(taskId, title, description, taskStatus, priority, taskType, estimation,
                createdBy.toUserResponse(), assignedTo == null ? null : assignedTo.toUserResponse(),
                createdAt, updatedAt);
    }
}
