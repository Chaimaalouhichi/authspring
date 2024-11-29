package tn.staffonlyproject.staffonlyprojectbackend.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/registerStagiaire")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            description = "Register Stagiaire endpoint",
            summary = "This is for register Stagiaire endpoint"

    )
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        service.registerStagiaire(request);
        return ResponseEntity.accepted().build();
    }

    @Operation(
            description = "Stagiaire Activation account endpoint",
            summary = "This is for activate Stagiaire account endpoint"

    )
    @GetMapping("/activate-account")
    public void confirm(
            @RequestParam String token
    ) throws MessagingException {
        service.activateAccount(token);
    }

    @Operation(
            description = "Sign In endpoint",
            summary = "This is for Sign In endpoint"

    )

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @Operation(
            description = "Refresh Token endpoint",
            summary = "This is for Refresh Token endpoint"

    )

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }
}
