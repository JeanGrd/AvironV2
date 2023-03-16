package javaprojetaviron.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.util.Pair;


public class Tournoi{
    private final int nb_participants;
    private int nb_participants_par_embarcation;
    private final String nom;
    private final String lieu;
    private String code;
    private final float metres;
    private final float intervalle ; 
    private boolean estBarre;
    private final TypeTournoi type;
    private Categorie categorie;
    private Sexe sexe;
    private Armature armature;
    private final MaxSizeArrayList<Embarcation> concourrants;
    private final Map<Float, Map<Integer, Pair<Embarcation, Float>>> classement;

    public int getNb_participants_par_embarcation() {
        return nb_participants_par_embarcation;
    }
    
    public float getIntervalle() {
        return this.intervalle ; 
    }

    public boolean isBarre() {
        return estBarre;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public int getNb_participants() {
        return nb_participants;
    }
    
    

    public Armature getArmature() {
        return armature;
    }

    public Tournoi(String nom, String lieu, String code, float metres, float intervalle, int nb_participants, TypeTournoi type) throws Exception {        this.nom = nom;
        this.lieu = lieu;
        this.metres = metres;
        this.intervalle = intervalle; 
        this.type = type;
        this.nb_participants = nb_participants;
        this.concourrants = new MaxSizeArrayList<>(this.nb_participants);
        this.classement = new HashMap<Float, Map<Integer, Pair<Embarcation, Float>>>();
    }

    public void decodeCode(String code) throws Exception {
        // Vérification de la longueur du code
        if (code.length() < 4 || code.length() > 5) {
            throw new Exception("Le code n'est pas valide");
        }

        // Récupération des différentes informations
        this.sexe = Sexe.getSexe(code.substring(0, 1));
        this.categorie = Categorie.getCategorie(code.substring(1, 2));
        this.nb_participants_par_embarcation = Integer.parseInt(code.substring(2, 3));
        if (code.contains("X")) {
            this.armature = Armature.COUPLE;
        } else {
            this.armature = Armature.POINTE;
        }
        this.estBarre = code.contains("+");

    }

    public void addConcourrant(Embarcation embarcation) throws Exception {

        if (!embarcation.isOk()) {
            throw new Exception("Embarcation non terminée");
        }

        if (embarcation.getSizeEmbarcation() != this.nb_participants_par_embarcation) {
            throw new Exception("Cette embarcation n'a pas la bonne dimension pour ce tournoi");
        }

        if (embarcation.containsBarreur() != this.estBarre) {
            throw new Exception("Cette embarcation ne contient pas de barreur");
        }

        if (this.concourrants.contains(embarcation)) {
            throw new Exception("embarcation existe déjà");
        }

        this.concourrants.add(embarcation);
    }

    public void rmConcourrant(Embarcation embarcation){
        this.concourrants.remove(embarcation);
    }

    public void showConcourrants() {
        for (Embarcation c : this.concourrants) {
            System.out.println(c);
        }

    }

    private void initializeClassement(float intervalle, int position, float temps) {
        if (!classement.containsKey(intervalle)) {
            classement.put(intervalle, new HashMap<Integer, Pair<Embarcation, Float>>());
        }
        classement.get(intervalle).put(position, new Pair<Embarcation, Float>(null, temps));
    }

    public void addInClassement(float intervalle, int position, Embarcation embarcation) {
        if (!classement.containsKey(intervalle)) {
            classement.put(intervalle, new HashMap<Integer, Pair<Embarcation, Float>>());
        }

        Pair<Embarcation, Float> pair = new Pair<Embarcation, Float>(embarcation, null);

        // Remplacer la valeur null par le nom de l'embarcation si elle est null
        if (classement.get(intervalle).get(position) != null) {
            pair = classement.get(intervalle).get(position);
            if (pair.getKey() == null) {
                pair = new Pair<Embarcation, Float>(embarcation, pair.getValue());
            }
        }

        classement.get(intervalle).put(position, pair);
    }

    public void showClassement(float intervalle) throws Exception {
        if (classement.containsKey(intervalle)) {
            System.out.println("classement de la course " + intervalle + ":");
            for (Map.Entry<Integer, Pair<Embarcation, Float>> entry : classement.get(intervalle).entrySet()) {
                int position = entry.getKey();
                Embarcation embarcation = entry.getValue().getKey();
                float valeur = entry.getValue().getValue();
                System.out.println("Position: " + (position + 1) + ", Embarcation: " + embarcation + ", Valeur: " + valeur);
            }
        } else {
            throw new Exception("Intervalle " + intervalle + " non trouvée dans le classement.");
        }
    }

    public void running() throws Exception {
         if (!this.isOk()) {
            throw new Exception("Tournoi non valide");
        }

        Chronometre chrono = new Chronometre();
        Senseur firstArrivalSensor = new Senseur(intervalle);
        Random rand = new Random();

        chrono.running();

        float distance_parcouru = 0f;
        float first_arrival = firstArrivalSensor.genererTemps();

        while(distance_parcouru < this.metres) {
            while(chrono.getTemps() < first_arrival){
                //System.out.println(first_arrival);
                Thread.yield(); // permet à d'autres threads de s'exécuter
            }

            distance_parcouru += intervalle;

            initializeClassement(distance_parcouru, 0, first_arrival);
            first_arrival = firstArrivalSensor.genererTemps() + chrono.getTemps();

            for (int i = 1; i < this.nb_participants; i++) {
                double randomNum = (float) (0.5 + (3 - 0.5) * rand.nextDouble()) + chrono.getTemps();
                while(chrono.getTemps() < randomNum) {
                    initializeClassement(distance_parcouru, i, (float) randomNum);
                }

            }

        }
        chrono.stop();
    }

    public String getNom() {
        return nom;
    }

    public String getLieu() {
        return lieu;
    }

    public String getCode() {
        return code;
    }

    public float getMetres() {
        return metres;
    }

    public TypeTournoi getType() {
        return type;
    }

    public String getConcourrants(int position) {
        return this.concourrants.get(position).getNom();
    }

    public boolean isOk(){
        return this.concourrants.size() == this.nb_participants;
    }

    @Override
    public String toString() {
        return "Tournoi{" +
                "nom='" + nom + '\'' +
                ", lieu='" + lieu + '\'' +
                ", code='" + code + '\'' +
                ", metres=" + metres +
                ", estBarre=" + estBarre +
                ", type=" + type +
                ", categorie=" + categorie +
                ", sexe=" + sexe +
                ", armature=" + armature +
                ", concourrants=" + concourrants +
                '}';
    }
    
    //Rajout méthodes pour le controlleur afin de créer ce qui est necéssaire
    public void creationEmbarcationsWithParticipants (ArrayList<String> infosEmbarcations) {
        Embarcation e = new Embarcation (infosEmbarcations.get(0), this.getNb_participants_par_embarcation()) ;
        int departInfosP = 1 ; 
        for (int i=1;i<this.getNb_participants_par_embarcation();i++) {
            infosEmbarcations.get(departInfosP) ; 
            infosEmbarcations.get(departInfosP+1) ;
            infosEmbarcations.get(departInfosP+2) ; 
        }
        //Rajout à la liste des embarcations 
        
    }
    
    
}