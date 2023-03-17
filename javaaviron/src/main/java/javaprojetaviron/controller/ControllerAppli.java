package javaprojetaviron.controller;
import java.util.ArrayList;
import javafx.scene.control.Alert;
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
    
    public void finTournoiInformation() {
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
	alert.setContentText("Fin du tournoi. Merci de fermer la fenêtre. Vous pouvez avant sauvegarder le résultat");
	alert.showAndWait();
    }
    
    public void enregistrerTournoi(String cheminFichier) {
        System.out.println(cheminFichier);
        this.tounoiC.generateCSV(cheminFichier);
    }
    
    public void creationTournoiCSV(String cheminFichier) {
        try {
            this.tounoiC = Tournoi.readTournoi(cheminFichier, this) ;
        } catch (Exception e) {
            this.erreurSaisieAlerte(e.getMessage()) ; 
        }
         
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
    
    public ArrayList getNomsEquipes() {
        return this.tounoiC.getNomsEmbarcations() ;
    }
    
    public void sendInformationsToView(ArrayList<String> infosClassement) {
        this.fenetreCourante.majView(infosClassement);
    }
    
    public void finTournoi() {
        this.finTournoiInformation() ; 
        this.fenetreCourante.finTournoi();
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
