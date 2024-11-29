package tn.staffonlyproject.staffonlyprojectbackend.entities.actors;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import tn.staffonlyproject.staffonlyprojectbackend.entities.User;

import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("Encadrant")
public class Encadrant extends User {
    private List<String> technologies;
    private int disponibilite;
    private int nbProjetsEncadrees = 0;
    private boolean changeNewPassword;
    private int numberOfSignIn=0;
}
