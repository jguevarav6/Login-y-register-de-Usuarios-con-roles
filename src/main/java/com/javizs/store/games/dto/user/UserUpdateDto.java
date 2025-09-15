package com.javizs.store.games.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {
    @NotBlank(message = "Name cannot be empty")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only letters and spaces")
    private String name;
    @NotBlank(message = "Name cannot be empty")
    @Email(message = "Email must be valid")
    private String email;
    @NotBlank(message = "Name cannot be empty")
    @Pattern(regexp = "^[0-9]+$", message = "Phone must contain only digits")
    private String phone;
}
