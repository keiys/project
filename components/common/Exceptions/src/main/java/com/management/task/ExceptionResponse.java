package com.management.task;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse {

    private String exceptionMessage;
    private int status;
    private List<String> details;

    public ExceptionResponse(String exceptionMessage, int status) {
        this.exceptionMessage = exceptionMessage;
        this.status = status;
    }


}
