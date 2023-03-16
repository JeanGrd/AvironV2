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
import javafx.stage.Stage;
import javaprojetaviron.controller.ControllerAppli;
import javaprojetaviron.model.Tournoi;

public class ApplicationMain extends Application{
    
    @Override
    public void start(Stage primaryStage) {
        
        //Initialisation de tout les modèles avant à vide et liaison avec le controlleur c sur le tournoi !!!!!
        ControllerAppli c = new ControllerAppli(); 
        Tournoi t = null; 
        c.setModel(t);
        
        //Mise en place de la vue 
        HomeView homescene = new HomeView() ; 
        homescene.setControlleurVue(c);
        primaryStage.setScene(homescene.creationScene());
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args) ; 
    }
    
    

}