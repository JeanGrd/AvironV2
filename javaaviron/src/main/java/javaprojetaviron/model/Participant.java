package javaprojetaviron.model;

import java.time.LocalDate;
import java.time.Period;

public class Participant{
    private String prenom;
    private String nom;
    private  Sexe sexe;
    public LocalDate dateNaissance;

    public Participant (String prenom, String nom, Sexe sexe, LocalDate dateNaissance){
        this.prenom = prenom;
        this.nom = nom;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public int getCurrentAge(){

        // Obtenir la date actuelle
        LocalDate today = LocalDate.now();

        // Calculer l'âge en années
        Period age = Period.between(this.dateNaissance, today);

        return age.getYears();

    }

}