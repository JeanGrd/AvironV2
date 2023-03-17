package javaprojetaviron.model;

import java.util.*;
import javafx.util.Pair;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
        ArrayList<String> infosC = new ArrayList<>();
        if (classement.containsKey(intervalle)) {
            System.out.println("classement de la course " + intervalle + ":");
            for (Map.Entry<Integer, Pair<Embarcation, Float>> entry : classement.get(intervalle).entrySet()) {
                int position = entry.getKey();
                Embarcation embarcation = entry.getValue().getKey();
                float valeur = entry.getValue().getValue();

                infosC.add(Integer.toString(position + 1) + "-" + embarcation.getNom());
                this.controlleur.sendInformationsToView(infosC);

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

        while (distance_parcouru < this.metres) {
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

            for (int i = 1; i < this.nb_participants; i++) {
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
        this.controlleur.finTournoi();
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
        Embarcation embarcation = new Embarcation(infosEmbarcation.get(0), this.getNb_participants_par_embarcation());
        int departInfosP = 1;
        try {
            for (int i = 0; i < this.getNb_participants_par_embarcation(); i++) {
                String[] infosDateNaiss = infosEmbarcation.get(departInfosP + 2).split("/");

                LocalDate date = LocalDate.of(Integer.parseInt(infosDateNaiss[2]), Integer.parseInt(infosDateNaiss[1]), Integer.parseInt(infosDateNaiss[0]));
                Participant p = new Participant(infosEmbarcation.get(departInfosP + 1), infosEmbarcation.get(departInfosP), date);

                embarcation.positionnerParticipant(Integer.parseInt(infosEmbarcation.get(departInfosP + 3)) - 1, p);

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

    public ArrayList getNomsEmbarcations() {
        ArrayList<String> noms = new ArrayList<>();
        for (int i = 0; i < concourrants.size(); i++) {
            noms.add(concourrants.get(i).getNom());
        }
        return noms;
    }

    //Partie CSV
    public void generateCSV(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Intervalle, Place, Temps, Nom de l'embarcation\n");

            List<Float> sortedKeys = new ArrayList<>(classement.keySet());
            Collections.sort(sortedKeys);

            for (Float temps : sortedKeys) {
                Map<Integer, Pair<Embarcation, Float>> classementParTemps = classement.get(temps);
                for (Integer place : classementParTemps.keySet()) {
                    Pair<Embarcation, Float> pair = classementParTemps.get(place);
                    Embarcation embarcation = pair.getKey();
                    Float tempsDeCourse = pair.getValue();
                    String nomEmbarcation;
                    try {
                        nomEmbarcation = embarcation.getNom();
                    } catch (Exception e) {
                        nomEmbarcation = null;
                    }
                    String placement = Integer.toString(place+1) ; 
                    writer.write("" + temps + ',' + placement + "," + tempsDeCourse + "," + nomEmbarcation + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la génération du fichier CSV : " + e.getMessage());
        }
    }

    public static Tournoi readTournoi(String filePath, ControllerAppli c) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String ligne;
        Tournoi tournoi = null;
        Embarcation embarcationCourante = null;
        List<Embarcation> embarcations = new ArrayList<>();
        while ((ligne = br.readLine()) != null) {
            String[] elements = ligne.split(";");
            String type = elements[0];
            switch (type) {
                case "TOURNOI":
                    String nomTournoi = elements[1];
                    String lieu = elements[2];
                    String code = elements[3];
                    int distance = Integer.parseInt(elements[4]);
                    int nbParticipants = Integer.parseInt(elements[5]);
                    int nbEmbarcations = Integer.parseInt(elements[6]);
                    String typeT = elements[7];
                    TypeTournoi typeTournoi = TypeTournoi.valueOf(typeT);
                    tournoi = new Tournoi(nomTournoi, lieu, code, distance, nbParticipants, nbEmbarcations, typeTournoi, c);
                    break;
                case "EMBARCATION":
                    String nomEmbarcation = elements[1];
                    int nbRameurs = Integer.parseInt(elements[2]);
                    embarcationCourante = new Embarcation(nomEmbarcation, nbRameurs);
                    embarcations.add(embarcationCourante);
                    break;
                case "PARTICIPANT":
                    int indexParticipant = Integer.parseInt(elements[1]);
                    String prenom = elements[2];
                    String nom = elements[3];
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dateNaissance = LocalDate.parse(elements[4], formatter);
                    Participant participant = new Participant(prenom, nom, dateNaissance);
                    if (indexParticipant != 0) {
                        embarcationCourante.positionnerParticipant(indexParticipant, participant);
                    } else {
                        embarcationCourante.putBarreur(participant);
                    }
                    break;
                default:
                    break;
            }
        }
        for (Embarcation embarcation : embarcations) {
            tournoi.addConcourrant(embarcation);
        }
        return tournoi;
    }

}
