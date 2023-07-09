package com.zuzex.testtask.controller;

import com.zuzex.testtask.dto.JwtRequest;
import com.zuzex.testtask.entity.User;
import com.zuzex.testtask.security.JwtToken;
import com.zuzex.testtask.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final UserService userService;
    private final JwtToken jwtToken;
    private final AuthenticationManager authManager;

    public AuthenticationController(UserService userService, JwtToken jwtToken, AuthenticationManager authManager) {
        this.userService = userService;
        this.jwtToken = jwtToken;
        this.authManager = authManager;
    }

    @PostMapping
    public ResponseEntity<String> createToken(@RequestBody JwtRequest request) {
        this.authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = (User) this.userService.loadUserByUsername(request.getUsername());
        return ResponseEntity.ok(this.jwtToken.generate(user));
    }
}
