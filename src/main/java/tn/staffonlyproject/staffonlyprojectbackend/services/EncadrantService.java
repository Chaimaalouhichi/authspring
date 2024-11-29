package tn.staffonlyproject.staffonlyprojectbackend.services;

import tn.staffonlyproject.staffonlyprojectbackend.auth.AuthenticationResponse;
import tn.staffonlyproject.staffonlyprojectbackend.dto.request.CompleteProfileStagiareRequest;

import java.util.List;

public interface EncadrantService {
    AuthenticationResponse completeProfile(String email, CompleteProfileStagiareRequest request);
}
