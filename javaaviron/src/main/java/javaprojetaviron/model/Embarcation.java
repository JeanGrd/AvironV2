package javaprojetaviron.model;

import java.util.ArrayList;

public class Embarcation{
    public String nom;
    public float vitesse;
    public float cadence;
    public ArrayList<Participant> placementParticipant;

    public Embarcation (String nom, int sizeEmbarcation){
        this.nom = nom;
        this.placementParticipant = new ArrayList<Participant>(sizeEmbarcation);
        for (int i = 0; i < sizeEmbarcation; i++) {
            placementParticipant.add(null);
        }
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

    public boolean embarcationIsOk(){
        for(Participant p : placementParticipant)
        {
            if(p == null)
            {
                return false;
            }
        }
        return true;
    }
}
