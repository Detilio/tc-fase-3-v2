package br.com.fiap.hospital.auth.mapper;

import br.com.fiap.hospital.auth.dto.UserRequest;
import br.com.fiap.hospital.auth.dto.UserResponse;
import br.com.fiap.hospital.auth.entity.UserEntity;

public class UserMapper {
    public static UserEntity toEntity(UserRequest model) {
        return UserEntity.builder()
                .name(model.getName())
                .password(model.getPassword())
                .login(model.getLogin())
                .roles(model.getRoles())
                .build();
    }

    public static UserResponse toResponse(UserEntity model) {
        return UserResponse.builder()
                .name(model.getName())
                .password(model.getPassword())
                .login(model.getLogin())
                .roles(model.getRoles())
                .build();
    }
}
