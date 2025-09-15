package com.javizs.store.games.dto.user;

import com.javizs.store.games.entity.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDto {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Email(message = "Email no válido")
    @NotBlank(message = " El email es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    private String phone;

    private Role rol;
}
