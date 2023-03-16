package javaprojetaviron.model;

import java.util.ArrayList;

public class Embarcation{
    private String nom;
    private float vitesse;
    private float cadence;
    private int sizeEmbarcation;
    private ArrayList<Participant> placementParticipant;

    public float getVitesse() {
        return vitesse;
    }

    public float getCadence() {
        return cadence;
    }

    public int getSizeEmbarcation() {
        return sizeEmbarcation - 1;
    }

    public ArrayList<Participant> getPlacementParticipant() {
        return placementParticipant;
    }

    public String getNom() {
        return nom;
    }

    public Embarcation (String nom, int sizeEmbarcation){
        this.nom = nom;
        this.sizeEmbarcation = sizeEmbarcation + 1;
        this.placementParticipant = new ArrayList<Participant>(this.sizeEmbarcation);
        for (int i = 0; i < this.sizeEmbarcation; i++) {
            placementParticipant.add(null);
        }
    }

    @Override
    public String toString() {
        return this.getNom();
    }

    public void positionnerParticipant (int indice, Participant participant) throws Exception{
        if (indice == 0) {
            throw new Exception("L'indice 0 est la position du barreur");
        }
        if (indice < placementParticipant.size()) {
            if (placementParticipant.get(indice) != null) {
                throw new Exception("Il y a déjà un autre participant sur la place");
            } else {
                placementParticipant.set(indice, participant);
            }
        } else {
            throw new Exception("L'indice est en dehors de la plage autorisée pour cette liste");
        }
    }

    public void putBarreur(Participant participant){
        placementParticipant.set(0, participant);
    }

    public void removeBarreur(){
        placementParticipant.set(0, null);
    }

    public boolean containsBarreur() {
        return this.placementParticipant.get(0) != null;
    }

    public boolean isOk () {
        for (int i = 1; i < this.placementParticipant.size(); i++) {
            if (this.placementParticipant.get(i) == null) {
                return false;
            }
        }
        return true;
    }

    public boolean checkAge (int age_limite) {
        for (Participant participant : this.placementParticipant) {
            if (participant != null) {
                if (participant.getCurrentAge() > age_limite)
                    return false;
            }
        }
        return true;
    }

}
