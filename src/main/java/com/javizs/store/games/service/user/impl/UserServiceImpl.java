package com.javizs.store.games.service.user.impl;

import com.javizs.store.games.dto.user.PasswordUpdateDto;
import com.javizs.store.games.dto.user.UserCreateDto;
import com.javizs.store.games.dto.user.UserDto;
import com.javizs.store.games.dto.user.UserUpdateDto;
import com.javizs.store.games.entity.user.Role;
import com.javizs.store.games.entity.user.User;
import com.javizs.store.games.exception.UserNotFoundException;
import com.javizs.store.games.repository.user.UserRepository;
import com.javizs.store.games.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createAdminUser(UserCreateDto userCreateDto){
        User user = new User();
        user.setName(userCreateDto.getName());
        user.setEmail(userCreateDto.getEmail());
        user.setPhone(userCreateDto.getPhone());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setRol(Role.ADMIN);
        user=userRepository.save(user);
        return mapToDto(user);
    }




    @Override
    public UserDto getUserById( Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found " +  id));
        return mapToDto(user);
    }
    private UserDto mapToDto(User user){
        UserDto dto= new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        return dto;
    }
    @Override
    public List<UserDto> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    @Override
    public UserDto updateUser(Long id, UserUpdateDto updateDto){
        User user= userRepository.findById(id)
                .orElseThrow(() ->  new UserNotFoundException("User not found " + id));
        user.setName(updateDto.getName());
        user.setEmail(updateDto.getEmail());
        user.setPhone(updateDto.getPhone());
        User updated = userRepository.save(user);
        return mapToDto(updated);
    }

    @Override
    public void updatePassword(Long id, PasswordUpdateDto passwordUpdateDto){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException(" User not found "+ id ));
        if (!passwordEncoder.matches(passwordUpdateDto.getCurrentPassword(), user.getPassword())){
            throw  new RuntimeException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
        userRepository.save(user);
    }




    @Override
    public void deleteUser( Long id){
        if (!userRepository.existsById(id)){
            throw  new UserNotFoundException("User not found with id " + id);

        }
        userRepository.deleteById(id);

    }

}
