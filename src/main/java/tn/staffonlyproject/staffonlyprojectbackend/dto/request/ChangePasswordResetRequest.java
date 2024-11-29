package tn.staffonlyproject.staffonlyprojectbackend.dto.request;

public record ChangePasswordResetRequest(
       String newPassword,
       String confirmationPassword
) {
}
