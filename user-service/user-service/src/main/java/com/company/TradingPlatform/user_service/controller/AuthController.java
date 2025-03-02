package com.company.TradingPlatform.user_service.controller;



import com.company.TradingPlatform.user_service.dtos.AuthRequestDto;
import com.company.TradingPlatform.user_service.dtos.UserDto;
import com.company.TradingPlatform.user_service.exceptions.BadRequestException;
import com.company.TradingPlatform.user_service.service.AuthService;
import com.company.TradingPlatform.user_service.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody AuthRequestDto authRequestDto) {
        try {
            UserDto userDto = authService.signUp(authRequestDto);
            return new ResponseEntity<>(userDto, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Signup failed: " + e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequestDto authRequestDto) {
        try {
            String token = authService.login(authRequestDto);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            e.printStackTrace(); // Logs the full error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed: " + e.getMessage());
        }
    }

    @GetMapping("/validate-token")
    public Boolean validateToken(@RequestParam("token") String token) {
        return jwtTokenProvider.validateToken(token);
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        UserDto userDto = authService.getUserByEmail(email);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/user")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        UserDto updatedUser = authService.updateUser(userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/user/id/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        log.info("Received request to fetch user by ID: {}", userId);
        UserDto userDto = authService.getUserById(userId);
        log.info("Returning user: {}", userDto);
        return ResponseEntity.ok(userDto);
    }

}
