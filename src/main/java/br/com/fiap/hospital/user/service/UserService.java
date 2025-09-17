package br.com.fiap.hospital.user.service;

import br.com.fiap.hospital.agendamento.repository.ConsultationRepository;
import br.com.fiap.hospital.shared.exception.UserHasActiveConsultationException;
import br.com.fiap.hospital.user.dto.UserRequest;
import br.com.fiap.hospital.user.dto.UserResponse;
import br.com.fiap.hospital.user.entity.UserEntity;
import br.com.fiap.hospital.user.mapper.UserMapper;
import br.com.fiap.hospital.user.repository.UserRepository;
import br.com.fiap.hospital.shared.exception.ResourceNotFoundException;
import br.com.fiap.hospital.shared.Role;
import br.com.fiap.hospital.shared.exception.UserAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ConsultationRepository consultationRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, ConsultationRepository consultationRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.consultationRepository = consultationRepository;
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

    public UserResponse updateUser(Long userId, UserRequest userRequest) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + userId));

        userRepository.findByLogin(userRequest.getLogin()).ifPresent(userFound -> {
            if (!userFound.getId().equals(userId)) {
                throw new UserAlreadyExistsException("O login '" + userRequest.getLogin() + "' já está em uso por outro usuário.");
            }
        });

        userEntity.setLogin(userRequest.getLogin());
        userEntity.setRoles(userRequest.getRoles());

        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        UserEntity updatedUser = userRepository.save(userEntity);
        return UserMapper.toResponse(updatedUser);
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuário não encontrado com o ID: " + userId);
        }

        boolean hasConsultations = consultationRepository.existsByPatientId(userId) ||
                consultationRepository.existsByDoctorId(userId);

        if (hasConsultations) {
            throw new UserHasActiveConsultationException("Não é possível excluir o usuário pois ele possui consultas vinculadas.");
        }

        userRepository.deleteById(userId);
    }

}
