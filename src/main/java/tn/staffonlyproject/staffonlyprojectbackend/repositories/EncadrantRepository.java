package tn.staffonlyproject.staffonlyprojectbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.staffonlyproject.staffonlyprojectbackend.entities.actors.Encadrant;

import java.util.Optional;

public interface EncadrantRepository extends JpaRepository<Encadrant, Integer> {
    Optional<Encadrant> findByEmail(String email);
}