package com.javizs.store.games.security.user;

import com.javizs.store.games.entity.user.User;
import com.javizs.store.games.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("🔍 Buscando usuario con email: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("❌ Usuario no encontrado: " + email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });

        System.out.println("✅ Usuario encontrado: " + user.getEmail());
        return new CustomUserDetailsImpl(user);
    }
}
