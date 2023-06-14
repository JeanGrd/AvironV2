package javaprojetaviron.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe pour gérer les tournois en fonction de la liste des participants et du type de tournoi.
 */
public class BracketV2 {
    // Liste des participants à la compétition
    private ArrayList<Embarcation> participants;
    // Structure de données pour stocker les tournois par round
    private Map<Integer, List<Tournoi>> tournamentsByRound;
    // Index du tournoi en cours
    private int currentTournamentIndex;
    // Round en cours
    private int round;

    /**
     * Constructeur pour la classe BracketV2.
     * @param participants Liste des participants au tournoi
     * @throws Exception si le nombre de participants n'est pas pair
     */
    public BracketV2(ArrayList<Embarcation> participants) throws Exception {
        if(participants.size() % 2 != 0) {
            throw new Exception("Nombre de participants doit être pair");
        }
        this.participants = participants;
        this.tournamentsByRound = new HashMap<>(); // Initialisation de la map des tournois
        this.currentTournamentIndex = 0;
        this.round = 1;
    }

    /**
     * Méthode pour exécuter un tournoi.
     * @param tournoiType Type de tournoi à exécuter
     * @throws Exception si le nombre de participants par tournoi n'est pas de deux
     */
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

        // Réinitialiser si le tour est terminé
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

    /**
     * Méthode pour obtenir la map des tournois par round.
     * @return la map des tournois par round
     */
    public Map<Integer, List<Tournoi>> getTournamentsByRound() {
        return tournamentsByRound;
    }

    /**
     * Méthode pour récupérer les gagnants d'un round spécifique.
     * @param round le round dont les gagnants sont à récupérer
     * @return une liste des gagnants du round spécifié
     */
    private ArrayList<Embarcation> getWinnersFromRound(int round) {
        return new ArrayList<>(tournamentsByRound.get(round).stream().map(Tournoi::getWinner).collect(Collectors.toList()));
    }

    /**
     * Méthode pour vérifier si le tournoi est terminé.
     * @return vrai si le tournoi est terminé, faux sinon
     */
    boolean isFinished() {
        return round > 1 && currentTournamentIndex * 2 >= participants.size();
    }

    /**
     * Méthode pour obtenir la liste des tournois d'un round spécifique.
     * @param round le round dont on veut récupérer les tournois
     * @return une liste des tournois du round spécifié
     */
    public List<Tournoi> getTournamentsFromRound(int round) {
        return tournamentsByRound.getOrDefault(round, new ArrayList<>());
    }

    /**
     * Méthode pour ajouter un gagnant à un tournoi spécifique.
     * @param round le round où le gagnant doit être ajouté
     * @param e l'embarcation gagnante à ajouter
     */
    public void addWinner(int round, Embarcation e) {
        this.tournamentsByRound.get(round).get(currentTournamentIndex-1).addInClassement(this.tournamentsByRound.get(round).get(currentTournamentIndex-1).getMetres(), 0, e);
    }

    /**
     * Méthode pour obtenir le gagnant du tournoi.
     * @return le nom du gagnant si le tournoi est terminé, sinon un message indiquant que le tournoi n'est pas encore terminé
     */
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

    /**
     * Méthode pour afficher les détails du tournoi.
     */
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
