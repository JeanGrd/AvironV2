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
import javafx.stage.Stage;

/**
 *
 * @author PaulineVarin
 */
public abstract class MotherView {
    public abstract void sendNomEquipe(String n) ; 
    
    public void demandeSaisie() {
        Stage popupwindow= new Stage();
        
        TextField nomEquipe = new TextField() ; 
        Label label= new Label("Merci de saisir le nom de l'équipe");
        Button valider = new Button("Valider") ; 
        
        valider.setOnAction(new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent t) {
                System.out.println("HELLO VOUTON");
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
}
