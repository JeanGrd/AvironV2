package javaprojetaviron.model;

import java.util.Date;

public class Main {
    public static void main(String[] args) throws Exception {

        Tournoi test = new Tournoi("test","Narbonne", "FM4X+", 75,5, 4, TypeTournoi.COURSE_LIGNE);

        Date date = new Date();

        Participant p1 = new Participant("Jean", "Marc", date);
        Participant p2 = new Participant("Jean", "Francois", date);
        Participant p3 = new Participant("Jean", "Ana", date);
        Participant p4 = new Participant("Jean", "Covici", date);

        Embarcation emb1 = new Embarcation("Winner", 4);
        emb1.positionnerParticipant(1, p1);
        emb1.positionnerParticipant(2, p2);
        emb1.positionnerParticipant(3, p3);
        emb1.positionnerParticipant(4, p4);
        emb1.putBarreur(p1);

        Embarcation emb2 = new Embarcation("Jean", 4);
        emb2.positionnerParticipant(1, p1);
        emb2.positionnerParticipant(2, p2);
        emb2.positionnerParticipant(3, p3);
        emb2.positionnerParticipant(4, p4);
        emb2.putBarreur(p1);

        Embarcation emb3 = new Embarcation("Ana", 4);
        emb3.positionnerParticipant(1, p1);
        emb3.positionnerParticipant(2, p2);
        emb3.positionnerParticipant(3, p3);
        emb3.positionnerParticipant(4, p4);
        emb3.putBarreur(p1);

        Embarcation emb4 = new Embarcation("Looser", 4);
        emb4.positionnerParticipant(1, p1);
        emb4.positionnerParticipant(2, p2);
        emb4.positionnerParticipant(3, p3);
        emb4.positionnerParticipant(4, p4);
        emb4.putBarreur(p1);

        test.addConcourrant(emb1);
        test.addConcourrant(emb2);
        test.addConcourrant(emb3);
        test.addConcourrant(emb4);

        System.out.println(test.isOk());

        test.running(25);

        test.showClassement(25);
        test.showClassement(50);

        test.addInClassement(75, 0, emb3);
        test.addInClassement(75, 0, emb2);

        test.showClassement(75);

    }
}