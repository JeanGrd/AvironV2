package javaprojetaviron.model;

import java.util.ArrayList;

/**
 * Classe représentant une embarcation.
 */
public class Embarcation {
    // Nom de l'embarcation
    private String name;
    // Taille de l'embarcation
    private int sizeEmbarcation;
    // Placement des participants dans l'embarcation
    private ArrayList<Participant> participantPlacement;

    /**
     * Constructeur pour la classe Embarcation.
     *
     * @param name            Nom de l'embarcation.
     * @param sizeEmbarcation Taille de l'embarcation.
     */
    public Embarcation(String name, int sizeEmbarcation) {
        this.name = name;
        this.sizeEmbarcation = sizeEmbarcation;
        this.participantPlacement = new ArrayList<Participant>(this.sizeEmbarcation);
        for (int i = 0; i < this.sizeEmbarcation; i++) {
            participantPlacement.add(null);
        }
    }

    /**
     * Retourne la taille de l'embarcation.
     *
     * @return La taille de l'embarcation.
     */
    public int getSizeEmbarcation() {
        return sizeEmbarcation;
    }

    /**
     * Retourne le placement des participants dans l'embarcation.
     *
     * @return Le placement des participants dans l'embarcation.
     */
    public ArrayList<Participant> getParticipantPlacement() {
        return participantPlacement;
    }

    /**
     * Retourne le nom de l'embarcation.
     *
     * @return Le nom de l'embarcation.
     */
    public String getName() {
        return name;
    }

    /**
     * Méthode pour convertir l'objet Embarcation en une chaîne de caractères.
     *
     * @return La représentation sous forme de chaîne de caractères de l'embarcation.
     */
    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * Méthode pour placer un participant à un indice donné dans l'embarcation.
     *
     * @param index       L'indice à placer le participant.
     * @param participant Le participant à placer.
     * @throws Exception si l'indice est en dehors de la plage autorisée ou s'il y a déjà un autre participant sur la place.
     */
    public void positionParticipant(int index, Participant participant) throws Exception {
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

    /**
     * Méthode pour placer un barreur dans l'embarcation.
     *
     * @param participant Le participant à placer en tant que barreur.
     */
    public void putBarreur(Participant participant) {
        participantPlacement.set(0, participant);
    }

    /**
     * Méthode pour retirer le barreur de l'embarcation.
     */
    public void removeBarreur() {
        participantPlacement.set(0, null);
    }

    /**
     * Vérifie si l'embarcation contient un barreur.
     *
     * @return Vrai si l'embarcation contient un barreur, sinon faux.
     */
    public boolean containsBarreur() {
        return this.participantPlacement.get(0) != null;
    }

    /**
     * Vérifie si tous les sièges de l'embarcation sont occupés, sauf celui du barreur.
     *
     * @return Vrai si tous les sièges sont occupés, sauf celui du barreur, sinon faux.
     */
    public boolean isOk() {
        for (int i = 1; i < this.participantPlacement.size(); i++) {
            if (this.participantPlacement.get(i) == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Vérifie si tous les participants de l'embarcation sont en dessous de la limite d'âge spécifiée.
     *
     * @param ageLimit La limite d'âge spécifiée.
     * @return Vrai si tous les participants sont en dessous de la limite d'âge, sinon faux.
     */
    public boolean checkAge(int ageLimit) {
        for (Participant participant : this.participantPlacement) {
            if (participant != null) {
                if (participant.getCurrentAge() > ageLimit)
                    return false;
            }
        }
        return true;
    }
}
