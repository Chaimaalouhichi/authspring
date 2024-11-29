package tn.staffonlyproject.staffonlyprojectbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class IncompleteProfileException extends RuntimeException {
    public IncompleteProfileException(String message) {
        super(message);
    }
}