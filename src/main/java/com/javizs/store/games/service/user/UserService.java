package com.javizs.store.games.service.user;

import com.javizs.store.games.dto.user.PasswordUpdateDto;
import com.javizs.store.games.dto.user.UserCreateDto;
import com.javizs.store.games.dto.user.UserDto;
import com.javizs.store.games.dto.user.UserUpdateDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
    UserDto updateUser(Long id, UserUpdateDto updateDto);
    void updatePassword (Long id, PasswordUpdateDto passwordUpdateDto);
    void deleteUser(Long id);
    UserDto createAdminUser(UserCreateDto userCreateDto);
}
