package io.lynx.controllers;


import io.lynx.dto.GenericAPIResponse;
import io.lynx.dto.SignUpRequest;
import io.lynx.dto.TokenRequest;
import io.lynx.exceptions.ErrorResponse;
import io.lynx.models.User;
import io.lynx.service.UserService;
import io.lynx.utils.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Endpoints for user authentication")
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<GenericAPIResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        User user = userService.signUp(signUpRequest.getEmail());
        return ResponseEntity.ok(new GenericAPIResponse(
                "OTP sent to " + user.getEmail(),
                null,
                HttpStatus.OK.value()
        ));
    }


    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestBody TokenRequest tokenRequest) {
        try {
            return userService.validateOtp(tokenRequest.getEmail(), tokenRequest.getOtp())
                    .<ResponseEntity<?>>map(user -> {
                        try {
                            String token = JwtUtil.generateToken(tokenRequest.getEmail());
                            return ResponseEntity.ok(new GenericAPIResponse("token generated successfully", token, 200));
                        } catch (Exception e) {
                            log.error("error generating token :-{}", e.getMessage());
                            return ResponseEntity.status(500)
                                    .body(new ErrorResponse(500, "error generating token: " + e.getMessage(), e.getStackTrace()));
                        }
                    })
                    .orElse(ResponseEntity.status(401)
                            .body(new ErrorResponse(401, "invalid OTP or expired OTP", null)));
        } catch (Exception e) {
            log.error("error:-{}", e.getMessage());
            return ResponseEntity.status(500)
                    .body(new ErrorResponse(500, "server error: " + e.getMessage(), e.getStackTrace()));
        }
    }
}
