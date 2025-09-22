CREATE TABLE task.tasks
(
    task_id      UUID primary key not null,
    title        varchar(255)     not null,
    description  varchar(255),
    task_status  varchar(64),
    priority     varchar(64),
    task_type    varchar(64),
    estimation INT,
    created_at   TIMESTAMP DEFAULT NOW(),
    updated_at   TIMESTAMP DEFAULT NOW(),
    created_by UUID NOT NULL,
    assigned_to UUID,
    CONSTRAINT fk_task_created_by_user
        FOREIGN KEY (created_by) REFERENCES task.users(user_id),
    CONSTRAINT fk_task_assigned_to_user
        FOREIGN KEY (assigned_to) REFERENCES task.users(user_id)
);