package br.com.fiap.hospital.auth.repository;

import br.com.fiap.hospital.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByName(String name);

    Optional <UserEntity> findByLogin(String email);

    boolean existsByLogin(String login);
}
