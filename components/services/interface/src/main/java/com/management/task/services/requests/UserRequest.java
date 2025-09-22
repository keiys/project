package com.management.task.services.requests;

import com.management.task.services.Enums.Role;
import com.management.task.services.Enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private static final String NAME_NULL_MSG = "Name cannot be null or empty";
    private static final String NAME_REGEX = "[A-Z][a-z]+";
    private static final String NAME_REGEX_MSG = "Invalid name format";

    private static final String SURNAME_NULL_MSG = "Name cannot be null or empty";
    private static final String SURNAME_REGEX = "[A-Z][a-z]+";
    private static final String SURNAME_REGEX_MSG = "Invalid surname format";

    private static final String USER_EMAIL_NULL_MSG = "Email cannot be null or empty";
    private static final String USER_EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final String USER_EMAIL_REGEX_MSG = "Invalid email format";


    @Schema(hidden = true)
    private UUID userId;

    @NotEmpty(message = NAME_NULL_MSG)
    @Pattern(regexp = NAME_REGEX, message = NAME_REGEX_MSG)
    @Schema(description = "Name of user", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yumi")
    private String name;

    @NotEmpty(message = SURNAME_NULL_MSG)
    @Pattern(regexp = SURNAME_REGEX, message = SURNAME_REGEX_MSG)
    @Schema(description = "Surname of user", requiredMode = Schema.RequiredMode.REQUIRED, example = "Kim")
    private String surname;

    @NotEmpty(message = USER_EMAIL_NULL_MSG)
    @Pattern(regexp = USER_EMAIL_REGEX, message = USER_EMAIL_REGEX_MSG)
    @Schema(description = "Email address of user", requiredMode = Schema.RequiredMode.REQUIRED, example = "yumi@example.com")
    private String email;

    @Schema(description = "Password of user", requiredMode = Schema.RequiredMode.REQUIRED, example = "Example_123")
    private String password;

    @Schema(hidden = true)
    private String verifyCode;

    @Schema(hidden = true)
    private Status status;

    @Schema(description = "Role of the user")
    private Role role;
}
