package tn.staffonlyproject.staffonlyprojectbackend.services.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.staffonlyproject.staffonlyprojectbackend.auth.RegistrationRequest;
import tn.staffonlyproject.staffonlyprojectbackend.dto.AddressMapper;
import tn.staffonlyproject.staffonlyprojectbackend.dto.request.ChangePasswordRequest;
import tn.staffonlyproject.staffonlyprojectbackend.entities.User;
import tn.staffonlyproject.staffonlyprojectbackend.entities.actors.Encadrant;
import tn.staffonlyproject.staffonlyprojectbackend.exception.EmailAlreadyExistsException;
import tn.staffonlyproject.staffonlyprojectbackend.repositories.*;
import tn.staffonlyproject.staffonlyprojectbackend.services.UserService;
import tn.staffonlyproject.staffonlyprojectbackend.util.TokenService;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EncadrantRepository encadrantRepository;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.newPassword()));

        // save the new password
        userRepository.save(user);
    }

    @Override
    public void createEncadrant(RegistrationRequest request) throws MessagingException {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email " + request.getEmail() + " is already registered");
        }
        var userRole = roleRepository.findByName("Encadrant")
                .orElseThrow(() -> new IllegalStateException("ROLE Encadrant was not initiated"));
        var savedAddress = addressRepository.save(addressMapper.toAddress(request.getAddress()));
        var user = Encadrant.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(savedAddress)
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        encadrantRepository.save(user);
        tokenService.sendActivation(user, request.getPassword());
    }
}
