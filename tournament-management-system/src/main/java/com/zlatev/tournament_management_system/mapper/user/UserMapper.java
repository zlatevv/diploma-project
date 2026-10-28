package com.zlatev.tournament_management_system.mapper.user;


import com.zlatev.tournament_management_system.dto.user.RegisterRequestDTO;
import com.zlatev.tournament_management_system.dto.user.UserResponseDTO;
import com.zlatev.tournament_management_system.entity.enums.user.Role;
import com.zlatev.tournament_management_system.entity.model.user.User;

import java.time.LocalDateTime;

public class UserMapper {
    public static User toUserEntity(RegisterRequestDTO registerRequest, String normalizedEmail) {
        if (registerRequest == null) {
            return null;
        }
        return User.builder()
                .fullName(registerRequest.fullName())
                .email(normalizedEmail)
                .createdAt(LocalDateTime.now())
                .role(Role.USER)
                .build();
    }

    public static UserResponseDTO toUserResponse(User user) {
        return new UserResponseDTO(
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getRole());
    }
}