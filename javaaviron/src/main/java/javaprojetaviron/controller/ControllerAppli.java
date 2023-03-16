package javaprojetaviron.controller;
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javaprojetaviron.model.TypeTournoi;
import javaprojetaviron.model.Tournoi ;   

/**
 *
 * @author PaulineVarin
 */
public class ControllerAppli {
    private Tournoi tounoiC  ;
    private Scene sceneCourante ;
    private boolean resReponse = true ;
    
    public void erreurSaisieAlerte(String raisonAlerte) {
        
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
	alert.setContentText("Erreur dans la saisie : "+ raisonAlerte);
	alert.showAndWait();
    }

    public void getInformationsToModelTournoi (ArrayList<String> infos) {
        TypeTournoi typeTournoiC ; 
                
        if(infos.get(3)=="COURSE CONTRE LA MONTRE") {
            typeTournoiC = TypeTournoi.COURSE_CONTRE_LA_MONTRE ; 
        }
        else {
            typeTournoiC = TypeTournoi.COURSE_LIGNE ; 
        }
        
        try {
            this.tounoiC = new Tournoi(infos.get(0), infos.get(1), infos.get(2), Float.parseFloat(infos.get(5)), Float.parseFloat(infos.get(6)), Integer.parseInt(infos.get(4)),typeTournoiC) ;   
        }
        catch (Exception e) {
            erreurSaisieAlerte("Mauvais code") ; 
            this.resReponse = false ; 
        }
    }
    
    public void getInformationsToModelEquipes(ArrayList<String> infos) {
        System.out.println("Heelo") ; 
        this.tounoiC.creationEmbarcationsWithParticipants(infos);
        
    }
    
    
    public int getNbEquipes() {
        return this.tounoiC.getNb_participants() ; 
    }
    
    public int getNbParticipants_parEquipe() {
        return this.tounoiC.getNb_participants_par_embarcation() ; 
    }
    
 
    
   
    public void setVueTournoi(Scene view) {
        this.sceneCourante = view;
    } 
    
    public void setModel(Tournoi t) {
        this.tounoiC = t ; 
    } 
    
    public boolean getStatutReponse() {
        return this.resReponse ; 
    }
}
