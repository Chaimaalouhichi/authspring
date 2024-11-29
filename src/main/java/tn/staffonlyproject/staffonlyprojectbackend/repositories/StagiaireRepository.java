package tn.staffonlyproject.staffonlyprojectbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.staffonlyproject.staffonlyprojectbackend.entities.actors.Stagiaire;

public interface StagiaireRepository extends JpaRepository<Stagiaire, Integer> {
}