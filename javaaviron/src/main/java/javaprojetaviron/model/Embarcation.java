package javaprojetaviron.model;

import java.util.ArrayList;

/**
 * Classe représentant une embarcation.
 */
public class Embarcation{
    // Nom de l'embarcation
    private String name;
    // Taille de l'embarcation
    private int sizeEmbarcation;
    // Placement des participants dans l'embarcation
    private ArrayList<Participant> participantPlacement;

    public int getSizeEmbarcation() {
        return sizeEmbarcation;
    }

    public ArrayList<Participant> getParticipantPlacement() {
        return participantPlacement;
    }

    public String getName() {
        return name;
    }

    /**
     * Constructeur pour la classe Embarcation.
     * @param name Nom de l'embarcation
     * @param sizeEmbarcation Taille de l'embarcation
     */
    public Embarcation (String name, int sizeEmbarcation){
        this.name = name;
        this.sizeEmbarcation = sizeEmbarcation;
        this.participantPlacement = new ArrayList<Participant>(this.sizeEmbarcation);
        for (int i = 0; i < this.sizeEmbarcation; i++) {
            participantPlacement.add(null);
        }
    }

    // Méthode pour convertir l'objet Embarcation en une chaîne de caractères
    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * Méthode pour placer un participant à un indice donné dans l'embarcation.
     * @param index L'indice à placer le participant
     * @param participant Le participant à placer
     */
    public void positionParticipant (int index, Participant participant) throws Exception{
        if (index < participantPlacement.size()) {
            if (participantPlacement.get(index) != null) {
                throw new Exception("Il y a déjà un autre participant sur la place");
            } else {
                participantPlacement.set(index, participant);
            }
        } else {
            throw new Exception("L'indice est en dehors de la plage autorisée pour cette liste");
        }
    }

    // Méthodes pour gérer le barreur de l'embarcation
    public void putBarreur(Participant participant){
        participantPlacement.set(0, participant);
    }

    public void removeBarreur(){
        participantPlacement.set(0, null);
    }

    public boolean containsBarreur() {
        return this.participantPlacement.get(0) != null;
    }

    // Méthode pour vérifier si tous les sièges sont occupés, sauf celui du barreur
    public boolean isOk () {
        for (int i = 1; i < this.participantPlacement.size(); i++) {
            if (this.participantPlacement.get(i) == null) {
                return false;
            }
        }
        return true;
    }

    // Méthode pour vérifier si tous les participants sont en dessous de la limite d'âge spécifiée
    public boolean checkAge (int ageLimit) {
        for (Participant participant : this.participantPlacement) {
            if (participant != null) {
                if (participant.getCurrentAge() > ageLimit)
                    return false;
            }
        }
        return true;
    }
}
