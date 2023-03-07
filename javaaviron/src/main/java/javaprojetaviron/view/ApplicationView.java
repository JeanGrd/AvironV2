/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaprojetaviron.view;

/**
 *
 * @author PaulineVarin
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ApplicationView extends Application{
    
    @Override
    public void start(Stage primaryStage) {
        HomeView homescene = new HomeView() ; 
        primaryStage.setScene(homescene.creationScene());
        primaryStage.show();
        
        
        
        /*
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Button b = new Button("Hello") ; 
        b.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Scene s1 = ((Button)e.getSource()).getScene() ; 
                Stage st1 =  (Stage) s1.getWindow() ; 
                
                
                //Creation d'une nouvelle scene + rajout à la fenetre de base
                VBox root = new VBox(10) ; 
                root.setAlignment(Pos.CENTER);
                Label l = new Label("DEUXIEME SCENE");
                root.getChildren().add(l) ; 
                Scene s2 = new Scene(root,600,400) ; 
                st1.setScene(s2);
                st1.show(); ; 
                
            }
        });
        
       
       
        primaryStage.setTitle("Accueil");
        VBox root = new VBox(10) ; 
        root.setAlignment(Pos.CENTER);
        //StackPane root = new StackPane() ; 
        root.getChildren().add(l) ; 
        root.getChildren().add(b) ;
        Scene scene1 = new Scene(root,600,400);
        
        primaryStage.setScene(scene1);
        primaryStage.show();
        */
    }



    public static void main(String[] args) {
        launch(args);
    }

}