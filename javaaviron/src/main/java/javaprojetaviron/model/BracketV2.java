package javaprojetaviron.model;
import java.util.*;
import java.util.stream.Collectors;

public class BracketV2 {
    private ArrayList<Embarcation> participants;
    private Map<Integer, List<Tournoi>> tournamentsByRound; // Map pour conserver les tournois par round
    private int currentTournamentIndex;
    private int round;

    public BracketV2(ArrayList<Embarcation> participants) throws Exception {
        if(participants.size() % 2 != 0) {
            throw new Exception("Nombre de participants doit être pair");
        }
        this.participants = participants;
        this.tournamentsByRound = new HashMap<>(); // Initialisation de la map des tournois
        this.currentTournamentIndex = 0;
        this.round = 1;
    }

    public void running(Tournoi tournoiType) throws Exception {

        // Vérification du nombre de participants
        if(tournoiType.getNb_participants() != 2) {
            throw new Exception("Le tournoi type doit avoir exactement deux participants");
        }

        if (currentTournamentIndex > 0) {
            Tournoi lastTournament = tournamentsByRound.get(round).get(currentTournamentIndex - 1);
            if (lastTournament.getWinner() == null) {
                throw new Exception("Le gagnant du match précédent doit être défini avant de lancer le prochain match");
            }
        }

        // Reset si le tour est terminé
        if (currentTournamentIndex * 2 >= participants.size()) {
            currentTournamentIndex = 0;
            round++;
            participants = getWinnersFromRound(round - 1);
        }

        // Lancer le prochain tournoi
        Tournoi tournament = tournoiType.clone(); // Créer un nouvel objet Tournoi à partir de tournoiType
        tournament.clearConcourrants();
        tournament.addConcourrant(this.participants.get(currentTournamentIndex * 2));
        tournament.addConcourrant(this.participants.get(currentTournamentIndex * 2 + 1));

        tournament.running();

        List<Tournoi> tournamentsThisRound = tournamentsByRound.getOrDefault(round, new ArrayList<>());
        tournamentsThisRound.add(tournament);
        tournamentsByRound.put(round, tournamentsThisRound); // Ajout des tournois de ce tour à la map

        currentTournamentIndex++;

    }

    // Méthode pour récupérer la map des tournois par round
    public Map<Integer, List<Tournoi>> getTournamentsByRound() {
        return tournamentsByRound;
    }

    private ArrayList<Embarcation> getWinnersFromRound(int round) {
        return new ArrayList<>(tournamentsByRound.get(round).stream().map(Tournoi::getWinner).collect(Collectors.toList()));
    }

    boolean isFinished() {
        return round > 1 && currentTournamentIndex * 2 >= participants.size();
    }

    public List<Tournoi> getTournamentsFromRound(int round) {
        return tournamentsByRound.getOrDefault(round, new ArrayList<>());
    }

    public void addWinner(int round, Embarcation e) {
        this.tournamentsByRound.get(round).get(currentTournamentIndex-1).addInClassement(this.tournamentsByRound.get(round).get(currentTournamentIndex-1).getMetres(), 0, e);
    }

    public String getWinner() {
        if(this.isFinished()) {
            // Récupérer les tournois du dernier round terminé
            List<Tournoi> lastRoundTournaments = getTournamentsByRound().get(round - 1);

            // Vérifier si des tournois existent pour ce round
            if(lastRoundTournaments != null && !lastRoundTournaments.isEmpty()) {
                // Récupérer le dernier tournoi de la liste
                Tournoi lastTournament = lastRoundTournaments.get(lastRoundTournaments.size() - 1);

                // Récupérer et retourner le nom du gagnant du dernier tournoi
                return lastTournament.getWinner().toString();
            }
        }

        return "Tournament non terminé";
    }

    public void showBracket() {
        // Parcours de tous les rounds
        for (int i = 1; i <= round; i++) {
            System.out.println("Round " + i + ":");

            // Récupérer tous les tournois pour ce round
            List<Tournoi> tournaments = getTournamentsFromRound(i);

            // Parcours de tous les tournois
            for (int j = 0; j < tournaments.size(); j++) {
                Tournoi tournament = tournaments.get(j);

                // Afficher les détails du tournoi
                System.out.println("  Tournament " + (j + 1) + ":");

                // Afficher les participants
                List<Embarcation> embarcations = tournament.getConcourrants();
                System.out.println("    Participants: " + embarcations.stream().map(Embarcation::toString).collect(Collectors.joining(", ")));

                // Afficher le gagnant
                Embarcation winner = tournament.getWinner();
                if (winner != null) {
                    System.out.println("    Winner: " + winner.toString());
                } else {
                    System.out.println("    No winner yet");
                }
            }
        }

        // Si le bracket est terminé, afficher le vainqueur final
        if (this.isFinished()) {
            System.out.println("The tournament is finished!");
            System.out.println("The winner is: " + getWinner());
        } else {
            System.out.println("The tournament is not finished yet.");
        }
    }



}
