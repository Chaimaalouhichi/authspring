package tn.staffonlyproject.staffonlyprojectbackend.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {

    @Email(message = "Email is not well formatted")
    @NotEmpty(message = "Email is mandatory")
    @NotNull(message = "Email is mandatory")
    @Schema(example = "admin@gmail.com")
    private String email;

    @NotEmpty(message = "Password is mandatory")
    @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    @Schema(example = "Admin123@")
    private String password;
}
