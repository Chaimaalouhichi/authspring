package tn.staffonlyproject.staffonlyprojectbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import tn.staffonlyproject.staffonlyprojectbackend.auth.AuthenticationResponse;
import tn.staffonlyproject.staffonlyprojectbackend.dto.request.CompleteProfileStagiareRequest;
import tn.staffonlyproject.staffonlyprojectbackend.exception.IncompleteProfileException;
import tn.staffonlyproject.staffonlyprojectbackend.services.EncadrantService;

@RestController
@RequestMapping("/api/encadrant")
@Tag(name = "Encadrant Controller")
public class EncadrantController {

    private final EncadrantService encadrantService;

    public EncadrantController(EncadrantService encadrantService) {
        this.encadrantService = encadrantService;
    }

    @PostMapping("/complete-profile")
    @Operation(
            description = "Complete Profile Encadrant endpoint",
            summary = "This is to complete profile encadrant endpoint"
    )
    public ResponseEntity<?> completeProfile(@RequestParam String email,
                                             @RequestBody @Valid CompleteProfileStagiareRequest request) {
        try {
            AuthenticationResponse response = encadrantService.completeProfile(email, request);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalStateException | IncompleteProfileException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
