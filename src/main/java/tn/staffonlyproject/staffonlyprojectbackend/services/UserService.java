package tn.staffonlyproject.staffonlyprojectbackend.services;


import jakarta.mail.MessagingException;
import tn.staffonlyproject.staffonlyprojectbackend.auth.RegistrationRequest;
import tn.staffonlyproject.staffonlyprojectbackend.dto.request.ChangePasswordRequest;

import java.security.Principal;

public interface UserService {
    void changePassword(ChangePasswordRequest request, Principal connectedUser);
    void createEncadrant(RegistrationRequest request) throws MessagingException;
}
