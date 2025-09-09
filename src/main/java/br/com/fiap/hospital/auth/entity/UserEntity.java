package br.com.fiap.hospital.auth.entity;

import br.com.fiap.hospital.shared.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @Column(unique = true)
    private String login;
    private String password;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    public UserEntity(){}
}
