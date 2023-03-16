/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaprojetaviron.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javaprojetaviron.controller.ControllerAppli;

/**
 *
 * @author PaulineVarin
 */
public class TournoiView extends MotherView{
    private ControllerAppli controlleurVue ;

    //Labels
    private Label nomTournoi = new Label() ;
    private Label lieuTournoi = new Label() ; 
    private Label intervalle = new Label() ; 
    
    //Bouttons
    private Button demarrerT = new Button("Démarrer") ;
    private Button enregistrer = new Button("Enregistrer") ;
    
    
    //Scenes
    private VBox rootInfosTournoi = new VBox(10) ; 
    private VBox rootInfosClassement = new VBox(10) ;
    private VBox root = new VBox(10) ;
    
    public void setControlleurVue(ControllerAppli c) {
        this.controlleurVue = c;
    }
    
    public String getNomEmbarcation() {
        return "" ; 
    }
    
    
    public Scene creationScene() {
        
        //Mise en place des labels
        this.nomTournoi.setText(controlleurVue.getNomTournoi());
        this.lieuTournoi.setText(controlleurVue.getLieuTournoi()) ;
        this.intervalle.setText("Intervalle : "+controlleurVue.getIntervalle()) ; 
        
        //Mise en place des boutons
        this.demarrerT.setOnAction(new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent t) {
                controlleurVue.lancementTournoi() ; 
                System.out.println("Hello");
            }
            
        });
        
        
        this.enregistrer.setOnAction(new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent t) {
            }
            
        });
        
        //Mise en place de la scene haute
        this.rootInfosTournoi.setAlignment(Pos.CENTER);
        this.rootInfosTournoi.getChildren().addAll(this.nomTournoi,this.lieuTournoi ,this.demarrerT) ; 
        
        //Mise en place de la scene basse
        this.rootInfosClassement.setAlignment(Pos.CENTER);
        
        
        
        //Creation de la scene principale  
        this.root.setAlignment(Pos.CENTER);
        this.root.getChildren().addAll(rootInfosTournoi, rootInfosClassement) ; 
        Scene scene = new Scene(root, 1000,600); 
        
        //Liaison de la scene et du controleur
        this.controlleurVue.setVueTournoi(this);
        
        //Test du bon fonctionnement
        return scene ; 
    }

    @Override
    public void sendNomEquipe(String n) {
        this.controlleurVue.setNomEquipe(n);
    }

}
