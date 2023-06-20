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
import java.util.stream.Collectors;

import javaprojetaviron.controller.ControllerAppli;

public class Tournoi implements Cloneable {

    private ControllerAppli controlleur;
    private final int nb_participants;
    private int nb_participants_par_embarcation;
    private final String nom;
    private final String lieu;
    private String code;

    private float startTimeInterval = 15f;

    private final float metres;
    private final float intervalle;
    private boolean estBarre;
    private final TypeTournoi type;
    private Categorie categorie;
    private Sexe sexe;
    private Armature armature;
    private MaxSizeArrayList<Embarcation> concourrants;
    private Map<Float, Map<Integer, Pair<Embarcation, Float>>> classement;

    /**
     * Retourne le nombre de participants par embarcation.
     * @return le nombre de participants par embarcation
     */
    public int getNb_participants_par_embarcation() {
        return nb_participants_par_embarcation;
    }

    /**
     * Retourne si le bateau est barré ou non.
     * @return vrai si le bateau est barré, faux sinon
     */
    public boolean isBarre() {
        return estBarre;
    }

    /**
     * Retourne la catégorie du tournoi.
     * @return la catégorie du tournoi
     */
    public Categorie getCategorie() {
        return this.categorie;
    }

    /**
     * Retourne le sexe des participants.
     * @return le sexe des participants
     */
    public Sexe getGender() {
        return sexe;
    }

    /**
     * Retourne le nombre de participants au tournoi.
     * @return le nombre de participants au tournoi
     */
    public int getNb_participants() {
        return nb_participants;
    }

    /**
     * Retourne l'armature du tournoi.
     * @return l'armature du tournoi
     */
    public Armature getArmature() {
        return armature;
    }

    /**
     * Constructeur de la classe Tournoi.
     * @param nom le nom du tournoi
     * @param lieu le lieu du tournoi
     * @param code le code du tournoi
     * @param metres le nombre de mètres parcourus
     * @param intervalle l'intervalle de temps entre chaque tournoi
     * @param nb_participants le nombre de participants
     * @param type le type de tournoi
     * @param c le contrôleur de l'application
     * @throws Exception si l'intervalle est incorrect ou si le nombre de mètres donné n'appartient pas à la catégorie spécifiée dans le code
     */
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

        if (metres > this.categorie.getMaxDistanceM()) {
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

        if (!embarcation.checkAge(this.categorie.getMaxAge())) {
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

    private Map<Float, Map<Integer, Pair<Embarcation, Float>>> sortClassement(Map<Float, Map<Integer, Pair<Embarcation, Float>>> classement) {
        Map<Float, Map<Integer, Pair<Embarcation, Float>>> sortedClassement = new LinkedHashMap<>();

        for (Map.Entry<Float, Map<Integer, Pair<Embarcation, Float>>> entry : classement.entrySet()) {
            Map<Integer, Pair<Embarcation, Float>> sortedInnerMap = entry.getValue().entrySet().stream()
                    .sorted(Map.Entry.<Integer, Pair<Embarcation, Float>>comparingByValue(Comparator.comparing(Pair::getValue)))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));
            sortedClassement.put(entry.getKey(), sortedInnerMap);
        }

        return sortedClassement;
    }

