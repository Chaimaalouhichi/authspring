package tn.staffonlyproject.staffonlyprojectbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.staffonlyproject.staffonlyprojectbackend.entities.Token;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);
}
