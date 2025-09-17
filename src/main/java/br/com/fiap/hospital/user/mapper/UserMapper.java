package br.com.fiap.hospital.user.mapper;

import br.com.fiap.hospital.user.dto.UserRequest;
import br.com.fiap.hospital.user.dto.UserResponse;
import br.com.fiap.hospital.user.entity.UserEntity;

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
                .id(model.getId())
                .name(model.getName())
                .password(model.getPassword())
                .login(model.getLogin())
                .roles(model.getRoles())
                .build();
    }
}
