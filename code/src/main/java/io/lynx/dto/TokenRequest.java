package io.lynx.dto;

import lombok.Data;

@Data
public class TokenRequest {
    private String email;
    private String otp;
}
