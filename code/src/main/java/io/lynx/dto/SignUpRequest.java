package io.lynx.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    private String firstName;
    private String lastName;
}
