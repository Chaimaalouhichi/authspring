package tn.staffonlyproject.staffonlyprojectbackend.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.staffonlyproject.staffonlyprojectbackend.config.security.JwtService;
import tn.staffonlyproject.staffonlyprojectbackend.dto.AddressMapper;
import tn.staffonlyproject.staffonlyprojectbackend.entities.Role;
import tn.staffonlyproject.staffonlyprojectbackend.entities.Token;
import tn.staffonlyproject.staffonlyprojectbackend.entities.User;
import tn.staffonlyproject.staffonlyprojectbackend.entities.actors.Adminisitrateur;
import tn.staffonlyproject.staffonlyprojectbackend.entities.actors.Encadrant;
import tn.staffonlyproject.staffonlyprojectbackend.entities.actors.Stagiaire;
import tn.staffonlyproject.staffonlyprojectbackend.exception.EmailAlreadyExistsException;
import tn.staffonlyproject.staffonlyprojectbackend.exception.IncompleteProfileException;
import tn.staffonlyproject.staffonlyprojectbackend.repositories.*;
import tn.staffonlyproject.staffonlyprojectbackend.util.TokenService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final StagiaireRepository stagiaireRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;
    private final AdminisitrateurRepository adminisitrateurRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final EncadrantRepository encadrantRepository;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;
    public static final String STAGIAIREROLE = "STAGIAIRE";


    @Transactional
    public void registerStagiaire(RegistrationRequest request) throws MessagingException {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email " + request.getEmail() + " is already registered");
        }
        var userRole = roleRepository.findByName(STAGIAIREROLE)
                .orElseThrow(() -> new IllegalStateException("ROLE STAGIAIRE was not initiated"));
        var savedAddress = addressRepository.save(addressMapper.toAddress(request.getAddress()));
        var user = Stagiaire.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(savedAddress)
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        stagiaireRepository.save(user);
        tokenService.sendValidationEmail(user);
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(request.getEmail()+"not found"));


        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ENCADRANT"))) {
            Encadrant encadrant = (Encadrant) user;
            if (!encadrant.isEnabled()) {

                throw new IncompleteProfileException("Please complete your profile first");

            }
        }
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var userConnected = ((User) auth.getPrincipal());
        claims.put("fullName", userConnected.getFullName());
        var jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());
        var refreshToken = jwtService.generateRefreshToken(userConnected);



        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @PostConstruct
    public void createRoles(){

        if (roleRepository.findByName("ADMIN").isEmpty()) {
            roleRepository.save(Role.builder().name("ADMIN").build());
        }
        if (roleRepository.findByName(STAGIAIREROLE).isEmpty()) {
            roleRepository.save(Role.builder().name(STAGIAIREROLE).build());
        }
        if (roleRepository.findByName("ENCADRANT").isEmpty()) {
            roleRepository.save(Role.builder().name("ENCADRANT").build());
        }
    }

    @PostConstruct
    public void createSuperAdmin(){

        var userRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));

        String email = "admin@gmail.com";
        if (!userRepository.existsByEmail(email)) {

            var user = Adminisitrateur.builder()
                    .firstname("admin")
                    .lastname("admin")
                    .email(email)
                    .password(passwordEncoder.encode("Admin123@"))
                    .accountLocked(false)
                    .enabled(true)
                    .roles(List.of(userRole))
                    .build();
            adminisitrateurRepository.save(user);
        }

    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            tokenService.sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }






}
