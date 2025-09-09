package br.com.fiap.hospital.auth.model;

import br.com.fiap.hospital.shared.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private Long id;
    private String name;
    private String email;
    private String login;
    private String password;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private Set<Role> roles;

}
