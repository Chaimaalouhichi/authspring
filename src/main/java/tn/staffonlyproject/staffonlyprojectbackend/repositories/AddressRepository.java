package tn.staffonlyproject.staffonlyprojectbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.staffonlyproject.staffonlyprojectbackend.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}