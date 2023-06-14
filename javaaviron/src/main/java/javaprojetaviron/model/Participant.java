package javaprojetaviron.model;

import java.time.LocalDate;
import java.time.Period;

/**
 * Classe représentant un participant dans le système.
 */
public class Participant {
    private String firstName;
    private String lastName;
    public LocalDate birthDate;

    /**
     * Constructeur pour la classe Participant.
     * @param firstName Le prénom du participant
     * @param lastName Le nom de famille du participant
     * @param birthDate La date de naissance du participant
     */
    public Participant (String firstName, String lastName, LocalDate birthDate){
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Méthode pour obtenir l'âge actuel du participant.
     * @return L'âge du participant en années
     */
    public int getCurrentAge(){
        // Obtenir la date actuelle
        LocalDate today = LocalDate.now();

        // Calculer l'âge en années
        Period age = Period.between(this.birthDate, today);

        return age.getYears();
    }
}
