package tn.staffonlyproject.staffonlyprojectbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.staffonlyproject.staffonlyprojectbackend.entities.actors.Adminisitrateur;

public interface AdminisitrateurRepository extends JpaRepository<Adminisitrateur, Integer> {
}