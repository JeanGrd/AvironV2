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
import javafx.stage.Stage;
import javaprojetaviron.controller.ControllerAppli;

/**
 *
 * @author PaulineVarin
 */
public abstract class MotherView {
    public abstract void sendNomEquipe(String n) ; 
    
    public abstract void majView(ArrayList<String> infos) ; 
    
    public abstract void finTournoi() ; 
    
    public void demandeSaisie() {
        Stage popupwindow= new Stage();
        
        TextField nomEquipe = new TextField() ; 
        Label label= new Label("Merci de saisir le nom de l'équipe");
        Button valider = new Button("Valider") ; 
        
        valider.setOnAction(new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent t) {
                sendNomEquipe(nomEquipe.getText()); 
                popupwindow.close() ;
            }
        });
        
        HBox contenuBox = new HBox() ; 
        contenuBox.setAlignment(Pos.CENTER);
        contenuBox.getChildren().addAll(nomEquipe,valider) ; 
        
        VBox root = new VBox() ; 
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(label,contenuBox) ; 
        
        Scene s = new Scene(root, 300,300) ; 
        popupwindow.setScene(s);
        popupwindow.showAndWait();
 
    }
    
    public void cheminFichier(ControllerAppli c, String contexte) {
        Stage popupwindow= new Stage();
        
        TextField chemin = new TextField() ; 
        TextField nomFichier = new TextField() ; 
        
        Label cheminL= new Label("Merci de saisir le chemin ");
        Label nomFichierL = new Label("Merci de saisir le nom du ficier") ; 
        
        Button valider = new Button("Valider") ; 
        
        valider.setOnAction(new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent t) {
                String cheminFichierString = chemin.getText()+nomFichier.getText()+".csv" ; 
                
                if(contexte.equals("sauvegarde")) {
                    c.enregistrerTournoi(cheminFichierString);
                    System.out.println("SAUVEGARDE");
                } else {
                    c.creationTournoiCSV(cheminFichierString) ; 
                    System.out.println("ENREGISTREMENT");
                }
                
                
                popupwindow.close() ;
            }
        });
        
        HBox cheminBox = new HBox() ; 
        cheminBox.setAlignment(Pos.CENTER);
        cheminBox.getChildren().addAll(cheminL, chemin) ; 
        
        HBox nomBox = new HBox() ; 
        nomBox.setAlignment(Pos.CENTER);
        nomBox.getChildren().addAll(nomFichierL, nomFichier) ; 
        
        VBox root = new VBox() ; 
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(cheminBox, nomBox, valider);
   
        Scene s = new Scene(root, 500,500) ; 
        popupwindow.setScene(s);
        popupwindow.showAndWait();
 
    }
}
