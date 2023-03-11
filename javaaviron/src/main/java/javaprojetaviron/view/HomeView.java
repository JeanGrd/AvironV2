package javaprojetaviron.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * La classe HomeView représente l'interface d'accueil de l'application 
 * @author PaulineVarin
 */
public class HomeView {
    //Bouttons
    private Button suivantB = new Button("Suivant") ;
    private Button chargerB = new Button("Charger") ; 
    
    //Labels
    private Label l = new Label() ;
    
    //Scenes
    private VBox rootV = new VBox(10) ;
    private HBox rootH = new HBox(10) ; 
    private VBox root = new VBox(10) ; 
   
    
    /**
     * Creation de la scene avec l'initialisation de tout les composants et le rajout a la scene
     * @return la scene construite qui est affiché dans le stage principal
     */
    public Scene creationScene() {
        //Création des différents éléments 
        this.l.setText("Gestion des tournois d'Aviron");
        
        this.suivantB.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Scene s1 = ((Button)e.getSource()).getScene() ; 
                Stage stageP =  (Stage) s1.getWindow() ; 
                
                //Mise en place de la scene suivante
                CreateTournoiView tournoiCreateView = new CreateTournoiView() ; 
                Scene sceneCreateTournoi = tournoiCreateView.creationScene() ; 
                stageP.setScene(sceneCreateTournoi);
            }
        });
        
        this.chargerB.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            }
        });
        
        
        //Creation de la scene titre
        this.rootV.setAlignment(Pos.CENTER);
        this.rootV.getChildren().add(l) ; 
        
        //Creation de la scene avec les boutons
        this.rootH.setAlignment(Pos.CENTER);
        this.rootH.getChildren().addAll(chargerB,suivantB) ; 
        
        //Creation de la scene principale 
        this.root.setAlignment(Pos.CENTER);
        this.root.getChildren().addAll(rootV, rootH) ; 
        Scene scene = new Scene(root, 1000,600); 
        
        return scene ; 
        
    }
   
    
}
