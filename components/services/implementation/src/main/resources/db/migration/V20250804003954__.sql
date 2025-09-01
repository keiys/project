CREATE TABLE task.users
(
    user_id           UUID primary key not null,
    first_name        varchar(64)      not null,
    last_name         varchar(64)      not null,
    email             varchar(64)      not null,
    password          varchar(64)      not null,
    verification_code varchar(64),
    status            varchar(64),
    role              varchar(64)
);