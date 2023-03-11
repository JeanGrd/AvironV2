package javaprojetaviron.model ; 
import java.util.ArrayList;

public class Embarcation{
    public String nom;
    public float vitesse;
    public float cadence;
    public ArrayList<Participant> placementParticipant;

    public Embarcation (String nom, int sizeEmbarcation){
        this.nom = nom;
        this.placementParticipant = new ArrayList<>(sizeEmbarcation);
        for (Participant p : this.placementParticipant) {
            // this.placementParticipant.add();
        }
    }

    public void positionnerParticipant (int indice, Participant participant) throws Exception{
        if (placementParticipant.get(indice)!=null) {
            if (indice!=0) {
                placementParticipant.add(indice, participant);
            }
            else{
                throw new Exception("L'indice 0 est la position du barreur");
            }
        }
        else{
            throw new Exception("Il y a deja un autre participant sur la place");
        }
    }

    public void putBarreur(Participant participant){
        placementParticipant.add(0, participant);
    }

    public void removeBarreur(){
        placementParticipant.remove(0);
    }

    public boolean embarcaationIsOk(){
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