    public void clearConcourrants() {
        this.concourrants.clear();
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

                System.out.println("Position: " + (position + 1) + ", Embarcation: " + embarcation + ", Valeur: " + valeur);
            }
        } else {
            throw new Exception("Intervalle " + intervalle + " non trouvée dans le classement.");
        }
    }

    public void running() throws Exception {
        switch (this.type) {
            case COURSE_LIGNE -> runningCourseLigne();
            case COURSE_CONTRE_LA_MONTRE -> runningCourseContreLaMontre();
            default -> throw new Exception("Type de course inconnu : " + this.type);
        }
    }

    private void runningCourseContreLaMontre() throws Exception {
        if (!this.isOk()) {
            throw new Exception("Tournoi non valide");
        }

        Chronometre chrono = new Chronometre();
        List<Thread> threads = new ArrayList<>();  // Liste pour stocker les références aux threads

        chrono.running();
        for (int i = 0; i < this.concourrants.size(); i++) {
            Embarcation c = this.concourrants.get(i);
            Thread t = new Thread(() -> courir(c));
            t.start();
            threads.add(t);
            if (i < this.concourrants.size() - 1) {  // Si ce n'est pas le dernier thread
                while (chrono.getTemps() < this.getStartTimeInterval()) {
                    Thread.yield(); // permet à d'autres threads de s'exécuter
                }
            }
        }

        // Attendre que tous les threads se terminent
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        chrono.stop();
        this.classement = sortClassement(this.classement);
    }


    private void courir(Embarcation e) {
        Chronometre chrono = new Chronometre();
        Senseur senseur = new Senseur(intervalle);

        float distance_parcouru = 0f;

        chrono.running();
        while (distance_parcouru < this.metres) {
            float dist = senseur.generateTime();
            while (chrono.getTemps() < dist) {
                //System.out.println(first_arrival);
                Thread.yield(); // permet à d'autres threads de s'exécuter
            }
            distance_parcouru += intervalle;
            initializeClassement(distance_parcouru, concourrants.indexOf(e), chrono.getTemps());
        }
        chrono.stop();

    }

    public void runningCourseLigne() throws Exception {

        if (!this.isOk()) {
            throw new Exception("Tournoi non valide");
        }

        Chronometre chrono = new Chronometre();
        Senseur firstArrivalSenseur = new Senseur(intervalle);
        Random rand = new Random();

        chrono.running();

        float distance_parcouru = 0f;
        float first_arrival = firstArrivalSenseur.generateTime();

        while (distance_parcouru < this.metres) {
            while (chrono.getTemps() < first_arrival) {
                //System.out.println(first_arrival);
                Thread.yield(); // permet à d'autres threads de s'exécuter
            }

            distance_parcouru += intervalle;
            initializeClassement(distance_parcouru, 0, first_arrival);

            //Le premier vient d'arriver => on doit demander la saisie du nom de la 1ère équipe
            String nomE = "First";
            Embarcation embarcationCourante = null;
            this.addInClassement(distance_parcouru, 0, embarcationCourante);

            first_arrival = firstArrivalSenseur.generateTime() + chrono.getTemps();

            for (int i = 1; i < this.nb_participants; i++) {
                double randomNum = (float) (0.5 + (3 - 0.5) * rand.nextDouble()) + chrono.getTemps();
                while (chrono.getTemps() < randomNum) {
                    initializeClassement(distance_parcouru, i, (float) randomNum);
                }

                //le suivant vient de finir => il faut demander la saisie de son nom à l'interface
                String nomEquipeSuivante = "Second";
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

    public Embarcation getWinner() {
        // Trouver la dernière intervalle (max clé dans la Map)
        Float derniereIntervalle = Collections.max(classement.keySet());

        // Récupérer le classement pour cette intervalle
        Map<Integer, Pair<Embarcation, Float>> classementDerniereIntervalle = classement.get(derniereIntervalle);

        // Récupérer l'embarcation qui a gagné (position 0)
        Pair<Embarcation, Float> pairGagnant = classementDerniereIntervalle.get(0);

        if(pairGagnant != null && pairGagnant.getKey() != null) {
            return pairGagnant.getKey();
        }
        else {
            return null;
        }
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

    public String getConcourrant(int position) {
        return this.concourrants.get(position).getName();
    }

    public MaxSizeArrayList<Embarcation> getConcourrants() {
        return this.concourrants;
    }

    public void setStartTimeInterval(float startTimeInterval) {
        this.startTimeInterval = startTimeInterval;
    }

    public float getStartTimeInterval() {
        return startTimeInterval;
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

                embarcation.positionParticipant(Integer.parseInt(infosEmbarcation.get(departInfosP + 3)) - 1, p);

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
            if (this.concourrants.get(i).getName().toUpperCase().equals(nomE.toUpperCase())) {
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
            noms.add(concourrants.get(i).getName());
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
                        nomEmbarcation = embarcation.getName();
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
                        embarcationCourante.positionParticipant(indexParticipant, participant);
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

    @Override
    public Tournoi clone() {
        try {
            Tournoi cloned = (Tournoi) super.clone();
            cloned.clearConcourrants();
            cloned.controlleur = new ControllerAppli(); //Assuming default constructor. Replace with actual cloning method if needed.
            cloned.concourrants = new MaxSizeArrayList<>(this.nb_participants); // Assuming copy constructor. Replace with actual cloning method if needed.
            cloned.classement = new HashMap<>(this.classement); // Assuming HashMap's clone method will work. Replace with actual deep clone if needed.
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Can't happen as we implement Cloneable
        }
    }
}

