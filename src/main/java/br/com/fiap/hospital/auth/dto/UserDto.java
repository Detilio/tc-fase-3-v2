package br.com.fiap.hospital.auth.dto;

import br.com.fiap.hospital.shared.Role;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record UserDto(Long id,
                      String name,
                      String email,
                      String login,
                      String password,
                      LocalDateTime updatedAt,
                      LocalDateTime createdAt,
                      Set<Role> roles) {
}
