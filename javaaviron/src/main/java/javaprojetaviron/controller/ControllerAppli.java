package javaprojetaviron.controller;

import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javaprojetaviron.view.CreateTournoiView;

/**
 *
 * @author PaulineVarin
 */
public class ControllerAppli {
    private ArrayList<String> model ; 
    private Scene sceneCourante ;
    
    public void erreurSaisieAlerte() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
	alert.setContentText("Erreur dans la saisie, merci de recommencer");
	alert.showAndWait();
    }

    public void getInformationsToModel (ArrayList<String> infos) {
        System.out.println("Hello controlleur");
        for(int i=0;i<infos.size();i++) {
            System.out.println(infos.get(i));
        }
        model = infos ; 
    }
   
    
    public void listInfos() {
        System.out.println("Hello controlleur");
        for(int i=0;i<model.size();i++) {
            System.out.println(model.get(i));
        }
        
    }

    public void setVueTournoi(Scene view) {
        this.sceneCourante = view;
    } 
}
