package br.com.fiap.hospital.auth.controller;

import br.com.fiap.hospital.auth.dto.UserRequest;
import br.com.fiap.hospital.auth.dto.UserResponse;
import br.com.fiap.hospital.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> criar(@RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

}
