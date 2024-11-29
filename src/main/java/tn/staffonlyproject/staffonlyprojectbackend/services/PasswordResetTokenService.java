package tn.staffonlyproject.staffonlyprojectbackend.services;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import tn.staffonlyproject.staffonlyprojectbackend.dto.request.ChangePasswordResetRequest;
import tn.staffonlyproject.staffonlyprojectbackend.dto.response.Response;

public interface PasswordResetTokenService {
    ResponseEntity<Response> verifyEmail(String email) throws MessagingException;

    ResponseEntity<Response> verifyOtp(String otp, String email) throws MessagingException;

    ResponseEntity<Response> changePasswordHandler(
            ChangePasswordResetRequest changePasswordResetRequest,
            String email
    );

}
