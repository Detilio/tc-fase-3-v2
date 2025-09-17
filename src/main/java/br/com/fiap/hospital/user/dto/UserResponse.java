package br.com.fiap.hospital.user.dto;

import br.com.fiap.hospital.shared.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class UserResponse {
    private Long id;
    private String name;
    private String password;
    private String login;
    private Set<Role> roles;
}
