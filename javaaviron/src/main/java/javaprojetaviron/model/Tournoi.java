package javaprojetaviron.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.util.Pair;
import javaprojetaviron.controller.ControllerAppli;

public class Tournoi {

    private ControllerAppli controlleur;
    private final int nb_participants;
    private int nb_participants_par_embarcation;
    private final String nom;
    private final String lieu;
    private String code;
    private final float metres;
    private final float intervalle;
    private boolean estBarre;
    private final TypeTournoi type;
    private Categorie categorie;
    private Sexe sexe;
    private Armature armature;
    private final MaxSizeArrayList<Embarcation> concourrants;
    private final Map<Float, Map<Integer, Pair<Embarcation, Float>>> classement;
    private Thread thread;

    public int getNb_participants_par_embarcation() {
        return nb_participants_par_embarcation;
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

    public Tournoi(String nom, String lieu, String code, float metres, float intervalle, int nb_participants, TypeTournoi type, ControllerAppli c) throws Exception {
        this.controlleur = c;
        this.decodeCode(code);
        this.nom = nom;
        this.lieu = lieu;
        this.metres = metres;
        this.intervalle = intervalle;
        this.type = type;
        this.nb_participants = nb_participants;

        if (this.metres % intervalle != 0) {
            throw new Exception("Intervalle incorrecte");
        }

        if (metres > this.categorie.getNb_max_m()) {
            throw new Exception("Nombre de metres donné n'appartient pas à la catégorie spécifié dans le code");
        }

        this.concourrants = new MaxSizeArrayList<>(this.nb_participants);
        this.classement = new HashMap<Float, Map<Integer, Pair<Embarcation, Float>>>();
    }

    public void decodeCode(String code) throws Exception {
        // Vérification de la longueur du code
        if (code.length() < 3 || code.length() > 5) {
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

        if (!embarcation.checkAge(this.categorie.getMax_age())) {
            throw new Exception("Un participant est hors catégorie");
        }

        if (embarcation.getSizeEmbarcation() != this.nb_participants_par_embarcation) {
            throw new Exception("Cette embarcation n'a pas la bonne dimension pour ce tournoi");
        }

        /*if (embarcation.containsBarreur() != this.estBarre) {
            throw new Exception("Cette embarcation ne contient pas de barreur");
        }*/
        if (this.concourrants.contains(embarcation)) {
            throw new Exception("embarcation existe déjà");
        }

        this.concourrants.add(embarcation);
    }

    public void rmConcourrant(Embarcation embarcation) {
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
        try {
            this.isOk();
        } catch (Exception e) {
            controlleur.erreurSaisieAlerte(e.getMessage());
        }

        Chronometre chrono = new Chronometre();
        Senseur firstArrivalSensor = new Senseur(intervalle);
        Random rand = new Random();

        chrono.running();

        float distance_parcouru = 0f;
        float first_arrival = firstArrivalSensor.genererTemps();

        while (distance_parcouru < metres) {
            while (chrono.getTemps() < first_arrival) {
                //System.out.println(first_arrival);
                Thread.yield(); // permet à d'autres threads de s'exécuter
            }

            distance_parcouru += intervalle;
            initializeClassement(distance_parcouru, 0, first_arrival);

            //Le premier vient d'arriver => on doit demander la saisie du nom de la 1ère équipe
            String nomE = controlleur.getNomEmbarcationRunning();
            Embarcation embarcationCourante = null;
            try {
                embarcationCourante = this.getEmbarcationWithName(nomE);
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
            this.addInClassement(distance_parcouru, 0, embarcationCourante);
            first_arrival = firstArrivalSensor.genererTemps() + chrono.getTemps();

            for (int i = 1; i < nb_participants; i++) {
                double randomNum = (float) (0.5 + (3 - 0.5) * rand.nextDouble()) + chrono.getTemps();
                while (chrono.getTemps() < randomNum) {
                    initializeClassement(distance_parcouru, i, (float) randomNum);
                }
                //le suivant vient de finir => il faut demander la saisie de son nom à l'interface
                String nomEquipeSuivante = controlleur.getNomEmbarcationRunning();
                Embarcation embarcationCouranteSuivante = null;
                try {
                    embarcationCouranteSuivante = this.getEmbarcationWithName(nomEquipeSuivante);
                } catch (Exception e) {

                }
                this.addInClassement(distance_parcouru, i, embarcationCouranteSuivante);
            }
            this.showClassement(distance_parcouru);
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

    public float getIntervalle() {
        return this.intervalle;
    }

    public TypeTournoi getType() {
        return type;
    }

    public String getConcourrants(int position) {
        return this.concourrants.get(position).getNom();
    }

    public boolean isOk() throws Exception {
        if (this.concourrants.size() == this.nb_participants) {
            return true;
        } else {
            throw new Exception("Tournoi non valide");
        }

    }

    @Override
    public String toString() {
        return "Tournoi{"
                + "nom='" + nom + '\''
                + ", lieu='" + lieu + '\''
                + ", code='" + code + '\''
                + ", metres=" + metres
                + ", estBarre=" + estBarre
                + ", type=" + type
                + ", categorie=" + categorie
                + ", sexe=" + sexe
                + ", armature=" + armature
                + ", concourrants=" + concourrants
                + '}';
    }

    //Rajout méthodes pour le controlleur afin de créer ce qui est necéssaire
    public void creationEmbarcationsWithParticipants(ArrayList<String> infosEmbarcation) {
        Embarcation embarcation = new Embarcation(infosEmbarcation.get(0), this.getNb_participants_par_embarcation(), this.estBarre);
        int departInfosP = 1;
        try {
            for (int i = 0; i < this.getNb_participants_par_embarcation(); i++) {
                String[] infosDateNaiss = infosEmbarcation.get(departInfosP + 2).split("/");

                LocalDate date = LocalDate.of(Integer.parseInt(infosDateNaiss[2]), Integer.parseInt(infosDateNaiss[1]), Integer.parseInt(infosDateNaiss[0]));
                Participant p = new Participant(infosEmbarcation.get(departInfosP + 1), infosEmbarcation.get(departInfosP), date);

                if (Integer.parseInt(infosEmbarcation.get(departInfosP + 3)) == 0 && this.estBarre) {
                    embarcation.putBarreur(p);
                } else {
                    embarcation.positionnerParticipant(Integer.parseInt(infosEmbarcation.get(departInfosP + 3)) - 1, p);
                }

                departInfosP = departInfosP + 4;
            }
            this.addConcourrant(embarcation);
        } catch (Exception e) {
            this.controlleur.erreurSaisieEquipes(e.getMessage());
        }

    }

    public Embarcation getEmbarcationWithName(String nomE) throws Exception {
        Embarcation e = null;
        for (int i = 0; i < this.concourrants.size(); i++) {
            if (this.concourrants.get(i).getNom().toUpperCase().equals(nomE.toUpperCase())) {
                e = this.concourrants.get(i);
            }
        }

        if (e == null) {
            throw new Exception("Equipe inconnue");
        }

        return e;
    }
}
