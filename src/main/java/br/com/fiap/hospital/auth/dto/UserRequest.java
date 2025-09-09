package br.com.fiap.hospital.auth.dto;

import br.com.fiap.hospital.shared.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class UserRequest {
    private String name;
    private String login;
    private String password;
    private Set<Role> roles;
}
