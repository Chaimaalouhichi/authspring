package tn.staffonlyproject.staffonlyprojectbackend.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tn.staffonlyproject.staffonlyprojectbackend.entities.User;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        User userPrincipal = (User) authentication.getPrincipal();

        return Optional.ofNullable(userPrincipal.getId());
    }
}
