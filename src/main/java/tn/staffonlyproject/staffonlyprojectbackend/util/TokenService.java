package tn.staffonlyproject.staffonlyprojectbackend.util;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.staffonlyproject.staffonlyprojectbackend.email.EmailService;
import tn.staffonlyproject.staffonlyprojectbackend.email.EmailTemplateName;
import tn.staffonlyproject.staffonlyprojectbackend.entities.Token;
import tn.staffonlyproject.staffonlyprojectbackend.entities.User;
import tn.staffonlyproject.staffonlyprojectbackend.repositories.TokenRepository;
import tn.staffonlyproject.staffonlyprojectbackend.repositories.UserRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;



    public String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode();
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }

    public void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }
    public void sendActivation(User user,String password) throws MessagingException {

        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ADD_ENCADRANT,
                activationUrl,
                password,
                "Account activation"
        );
    }

    public String generateActivationCode() {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < 6; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
}

