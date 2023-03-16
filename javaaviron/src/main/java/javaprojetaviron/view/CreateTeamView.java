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
import javaprojetaviron.controller.ControllerAppli;

/**
 * La classe CreateTeamView représente l'interface qui permet d'initialiser chaque equipe
 * @author PaulineVarin
 */

public class CreateTeamView {
    public static int nombreEquipes = 4;
    private static int nombreParticipantsEquipe = 1 ;
    public static int equipeCourante = 1 ; 
    
    //Récupération données
    private ControllerAppli controlleurVue ; 
    
    //Labels
    private Label l = new Label() ;  
    private Label nomEquipe = new Label() ; 
    private TextField[] listLabels ;
    
    //TextBox
    TextField boxNomEquipe = new TextField("Equipe toto") ; 
    private TextField[] listTextBox ;
    
    //Bouttons
    private Button suivantB = new Button("Suivant") ;
    
    //Scenes
    private VBox rootTitre = new VBox(10) ;
    private HBox rootNomEquipe = new HBox(10) ; 
    private HBox rootBouton = new HBox(10) ; 
    private VBox rootInfosEquipe = new VBox(10) ;
    private VBox root = new VBox(10) ; 
    
    public void setControlleurVue(ControllerAppli c) {
        this.controlleurVue = c;
    }
    
    
    
    /**
     * Permet de mettre à jour la valeur de l'attribut nbEquipe en fonction de celle reçu via le controlleur 
     * @param nbEquipe int
     */
    public void setNbEquipes(int nbEquipe) {
        CreateTeamView.nombreEquipes = nbEquipe;
    }
    
    /**
     * Creation de la scene avec l'initialisation de tout les composants et le rajout a la scene
     * @return la scene construite qui est rajouté au stage principal
     */
    public Scene creationScene() {
        //Definiton du comportement des boutons
        this.suivantB.setOnAction(new EventHandler<ActionEvent> () {
            public void handle(ActionEvent e) {
                if(CreateTeamView.equipeCourante < CreateTeamView.nombreEquipes) {
                    //Envoie des informations au controlleur
                    
                    CreateTeamView.equipeCourante++ ; 
                    System.out.println(CreateTeamView.equipeCourante);
                    
                    Scene s1 = ((Button)e.getSource()).getScene() ; 
                    Stage stageP =  (Stage) s1.getWindow() ; 
                
                    //Mise en place de la même scène pour une autre équipe autant de fois que d'équipes existantes
                    //et liaison avec le controlleur 
                    CreateTeamView teamCreateView = new CreateTeamView() ; 
                    teamCreateView.setControlleurVue(controlleurVue);
                    Scene sceneCreateTeam = teamCreateView.creationScene() ; 
                    stageP.setScene(sceneCreateTeam);
                } else {
                    System.out.println("Passage scene suivante") ; 
                }
               
                
            }
            
        });
        
        //Mise en place du label pour le titre
        this.l.setText("Equipe "+CreateTeamView.equipeCourante);
        
        //Creation de la scene titre
        this.rootTitre.setAlignment(Pos.CENTER);
        this.rootTitre.getChildren().add(l) ; 
        
        
        //Mise en place de la scene pour le nom de l'équipe
        this.nomEquipe.setText("Nom : ");
        this.rootNomEquipe.setAlignment(Pos.CENTER);
        this.rootNomEquipe.getChildren().addAll(this.nomEquipe, this.boxNomEquipe) ; 
        

        //Mise en place de la scene pour le nom des membres de l'équipe
        
        //Définiton des labels
        this.listLabels = new TextField[4] ;
        String labelsString[]=new String[]{
            "Nom",
            "Prenom",
            "Date de naissance",
            "Position",
            }; 
        
        for(int i = 0; i < this.listLabels.length; i++) {
            this.listLabels[i]=new TextField(labelsString[i]);
            this.listLabels[i].setEditable(false);
            this.listLabels[i].setStyle("-fx-background-color: grey;");
            this.listLabels[i].setAlignment(Pos.CENTER);
        }
        
        //Creation des TextBox
        int longeurListe = 4*CreateTeamView.nombreParticipantsEquipe ; 
        this.listTextBox = new TextField[longeurListe] ; 
        for(int i=0;i<this.listTextBox.length;i++) {
            this.listTextBox[i] = new TextField() ; 
        }
        
        //Mise en place de la structure de chaque élément pour la complétion des informations sur les membres de l'équipe
        HBox infoJoueurLabel = new HBox(listLabels[0],listLabels[1],listLabels[2],listLabels[3]) ; 
        infoJoueurLabel.setAlignment(Pos.CENTER);
        
        //Liaison à la scène  pour les labels 
        this.rootInfosEquipe.setAlignment(Pos.CENTER);
        this.rootInfosEquipe.getChildren().addAll(infoJoueurLabel) ; 
        
        //Mise en place de la structure de chaque élement pour chaque participant
        int nbFinBox = 3 ; 
        
        for (int i=1;i<=CreateTeamView.nombreParticipantsEquipe;i++) {
            HBox infoJoueur = new HBox(listTextBox[nbFinBox-3],listTextBox[nbFinBox-2],listTextBox[nbFinBox-1],listTextBox[nbFinBox]) ; 
            infoJoueur.setAlignment(Pos.CENTER);
            //Liaison à la scène
            this.rootInfosEquipe.getChildren().add(infoJoueur) ; 
            nbFinBox =nbFinBox +  listTextBox.length / CreateTeamView.nombreParticipantsEquipe ; 
        }
       
      
        //Creation de la scene des boutons
        this.rootBouton.setAlignment(Pos.CENTER);
        this.rootBouton.getChildren().addAll(suivantB) ; 
        
        //Creation de la scene principale  
        this.root.setAlignment(Pos.CENTER);
        this.root.getChildren().addAll(rootTitre,rootNomEquipe,rootInfosEquipe, rootBouton) ; 
        Scene scene = new Scene(root, 1000,600); 
        
        //Liaison de la scene et du controleur
        this.controlleurVue.setVueTournoi(scene);
        
        
        //Test du bon fonctionnement
        this.controlleurVue.listInfos() ; 
        return scene ; 
    }
    
}
