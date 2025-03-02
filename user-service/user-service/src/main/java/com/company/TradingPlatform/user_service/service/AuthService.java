package com.company.TradingPlatform.user_service.service;


import com.company.TradingPlatform.user_service.dtos.AuthRequestDto;
import com.company.TradingPlatform.user_service.dtos.UserDto;
import com.company.TradingPlatform.user_service.entitiy.User;
import com.company.TradingPlatform.user_service.enums.Role;
import com.company.TradingPlatform.user_service.exceptions.BadRequestException;
import com.company.TradingPlatform.user_service.exceptions.ResourceNotFoundException;
import com.company.TradingPlatform.user_service.repository.UserRepository;
import com.company.TradingPlatform.user_service.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public UserDto signUp(AuthRequestDto authRequestDto) {
        // Check if the user already exists
        boolean exists = userRepository.existsByEmail(authRequestDto.getEmail());
        if (exists) {
            throw new BadRequestException("User already exists, cannot signup again");
        }

        // Map request DTO to User entity
        User user = modelMapper.map(authRequestDto, User.class);

        // Check if admin code is provided and valid
        if ("ADMIN123".equals(authRequestDto.getAdminCode())) {
            user.setRole(Role.ADMIN); // Allow setting ADMIN role
        } else if (authRequestDto.getRole() == null) {
            user.setRole(Role.USER); // Default to STUDENT
        } else {
            user.setRole(authRequestDto.getRole());
        }

        // Hash the password before saving
        user.setPassword(PasswordUtils.hashPassword(authRequestDto.getPassword()));

        // Save the user in the database
        User savedUser = userRepository.save(user);

        // Map saved User entity to UserDto for response
        return modelMapper.map(savedUser, UserDto.class);
    }


    public String login(AuthRequestDto authResponseDto){
        User user = userRepository.findByEmail(authResponseDto.getEmail())
                .orElseThrow(()->new ResourceNotFoundException("User not found with email: "+authResponseDto.getEmail()));

        boolean isPasswordMatch = PasswordUtils.checkPassword(authResponseDto.getPassword(),user.getPassword());
        if(!isPasswordMatch){
            throw new BadRequestException("Incorrect password");
        }
        return jwtTokenProvider.generateAccessToken(user);
    }

    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return modelMapper.map(user, UserDto.class);
    }

    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userDto.getEmail()));

        // Update user details
        user.setName(userDto.getName());
        user.setRole(userDto.getRole());
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDto.class);
    }
    public UserDto getUserById(Long userId) {
        log.info("Fetching user by ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        return new UserDto(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole());
    }



}
