package tn.staffonlyproject.staffonlyprojectbackend.dto.request;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword,
        String confirmationPassword
) {}
