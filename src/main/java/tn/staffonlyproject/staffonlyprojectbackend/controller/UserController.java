package tn.staffonlyproject.staffonlyprojectbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.staffonlyproject.staffonlyprojectbackend.auth.RegistrationRequest;
import tn.staffonlyproject.staffonlyprojectbackend.dto.request.ChangePasswordRequest;
import tn.staffonlyproject.staffonlyprojectbackend.services.UserService;

import java.security.Principal;
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Services (Change Password + Create Encadrant)")
public class UserController {
    private final UserService userService;

    @PatchMapping("/updatePassword")
    @Operation(
            description = "updatePassword endpoint",
            summary = "This is to update password endpoint"
    )
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/createEncadrant")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            description = "create Encadrant endpoint",
            summary = "This is to create Encadrant endpoint by Administrateur"
    )
    public ResponseEntity<?> createEncadrant(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        userService.createEncadrant(request);
        return ResponseEntity.accepted().build();
    }
}
