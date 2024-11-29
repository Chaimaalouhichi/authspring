package tn.staffonlyproject.staffonlyprojectbackend.dto.request;

import java.util.List;

public record CompleteProfileStagiareRequest(
        List<String> technologies,
        Integer disponibilite,
        String newPassword,
        String confirmationPassword
) {
}
