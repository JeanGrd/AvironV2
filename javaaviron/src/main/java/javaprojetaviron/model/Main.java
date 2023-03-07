import java.util.Date;

public class Main{

    public static void main(String[] args) throws Exception {
        Participant p = new Participant("Andrei", "Vasilescu", new Date(2016, 11, 18));
        Participant p1 = new Participant("Ion", "Ionescu", new Date(2016, 11, 18));
        Participant p2 = new Participant("Ivan", "Andreivitch", new Date(2016, 11, 18));

        Embarcation emb = new Embarcation("Test", 3);

        emb.positionnerParticipant(1, p);

        System.out.println();
    }
}