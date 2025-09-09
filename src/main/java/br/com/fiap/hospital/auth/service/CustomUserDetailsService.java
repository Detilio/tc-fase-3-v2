package br.com.fiap.hospital.auth.service;

import br.com.fiap.hospital.auth.entity.UserEntity;
import br.com.fiap.hospital.auth.repository.UserRepository;
import br.com.fiap.hospital.auth.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + login));

        // Retorne a nova classe UserPrincipal!
        return new UserPrincipal(userEntity);
    }
}