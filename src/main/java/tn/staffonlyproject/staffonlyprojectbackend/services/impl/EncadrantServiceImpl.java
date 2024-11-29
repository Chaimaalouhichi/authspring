package tn.staffonlyproject.staffonlyprojectbackend.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.staffonlyproject.staffonlyprojectbackend.auth.AuthenticationResponse;
import tn.staffonlyproject.staffonlyprojectbackend.config.security.JwtService;
import tn.staffonlyproject.staffonlyprojectbackend.dto.request.CompleteProfileStagiareRequest;
import tn.staffonlyproject.staffonlyprojectbackend.entities.User;
import tn.staffonlyproject.staffonlyprojectbackend.entities.actors.Encadrant;
import tn.staffonlyproject.staffonlyprojectbackend.repositories.EncadrantRepository;
import tn.staffonlyproject.staffonlyprojectbackend.repositories.UserRepository;
import tn.staffonlyproject.staffonlyprojectbackend.services.EncadrantService;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EncadrantServiceImpl implements EncadrantService {
    private final EncadrantRepository encadrantRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthenticationResponse completeProfile(String email, CompleteProfileStagiareRequest request){ // Step 1: Find Encadrant and validate account status
        Encadrant encadrant = encadrantRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found"));

        if (encadrant.isEnabled()) {
            throw new IllegalStateException("Your account is already enabled. You can sign in.");
        }

        // Step 2: Validate passwords
        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new IllegalStateException("Passwords do not match.");
        }

        // Step 3: Update profile and activate account
        encadrant.setPassword(passwordEncoder.encode(request.newPassword()));
        encadrant.setTechnologies(request.technologies());
        encadrant.setDisponibilite(request.disponibilite());
        encadrant.setEnabled(true);
        encadrantRepository.save(encadrant);

        // Step 4: Authenticate and generate tokens
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        request.newPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var userConnected = (User) auth.getPrincipal();
        claims.put("fullName", userConnected.getFullName());
        var jwtToken = jwtService.generateToken(claims, userConnected);
        var refreshToken = jwtService.generateRefreshToken(userConnected);

        // Step 5: Return tokens
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    }

