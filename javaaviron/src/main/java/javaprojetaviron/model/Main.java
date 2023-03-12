package javaprojetaviron.model;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        // CREAM IMBARCATIONEA
        Embarcation embarcation = new Embarcation("Embarcation1", 5);
        System.out.println("Embarcation créée : " + embarcation.nom);

        // ADAUGAM UN BARREUR
        Participant barreur = new Participant("John", "Doe", new Date(1990, 5, 15));
        embarcation.putBarreur(barreur);
        System.out.println("Barreur ajouté : " + barreur.prenom + " " + barreur.nom);

        // ADAUGAM PARTICIPANTI
        Participant participant1 = new Participant("Jane", "Doe", new Date(1992, 8, 25));
        try {
            embarcation.positionnerParticipant(1, participant1);
            System.out.println("Participant ajouté : " + participant1.prenom + " " + participant1.nom);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Participant participant2 = new Participant("Bob", "Smith", new Date(1988, 4, 10));
        try {
            embarcation.positionnerParticipant(2, participant2);
            System.out.println("Participant ajouté : " + participant2.prenom + " " + participant2.nom);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        Participant participant3 = new Participant("Alice", "Johnson", new Date(1995, 11, 20));
        try {
            embarcation.positionnerParticipant(3, participant3);
            System.out.println("Participant ajouté : " + participant3.prenom + " " + participant3.nom);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Participant participant4 = new Participant("Tom", "Wilson", new Date(1991, 7, 5));
        try {
            embarcation.positionnerParticipant(4, participant4);
            System.out.println("Participant ajouté : " + participant4.prenom + " " + participant4.nom);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //VERIFICAM IF IS OK
        boolean isOk = embarcation.embarcationIsOk();
        System.out.println("L'embarcation est valide : " + isOk);



        //AICI URMEAZA SA TESTAM CRONOMETRUL!!!

        Chronometre chronometre = new Chronometre();
        float temps_limite = 10.0f; // temps limite en secondes

        System.out.println("Début du chronomètre.");
        chronometre.running(temps_limite);

        try {
            Thread.sleep((long) (temps_limite * 1000)); // Attendre le temps limite
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Fin du chronomètre.");
        System.out.println("Temps écoulé: " + chronometre.getTemps() + " secondes.");
    }
}
