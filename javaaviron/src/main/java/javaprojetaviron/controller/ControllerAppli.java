package javaprojetaviron.controller;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javaprojetaviron.model.TypeTournoi;
import javaprojetaviron.model.Tournoi ;   
import javaprojetaviron.view.MotherView;

/**
 *
 * @author PaulineVarin
 */
public class ControllerAppli {
    private Tournoi tounoiC  ;
    private MotherView fenetreCourante ;
    private boolean resReponse = true ;
    private String nomEquipe = "" ; 
    
    public void erreurSaisieAlerte(String raisonAlerte) {
        
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
	alert.setContentText("Erreur dans la saisie : "+ raisonAlerte);
	alert.showAndWait();
    }
 
    public void setNomEquipe(String n) {
        this.nomEquipe = n ;
    }
    
    public void getInformationsToModelTournoi (ArrayList<String> infos) {
        this.resReponse = true ; 
        TypeTournoi typeTournoiC ; 
           
        if(infos.get(3)=="COURSE CONTRE LA MONTRE") {
            typeTournoiC = TypeTournoi.COURSE_CONTRE_LA_MONTRE ; 
        }
        else {
            typeTournoiC = TypeTournoi.COURSE_LIGNE ; 
        }
        
        try {
            this.tounoiC = new Tournoi(infos.get(0), infos.get(1), infos.get(2), Float.parseFloat(infos.get(5)), Float.parseFloat(infos.get(6)), Integer.parseInt(infos.get(4)),typeTournoiC, this) ;   
        }
        catch (Exception e) {
            erreurSaisieAlerte(e.getMessage()) ; 
            this.resReponse = false ; 
        }
    }
    
    public void getInformationsToModelEquipes(ArrayList<String> infos) {
        this.resReponse=true;
        this.tounoiC.creationEmbarcationsWithParticipants(infos);
    }
    
    public void erreurSaisieEquipes(String rep) {
       erreurSaisieAlerte(rep) ; 
       this.resReponse = false ; 
    }
    
    public void lancementTournoi() {
        try{
            this.tounoiC.running();
        }
        catch(Exception e) {
            erreurSaisieAlerte(e.getMessage()) ; 
        }
    }
    
    public String getNomEmbarcationRunning() {
       this.fenetreCourante.demandeSaisie();
       return this.nomEquipe ; 
    }
    
    
    public String getNomTournoi() {
        return this.tounoiC.getNom() ; 
    }
    
    public String getLieuTournoi() {
        return this.tounoiC.getLieu(); 
    }
    
    public float getIntervalle() {
        return this.tounoiC.getIntervalle(); 
    }
    
    
    public int getNbEquipes() {
        return this.tounoiC.getNb_participants() ; 
    }
    
    public int getNbParticipants_parEquipe() {
        return this.tounoiC.getNb_participants_par_embarcation() ; 
    }
    
    public void setVueTournoi(MotherView view) {
        this.fenetreCourante = view;
    } 
    
    public void setModel(Tournoi t) {
        this.tounoiC = t ; 
    } 
    
    public boolean getStatutReponse() {
        return this.resReponse ; 
    }
}
