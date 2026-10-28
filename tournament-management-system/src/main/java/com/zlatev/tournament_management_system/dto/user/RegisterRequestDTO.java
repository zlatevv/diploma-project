package com.zlatev.tournament_management_system.dto.user;

import com.zlatev.tournament_management_system.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequestDTO (
        @NotNull
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email is not valid")
        String email,

        @ValidPassword
        String password
){
}
