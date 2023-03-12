package javaprojetaviron.model;

import java.util.Date;

public class Participant{
    public String prenom;
    public String nom;
    public Date dateNaissance;

    public Participant (String prenom, String nom, Date dateNaissance){
        this.prenom = prenom;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
    }
}