package tn.staffonlyproject.staffonlyprojectbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.staffonlyproject.staffonlyprojectbackend.entities.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String role);
}
