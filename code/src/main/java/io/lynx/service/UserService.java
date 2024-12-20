package io.lynx.service;


import io.lynx.models.User;
import io.lynx.repository.UserRepository;
import io.lynx.utils.EncryptionUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final EncryptionUtil encryptionUtil;
    public UserService(UserRepository userRepository, EncryptionUtil encryptionUtil) {
        this.userRepository = userRepository;
        this.encryptionUtil = encryptionUtil;
    }

    public User signUp(String email) {
        String otp = generateOtp();
        log.warn("=== generated OTP: {} ===", otp); // temporary log statement
        User user = userRepository.findByEmail(email)
                .orElse(new User());
        user.setEmail(email);
        String encryptedOtp = encryptionUtil.encrypt(otp);
        user.setOtp(encryptedOtp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10)); // OTP valid for 10 minutes

        return userRepository.save(user);
    }

    public Optional<User> validateOtp(String email, String providedOtp) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String decryptedOtp = encryptionUtil.decrypt(user.getOtp());
            
            if (decryptedOtp.equals(providedOtp) && user.getOtpExpiry().isAfter(LocalDateTime.now())) {
                return userOptional;
            }
        }
        return Optional.empty();
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generate 6-digit OTP
        return String.valueOf(otp);
    }
}
