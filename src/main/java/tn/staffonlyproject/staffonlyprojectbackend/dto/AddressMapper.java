package tn.staffonlyproject.staffonlyprojectbackend.dto;

import org.springframework.stereotype.Service;
import tn.staffonlyproject.staffonlyprojectbackend.dto.request.AddressRequest;
import tn.staffonlyproject.staffonlyprojectbackend.entities.Address;

import java.awt.print.Book;

@Service
public class AddressMapper {

        public Address toAddress(AddressRequest request) {
            return Address.builder()
                    .state(request.state())
                    .city(request.city())
                    .zipCode(request.zipCode())
                    .street(request.street())
                    .build();
        }


}
