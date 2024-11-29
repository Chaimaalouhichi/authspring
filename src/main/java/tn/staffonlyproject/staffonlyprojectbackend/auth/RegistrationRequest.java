package tn.staffonlyproject.staffonlyprojectbackend.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tn.staffonlyproject.staffonlyprojectbackend.dto.request.AddressRequest;

@Getter
@Setter
@Builder
public class RegistrationRequest {


    @NotEmpty(message = "Firstname is mandatory")
    @NotNull(message = "Firstname is mandatory")
    private String firstname;
    @NotEmpty(message = "Lastname is mandatory")
    @NotNull(message = "Lastname is mandatory")
    private String lastname;
    @Email(message = "Email is not well formatted")
    @NotEmpty(message = "Email is mandatory")
    @NotNull(message = "Email is mandatory")
    private String email;
    @NotEmpty(message = "Password is mandatory")
    @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;
    @Schema(example = """
        {
          "street": "123 Main Street",
          "city": "New York",
          "state": "NY",
          "zipCode": "10001"
        }
        """)
    private AddressRequest address;
}
