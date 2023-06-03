/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaprojetaviron.view;

import java.util.ArrayList;
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
    private TextField[] labelsTableau ;
    private TextField[] classement ; 
    private TextField[] position ; 
    
    
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
            }
            
        });
   
        //Mise en place de la scene haute
        this.rootInfosTournoi.setAlignment(Pos.CENTER);
        this.rootInfosTournoi.getChildren().addAll(this.nomTournoi,this.lieuTournoi ,this.demarrerT) ; 
        
        //Mise en place de la scene basse
        this.labelsTableau = new TextField[2] ;
        String labelsString[]=new String[]{
            "Nom équipe",
            "Position",
            }; 
        
        for(int i = 0; i < this.labelsTableau.length; i++) {
            this.labelsTableau[i]=new TextField(labelsString[i]);
            this.labelsTableau[i].setEditable(false);
            this.labelsTableau[i].setStyle("-fx-background-color: grey;");
            this.labelsTableau[i].setAlignment(Pos.CENTER);
        }
        
        HBox tableauNomsColonnes = new HBox() ; 
        tableauNomsColonnes.setAlignment(Pos.CENTER);
        tableauNomsColonnes.getChildren().addAll(labelsTableau);
        
      
        this.rootInfosClassement.setAlignment(Pos.CENTER);
        this.rootInfosClassement.getChildren().add(tableauNomsColonnes) ; 
        
        ArrayList<String> noms = this.controlleurVue.getNomsEquipes() ; 
        
        this.classement = new TextField[this.controlleurVue.getNbEquipes()] ;
        this.position = new TextField[this.controlleurVue.getNbEquipes()] ;
        
        for(int i=0;i<classement.length;i++) {
            HBox classementEmbarcation = new HBox() ; 
            classementEmbarcation.setAlignment(Pos.CENTER);
            
            classement[i] = new TextField(noms.get(i)) ; 
            classement[i].setEditable(false);
            classement[i].setAlignment(Pos.CENTER);

            position[i] = new TextField("0") ; 
            position[i].setEditable(false);
            position[i].setAlignment(Pos.CENTER);
            
            classementEmbarcation.getChildren().addAll(classement[i],position[i] ) ; 
            rootInfosClassement.getChildren().addAll(classementEmbarcation) ; 
        }
        
        //Creation de la scene principale  
        this.root.setAlignment(Pos.CENTER);
        this.root.getChildren().addAll(rootInfosTournoi, rootInfosClassement) ; 
        Scene scene = new Scene(root, 800,600); 
        
        //Liaison de la scene et du controleur
        this.controlleurVue.setVueTournoi(this);
        
        //Test du bon fonctionnement
        return scene ; 
    }

    @Override
    public void sendNomEquipe(String n) {
        this.controlleurVue.setNomEquipe(n);
    }

    @Override
    public void majView(ArrayList<String> infos) {
        for(int i=0;i<infos.size();i++) {
            String[] res = infos.get(i).split("-") ; 
            classement[i].setText(res[1]);
            position[i].setText(res[0]);
        }
    }

    @Override
    public void finTournoi() {
        this.enregistrer.setOnAction(new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent t) {
                cheminFichier(controlleurVue,"sauvegarde") ;   
            }
            
        });
        this.root.getChildren().add(this.enregistrer) ; 
    }

}
