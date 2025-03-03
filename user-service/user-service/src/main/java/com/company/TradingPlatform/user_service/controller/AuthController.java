package com.company.TradingPlatform.user_service.controller;

import com.company.TradingPlatform.user_service.dtos.AuthRequestDto;
import com.company.TradingPlatform.user_service.dtos.UserDto;
import com.company.TradingPlatform.user_service.entitiy.User;
import com.company.TradingPlatform.user_service.exceptions.BadRequestException;
import com.company.TradingPlatform.user_service.repository.UserRepository;
import com.company.TradingPlatform.user_service.service.AuthService;
import com.company.TradingPlatform.user_service.service.JwtTokenProvider;
import com.company.TradingPlatform.user_service.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final UserRepository userRepository;

    /**
     * User Registration
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody AuthRequestDto authRequestDto) {
        try {
            UserDto userDto = authService.signUp(authRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Signup failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Signup failed: " + e.getMessage());
        }
    }

    /**
     * User Login - Returns JWT Token
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequestDto authRequestDto) {
        try {
            String token = authService.login(authRequestDto);
            return ResponseEntity.ok(token);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            log.error("Login failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed: " + e.getMessage());
        }
    }

    /**
     * Validate Token
     */
    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam("token") String token) {
        boolean isValid = jwtTokenProvider.validateToken(token);
        return isValid ? ResponseEntity.ok("Token is valid") :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
    }

    /**
     * Get User by Email
     */
    @GetMapping("/user/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        UserDto userDto = authService.getUserByEmail(email);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Update User
     */
    @PutMapping("/user")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        UserDto updatedUser = authService.updateUser(userDto);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Get User by ID
     */
    @GetMapping("/user/id/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        log.info("Received request to fetch user by ID: {}", userId);
        UserDto userDto = authService.getUserById(userId);
        log.info("Returning user: {}", userDto);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Forgot Password - Generate OTP
     */
    @PostMapping("/forget-password")
    public ResponseEntity<String> forgetPassword(@RequestParam String email) {
        return ResponseEntity.ok(otpService.generateOtp(email));
    }

    /**
     * Verify OTP
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = otpService.validateOtp(email, otp);
        return isValid ? ResponseEntity.ok("OTP Verified") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
    }

    /**
     * Reset Password with OTP
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email,
                                                @RequestParam String otp,
                                                @RequestParam String newPassword) {
        boolean isValid = otpService.validateOtp(email, otp);
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
        }

        // Find user and reset password
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);



        return ResponseEntity.ok("Password reset successfully");
    }
}
