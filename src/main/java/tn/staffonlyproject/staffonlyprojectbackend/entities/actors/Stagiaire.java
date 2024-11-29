package tn.staffonlyproject.staffonlyprojectbackend.entities.actors;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import tn.staffonlyproject.staffonlyprojectbackend.entities.User;
@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("Stagiaire")
public class Stagiaire extends User {
    private String niveau;
    private String etablissement;
}
