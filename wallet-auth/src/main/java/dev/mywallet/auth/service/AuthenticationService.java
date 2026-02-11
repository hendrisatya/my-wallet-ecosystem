package dev.mywallet.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.mywallet.auth.dto.AuthenticationRequest;
import dev.mywallet.auth.dto.AuthenticationResponse;
import dev.mywallet.auth.dto.RegisterRequest;
import dev.mywallet.auth.entity.UserEntity;
import dev.mywallet.auth.repository.UserRepository;
import dev.mywallet.auth.util.JwtUtils;
import dev.mywallet.common.enums.Role;
import dev.mywallet.common.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserProducer userProducer;

    @SuppressWarnings("null")
    public AuthenticationResponse register(RegisterRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already taken");
        }

        var user = UserEntity.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .active(true)
                .build();

        var savedUser = repository.save(user);

        userProducer.sendUserCreatedEvent(new UserCreatedEvent(
            savedUser.getId().toString(),
            savedUser.getEmail(),
            savedUser.getFullName()
        ));

        var jwtToken = jwtUtils.generateToken(savedUser.getEmail());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        var user = repository.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken = jwtUtils.generateToken(user.getEmail());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
