package javaprojetaviron.model;

import java.time.LocalDate;
import java.time.Period;

/**
 * Classe représentant un participant dans le système.
 */
public class Participant {
    // Prénom du participant
    private String firstName;
    // Nom de famille du participant
    private String lastName;
    // Date de naissance du participant
    public LocalDate birthDate;

    /**
     * Constructeur pour la classe Participant.
     *
     * @param firstName  Le prénom du participant.
     * @param lastName   Le nom de famille du participant.
     * @param birthDate  La date de naissance du participant.
     */
    public Participant(String firstName, String lastName, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    /**
     * Retourne le prénom du participant.
     *
     * @return Le prénom du participant.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Retourne le nom de famille du participant.
     *
     * @return Le nom de famille du participant.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Retourne la date de naissance du participant.
     *
     * @return La date de naissance du participant.
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Méthode pour obtenir l'âge actuel du participant.
     *
     * @return L'âge du participant en années.
     */
    public int getCurrentAge() {
        // Obtenir la date actuelle
        LocalDate today = LocalDate.now();

        // Calculer l'âge en années
        Period age = Period.between(this.birthDate, today);

        return age.getYears();
    }
}
