package br.com.fiap.hospital.auth.service;

import br.com.fiap.hospital.auth.dto.UserRequest;
import br.com.fiap.hospital.auth.dto.UserResponse;
import br.com.fiap.hospital.auth.entity.UserEntity;
import br.com.fiap.hospital.auth.mapper.UserMapper;
import br.com.fiap.hospital.auth.repository.UserRepository;
import br.com.fiap.hospital.shared.ResourceNotFoundException;
import br.com.fiap.hospital.shared.Role;
import br.com.fiap.hospital.shared.UserAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public UserResponse registerUser(UserRequest userRequest){
        if (userRepository.existsByLogin(userRequest.getLogin())) {
            throw new UserAlreadyExistsException("O login '" + userRequest.getLogin() + "' já está em uso.");
        }
        UserEntity entity = UserMapper.toEntity(userRequest);
        entity.setPassword(passwordEncoder.encode(userRequest.getPassword())); // Criptografa a senha antes de salvar
        entity.setRoles(userRequest.getRoles());
        UserEntity saved = userRepository.save(entity);
        return UserMapper.toResponse(saved);
    }

    public List<UserResponse> findAllUsers(String role) {
        List<UserEntity> users;

        if (role != null && !role.trim().isEmpty()) {
            try {
                Role roleEnum = Role.valueOf(role.toUpperCase());
                users = userRepository.findByRoles(roleEnum);

            } catch (IllegalArgumentException e) {
                throw new ResourceNotFoundException("A role '" + role + "' não é válida.");
            }
        } else {
            users = userRepository.findAll();
        }

        return users.stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

}
