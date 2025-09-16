package br.com.fiap.hospital.auth.controller;

import br.com.fiap.hospital.auth.dto.UserRequest;
import br.com.fiap.hospital.auth.dto.UserResponse;
import br.com.fiap.hospital.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE')")
    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(required = false) String role) {

        List<UserResponse> users = userService.findAllUsers(role);
        return ResponseEntity.ok(users);
    }

}
