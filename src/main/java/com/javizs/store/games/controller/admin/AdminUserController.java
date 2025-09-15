package com.javizs.store.games.controller.admin;

import com.javizs.store.games.dto.user.UserCreateDto;
import com.javizs.store.games.dto.user.UserDto;
import com.javizs.store.games.dto.user.UserUpdateDto;
import com.javizs.store.games.entity.user.User;
import com.javizs.store.games.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminUserController {

    private final UserService userService;

    @PostMapping("/create-admin")
    public ResponseEntity<UserDto> createAdminUser(@Valid @RequestBody UserCreateDto userCreateDto){
        UserDto createdUser = userService.createAdminUser(userCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @Valid  @RequestBody UserUpdateDto userUpdateDto){
        return ResponseEntity.ok(userService.updateUser(id,userUpdateDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
