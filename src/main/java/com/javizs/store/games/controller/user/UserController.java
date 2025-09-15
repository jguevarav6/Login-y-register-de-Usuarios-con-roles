package com.javizs.store.games.controller.user;

import com.javizs.store.games.dto.user.PasswordUpdateDto;
import com.javizs.store.games.entity.user.User;
import com.javizs.store.games.exception.UserNotFoundException;
import com.javizs.store.games.repository.user.UserRepository;
import com.javizs.store.games.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody PasswordUpdateDto passwordUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));

        userService.updatePassword(user.getId(), passwordUpdateDto);

        return ResponseEntity.noContent().build(); // 204 No Content si éxito
    }

}
