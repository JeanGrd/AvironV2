package javaprojetaviron.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {

        Tournoi test = new Tournoi("test","Narbonne", "FS5X+", 25, 25, 2, TypeTournoi.COURSE_LIGNE, null);

        LocalDate date = LocalDate.of(2000, 10, 26);

        Participant p1 = new Participant("Jean", "Marc", date);
        Participant p2 = new Participant("Jean", "Francois", date);
        Participant p3 = new Participant("Jean", "Ana", date);
        Participant p4 = new Participant("Jean", "Covici", date);

        p3.getCurrentAge();

        Embarcation emb1 = new Embarcation("Winner", 5);
        emb1.positionParticipant(1, p1);
        emb1.positionParticipant(2, p2);
        emb1.positionParticipant(3, p3);
        emb1.positionParticipant(4, p4);
        emb1.putBarreur(p1);

        Embarcation emb2 = new Embarcation("Jean", 5);
        emb2.positionParticipant(1, p1);
        emb2.positionParticipant(2, p2);
        emb2.positionParticipant(3, p3);
        emb2.positionParticipant(4, p4);
        emb2.putBarreur(p1);

        Embarcation emb3 = new Embarcation("Ana", 5);
        emb3.positionParticipant(1, p1);
        emb3.positionParticipant(2, p2);
        emb3.positionParticipant(3, p3);
        emb3.positionParticipant(4, p4);
        emb3.putBarreur(p1);

        Embarcation emb4 = new Embarcation("Looser", 5);
        emb4.positionParticipant(1, p1);
        emb4.positionParticipant(2, p2);
        emb4.positionParticipant(3, p3);
        emb4.positionParticipant(4, p4);
        emb4.putBarreur(p1);

        ArrayList<Embarcation> e = new ArrayList<>();

        e.add(emb1);
        e.add(emb2);
        e.add(emb3);
        e.add(emb4);
        e.add(emb1);
        e.add(emb2);
        e.add(emb3);
        e.add(emb4);

        Bracket b = new Bracket(e);

        System.out.println(b.getTournamentsFromRound(1));

        b.running(test);
        b.addWinner(1, emb2);
        b.running(test);
        b.addWinner(1, emb3);
        System.out.println(b.getTournamentsFromRound(1));
        b.running(test);
        b.addWinner(1, emb1);
        b.running(test);
        b.addWinner(1, emb4);
        b.running(test);
        b.addWinner(2, emb2);
        b.showBracket();
        b.running(test);
        b.addWinner(2, emb1);
        b.running(test);
        b.addWinner(3, emb1);
        b.showBracket();

        System.out.println(b.getWinner());

    }
}