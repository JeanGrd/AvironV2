package javaprojetaviron.model;
import java.util.*;

public class Bracket {
    private ArrayList<Embarcation> participants;
    private Map<Integer, List<Tournoi>> tournamentsByRound; // Map pour conserver les tournois par round

    public Bracket(ArrayList<Embarcation> participants) throws Exception {
        if(participants.size() % 2 != 0) {
            throw new Exception("Nombre de participants doit être pair");
        }
        this.participants = participants;
        this.tournamentsByRound = new HashMap<>(); // Initialisation de la map des tournois
    }

    public void running(Tournoi tournoiType) throws Exception {

        if(tournoiType.getNb_participants() != 2) {
            throw new Exception("Le tournoi type doit avoir exactement deux participants");
        }

        int round = 1;
        while (this.participants.size() > 1) {
            ArrayList<Embarcation> winners = new ArrayList<>();
            List<Tournoi> tournamentsThisRound = new ArrayList<>();
            for (int i = 0; i < this.participants.size(); i += 2) {
                tournoiType.clearConcourrants();
                tournoiType.addConcourrant(this.participants.get(i));
                tournoiType.addConcourrant(this.participants.get(i + 1));

                tournoiType.running();

                // Il faudra mettre une boucle qui attend qu'on lui donne le gagnant du tour
                tournoiType.addInClassement(tournoiType.getMetres(), 0, this.participants.get(i));

                Embarcation winner = tournoiType.getWinner();
                winners.add(winner);

                tournamentsThisRound.add(tournoiType);
            }
            this.tournamentsByRound.put(round, tournamentsThisRound); // Ajoute les tournois de ce tour à la map
            this.participants = winners;
            round++; // Passe au tour suivant
        }
        System.out.print(this.participants.get(0));
    }

    public List<Embarcation> getParticipants() {
        return participants;
    }

    // Méthode pour récupérer la map des tournois par round
    public Map<Integer, List<Tournoi>> getTournamentsByRound() {
        return tournamentsByRound;
    }
}
