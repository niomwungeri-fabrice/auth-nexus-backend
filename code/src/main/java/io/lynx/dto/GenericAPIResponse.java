package io.lynx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericAPIResponse {
    private String message;
    private Object data;
    private int statusCode;
}

