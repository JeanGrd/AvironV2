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

/**
 * Classe pour gérer un tournoi.
 */
public class Tournoi implements Cloneable {

    private ControllerAppli controlleur; // Contrôleur de l'application
    private final int nb_participants; // Nombre de participants au tournoi
    private int nb_participants_par_embarcation; // Nombre de participants par embarcation
    private final String nom; // Nom du tournoi
    private final String lieu; // Lieu du tournoi
    private String code; // Code du tournoi

    private float startTimeInterval = 15f; // Intervalle de départ en secondes

    private final float metres; // Distance de la course en mètres
    private final float intervalle; // Intervalle entre chaque embarcation lors du départ
    private boolean estBarre; // Indique si l'embarcation est barrée
    private final TypeTournoi type; // Type du tournoi (par exemple, sprint, standard)
    private Categorie categorie; // Catégorie de l'équipe (par exemple, junior, senior)
    private Sexe sexe; // Sexe des participants (homme, femme)
    private Armature armature; // Armature de l'embarcation (rigide, semi-rigide)

    private MaxSizeArrayList<Embarcation> concourrants; // Liste des embarcations participantes
    private Map<Float, Map<Integer, Pair<Embarcation, Float>>> classement; // Classement des embarcations. La clé externe est le temps, la clé interne est la position, et la valeur est une paire qui contient l'embarcation et son temps de course.

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

    /**
     * Décode le code passé en paramètre pour extraire les informations
     * nécessaires sur le participant ou l'embarcation.
     * Le code doit être de format spécifique pour être valide.
     *
     * @param code La chaîne de caractères à déchiffrer.
     * @throws Exception Si le code est invalide.
     */
    public void decodeCode(String code) throws Exception {
        // Vérification de la longueur du code. Il doit contenir au minimum 3 et au maximum 5 caractères.
        if (code.length() < 3 || code.length() > 5) {
            throw new Exception("Le code n'est pas valide");
        }

        // Récupération du sexe du participant ou de l'embarcation en fonction du premier caractère du code.
        this.sexe = Sexe.getSexe(code.substring(0, 1));
        // Récupération de la catégorie du participant ou de l'embarcation en fonction du deuxième caractère du code.
        this.categorie = Categorie.getCategorie(code.substring(1, 2));
        // Récupération du nombre de participants par embarcation en fonction du troisième caractère du code.
        this.nb_participants_par_embarcation = Integer.parseInt(code.substring(2, 3));

        // Si le code contient un "X", l'armature de l'embarcation est définie comme COUPLE.
        // Sinon, l'armature est définie comme POINTE.
        if (code.contains("X")) {
            this.armature = Armature.COUPLE;
        } else {
            this.armature = Armature.POINTE;
        }

        // Si le code contient un "+", alors l'embarcation est barrée.
        this.estBarre = code.contains("+");
    }


    /**
     * Ajoute une embarcation à la liste des concourrants après avoir vérifié sa conformité.
     * Les vérifications incluent le statut de l'embarcation, l'âge des participants, la taille de l'embarcation
     * et la présence d'un barreur si nécessaire.
     *
     * @param embarcation L'embarcation à ajouter.
     * @throws Exception Si l'embarcation n'est pas conforme pour une quelconque raison.
     */
    public void addConcourrant(Embarcation embarcation) throws Exception {
        // Vérifie si l'embarcation est complète.
        // Si ce n'est pas le cas, une exception est levée.
        if (!embarcation.isOk()) {
            throw new Exception("Embarcation non terminée");
        }

        // Vérifie si l'âge de tous les participants de l'embarcation est conforme à la catégorie.
        // Si ce n'est pas le cas, une exception est levée.
        if (!embarcation.checkAge(this.categorie.getMaxAge())) {
            throw new Exception("Un participant est hors catégorie");
        }

        // Vérifie si la taille de l'embarcation est conforme au nombre de participants prévu pour le tournoi.
        // Si ce n'est pas le cas, une exception est levée.
        if (embarcation.getSizeEmbarcation() != this.nb_participants_par_embarcation) {
            throw new Exception("Cette embarcation n'a pas la bonne dimension pour ce tournoi");
        }

        // Vérifie si l'embarcation est déjà présente dans la liste des concourrants.
        // Si c'est le cas, une exception est levée.
        if (this.concourrants.contains(embarcation)) {
            throw new Exception("embarcation existe déjà");
        }

        // Ajoute l'embarcation à la liste des concourrants.
        this.concourrants.add(embarcation);
    }

    /**
     * Supprime une embarcation de la liste des concourrants.
     *
     * @param embarcation L'embarcation à supprimer.
     */
    public void rmConcourrant(Embarcation embarcation) {
        // Supprime l'embarcation de la liste des concourrants.
        this.concourrants.remove(embarcation);
    }

    /**
     * Affiche les informations de toutes les embarcations qui concourent.
     */
    public void showConcourrants() {
        // Parcourt la liste des concourrants et affiche chaque embarcation.
        for (Embarcation c : this.concourrants) {
            System.out.println(c);
        }
    }

    /**
     * Trie le classement selon les valeurs dans le sous-mapping.
     *
     * @param classement Le classement à trier.
     * @return Le classement trié.
     */
    private Map<Float, Map<Integer, Pair<Embarcation, Float>>> sortClassement(Map<Float, Map<Integer, Pair<Embarcation, Float>>> classement) {
        // Création d'une nouvelle map qui va contenir le classement trié.
        Map<Float, Map<Integer, Pair<Embarcation, Float>>> sortedClassement = new LinkedHashMap<>();

        // Parcours des entrées de la map de classement.
        for (Map.Entry<Float, Map<Integer, Pair<Embarcation, Float>>> entry : classement.entrySet()) {
            // Tri du sous-mapping par les valeurs de chaque paire (embarcation, flottant).
            Map<Integer, Pair<Embarcation, Float>> sortedInnerMap = entry.getValue().entrySet().stream()
                    .sorted(Map.Entry.<Integer, Pair<Embarcation, Float>>comparingByValue(Comparator.comparing(Pair::getValue)))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey, // clé inchangée
                            Map.Entry::getValue, // valeur inchangée
                            (e1, e2) -> e1, // en cas de collision de clés, on garde la première entrée
                            LinkedHashMap::new // on souhaite conserver l'ordre d'insertion
                    ));
            // On ajoute le sous-mapping trié dans le classement trié.
            sortedClassement.put(entry.getKey(), sortedInnerMap);
        }

        // Retour du classement trié.
        return sortedClassement;
    }

    /**
     * Efface tous les concurrents.
     */
    public void clearConcourrants() {
        // Suppression de tous les éléments de la liste des concurrents.
        this.concourrants.clear();
    }

    /**
     * Initialise le classement.
     *
     * @param intervalle L'intervalle de temps.
     * @param position La position de l'embarcation.
     * @param temps Le temps d'arrivée de l'embarcation.
     */
    private void initializeClassement(float intervalle, int position, float temps) {
        // Vérification de l'existence de l'intervalle dans le classement.
        // S'il n'existe pas, on ajoute un nouvel HashMap à cet intervalle.
        if (!classement.containsKey(intervalle)) {
            classement.put(intervalle, new HashMap<Integer, Pair<Embarcation, Float>>());
        }
        // Ajout de la position et du temps dans le sous-mapping de l'intervalle donné.
        // Ici, l'embarcation est initialisée à null.
        classement.get(intervalle).put(position, new Pair<Embarcation, Float>(null, temps));
    }

    /**
     * Ajoute une embarcation au classement.
     *
     * @param intervalle L'intervalle de temps.
     * @param position La position de l'embarcation.
     * @param embarcation L'embarcation à ajouter.
     */
    public void addInClassement(float intervalle, int position, Embarcation embarcation) {
        // Si l'intervalle n'existe pas dans le classement, on ajoute un nouvel HashMap à cet intervalle.
        if (!classement.containsKey(intervalle)) {
            classement.put(intervalle, new HashMap<Integer, Pair<Embarcation, Float>>());
        }

        // Création d'une nouvelle paire avec l'embarcation et une valeur null.
        Pair<Embarcation, Float> pair = new Pair<Embarcation, Float>(embarcation, null);

        // Si la position existe déjà dans le sous-mapping de l'intervalle,
        // et si l'embarcation est null, on remplace l'embarcation par celle passée en paramètre.
        if (classement.get(intervalle).get(position) != null) {
            pair = classement.get(intervalle).get(position);
            if (pair.getKey() == null) {
                pair = new Pair<Embarcation, Float>(embarcation, pair.getValue());
            }
        }

        // On met à jour le classement avec la nouvelle paire à la position donnée.
        classement.get(intervalle).put(position, pair);
    }

    /**
     * Affiche le classement pour un intervalle donné.
     *
     * @param intervalle L'intervalle de temps.
     * @throws Exception Si l'intervalle n'est pas trouvé dans le classement.
     */
    public void showClassement(float intervalle) throws Exception {
        // Initialisation de la liste pour stocker les informations du classement.
        ArrayList<String> infosC = new ArrayList<>();

        // Si l'intervalle est présent dans le classement, affiche les informations.
        if (classement.containsKey(intervalle)) {
            System.out.println("classement de la course " + intervalle + ":");
            // Parcourir les entrées du sous-mapping de l'intervalle.
            for (Map.Entry<Integer, Pair<Embarcation, Float>> entry : classement.get(intervalle).entrySet()) {
                int position = entry.getKey();
                Embarcation embarcation = entry.getValue().getKey();
                float valeur = entry.getValue().getValue();

                // Affichage des informations de l'embarcation à la position courante.
                System.out.println("Position: " + (position + 1) + ", Embarcation: " + embarcation + ", Valeur: " + valeur);
            }
        } else {
            // Si l'intervalle n'est pas présent dans le classement, lève une exception.
            throw new Exception("Intervalle " + intervalle + " non trouvée dans le classement.");
        }
    }

    /**
     * Démarre le tournoi en fonction du type.
     *
     * @throws Exception Si le type de course est inconnu.
     */
    public void running() throws Exception {
        switch (this.type) {
            // Si le type de la course est une course en ligne
            case COURSE_LIGNE -> runningCourseLigne();

            // Si le type de la course est une course contre la montre
            case COURSE_CONTRE_LA_MONTRE -> runningCourseContreLaMontre();

            // Si le type de la course est inconnu, une exception est levée
            default -> throw new Exception("Type de course inconnu : " + this.type);
        }
    }

    /**
     * Démarre une course contre la montre.
     *
     * @throws Exception Si le tournoi n'est pas valide.
     */
    private void runningCourseContreLaMontre() throws Exception {
        // Vérifie si le tournoi est valide
        if (!this.isOk()) {
            throw new Exception("Tournoi non valide");
        }

        // Initialisation du chronomètre
        Chronometre chrono = new Chronometre();
        // Liste pour stocker les références aux threads
        List<Thread> threads = new ArrayList<>();

        // Démarre le chronomètre
        chrono.running();

        // Parcourt les concurrents
        for (int i = 0; i < this.concourrants.size(); i++) {
            Embarcation c = this.concourrants.get(i);

            // Crée et démarre un nouveau thread pour chaque concurrent
            Thread t = new Thread(() -> courir(c));
            t.start();

            // Ajoute le thread à la liste
            threads.add(t);

            // Si ce n'est pas le dernier thread, on attend le début de l'intervalle de départ
            if (i < this.concourrants.size() - 1) {
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

        // Arrête le chronomètre
        chrono.stop();

        // Trie le classement
        this.classement = sortClassement(this.classement);
    }

    /**
     * Simule la course pour une embarcation donnée.
     *
     * @param e L'embarcation qui va courir.
     */
    private void courir(Embarcation e) {
        // Création d'un chronomètre pour le temps de course de l'embarcation
        Chronometre chrono = new Chronometre();

        // Création d'un senseur pour générer les temps de passage à chaque intervalle
        Senseur senseur = new Senseur(intervalle);

        // Initialisation de la distance parcourue par l'embarcation
        float distance_parcourue = 0f;

        // Démarre le chronomètre pour le temps de course de l'embarcation
        chrono.running();

        // Tant que l'embarcation n'a pas parcouru la distance totale
        while (distance_parcourue < this.metres) {
            // Génère le temps pour le prochain intervalle
            float dist = senseur.generateTime();

            // Tant que le temps du chronomètre n'a pas atteint le temps généré par le senseur
            while (chrono.getTemps() < dist) {
                // Permet à d'autres threads de s'exécuter
                Thread.yield();
            }

            // Augmente la distance parcourue par l'intervalle
            distance_parcourue += intervalle;

            // Initialise le classement avec la distance parcourue, l'index de l'embarcation et le temps du chronomètre
            initializeClassement(distance_parcourue, concourrants.indexOf(e), chrono.getTemps());
        }

        // Arrête le chronomètre pour le temps de course de l'embarcation
        chrono.stop();
    }

    /**
     * Exécute la course en ligne pour le tournoi.
     *
     * @throws Exception si le tournoi n'est pas valide.
     */
    public void runningCourseLigne() throws Exception {
        // Vérification de la validité du tournoi
        if (!this.isOk()) {
            throw new Exception("Tournoi non valide");
        }

        // Initialisation du chronomètre pour le suivi de la course
        Chronometre chrono = new Chronometre();

        // Initialisation du senseur pour le premier arrivant
        Senseur firstArrivalSenseur = new Senseur(intervalle);

        // Initialisation d'un générateur aléatoire
        Random rand = new Random();

        // Démarre le chronomètre
        chrono.running();

        // Initialisation de la distance parcourue et du temps du premier arrivant
        float distance_parcourue = 0f;
        float first_arrival = firstArrivalSenseur.generateTime();

        // Tant que la distance parcourue n'a pas atteint la distance totale
        while (distance_parcourue < this.metres) {
            // Tant que le temps du chronomètre n'a pas atteint le temps du premier arrivant
            while (chrono.getTemps() < first_arrival) {
                // Permet à d'autres threads de s'exécuter
                Thread.yield();
            }

            // Augmente la distance parcourue de l'intervalle
            distance_parcourue += intervalle;

            // Initialise le classement avec la distance parcourue, la position et le temps du premier arrivant
            initializeClassement(distance_parcourue, 0, first_arrival);

            // Le nom de l'équipe qui est arrivée en premier
            String nomE = "First";
            Embarcation embarcationCourante = null;

            // Ajoute l'équipe qui est arrivée en premier dans le classement
            this.addInClassement(distance_parcourue, 0, embarcationCourante);

            // Génère le temps du prochain premier arrivant
            first_arrival = firstArrivalSenseur.generateTime() + chrono.getTemps();

            // Pour chaque participant restant
            for (int i = 1; i < this.nb_participants; i++) {
                // Génère un temps aléatoire pour l'arrivée du participant
                double randomNum = (float) (0.5 + (3 - 0.5) * rand.nextDouble()) + chrono.getTemps();

                // Tant que le temps du chronomètre n'a pas atteint le temps généré
                while (chrono.getTemps() < randomNum) {
                    // Initialise le classement avec la distance parcourue, la position et le temps généré
                    initializeClassement(distance_parcourue, i, (float) randomNum);
                }

                // Le nom de l'équipe qui est arrivée ensuite
                String nomEquipeSuivante = "Second";
                Embarcation embarcationCouranteSuivante = null;
                try {
                    // Essaye de récupérer l'embarcation correspondante
                    embarcationCouranteSuivante = this.getEmbarcationWithName(nomEquipeSuivante);
                } catch (Exception e) {

                }

                // Ajoute l'embarcation dans le classement
                this.addInClassement(distance_parcourue, i, embarcationCouranteSuivante);

            }
            // Affiche le classement à la distance parcourue
            this.showClassement(distance_parcourue);
        }
        // Arrête le chronomètre
        chrono.stop();
    }

    /**
     * Obtient le gagnant du tournoi.
     *
     * @return L'embarcation qui a gagné le tournoi, ou null s'il n'y a pas de gagnant.
     */
    public Embarcation getWinner() {
        // Trouve l'intervalle finale (la clé maximale dans le Map)
        Float derniereIntervalle = Collections.max(classement.keySet());

        // Récupère le classement pour cet intervalle
        Map<Integer, Pair<Embarcation, Float>> classementDerniereIntervalle = classement.get(derniereIntervalle);

        // Récupère l'embarcation qui a gagné (position 0)
        Pair<Embarcation, Float> pairGagnant = classementDerniereIntervalle.get(0);

        // Si le gagnant existe et que l'embarcation est non nulle, retourne l'embarcation gagnante
        if(pairGagnant != null && pairGagnant.getKey() != null) {
            return pairGagnant.getKey();
        }
        // Sinon, retourne null (il n'y a pas de gagnant)
        else {
            return null;
        }
    }

    /**
     * Obtient le nom du tournoi.
     *
     * @return Le nom du tournoi.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Obtient le lieu du tournoi.
     *
     * @return Le lieu du tournoi.
     */
    public String getLieu() {
        return lieu;
    }

    /**
     * Obtient le code du tournoi.
     *
     * @return Le code du tournoi.
     */
    public String getCode() {
        return code;
    }

    /**
     * Obtient la distance du tournoi en mètres.
     *
     * @return La distance du tournoi en mètres.
     */
    public float getMetres() {
        return metres;
    }

    /**
     * Obtient l'intervalle de temps entre les départs des embarcations dans le tournoi.
     *
     * @return L'intervalle de temps entre les départs.
     */
    public float getIntervalle() {
        return this.intervalle;
    }

    /**
     * Obtient le type du tournoi.
     *
     * @return Le type du tournoi.
     */
    public TypeTournoi getType() {
        return type;
    }

    /**
     * Obtient le nom de l'embarcation concourant à une certaine position.
     *
     * @param position La position de l'embarcation dans la liste des concurrents.
     * @return Le nom de l'embarcation à cette position.
     */
    public String getConcourrant(int position) {
        return this.concourrants.get(position).getName();
    }

    /**
     * Obtient la liste des embarcations concourantes.
     *
     * @return La liste des embarcations concourantes.
     */
    public MaxSizeArrayList<Embarcation> getConcourrants() {
        return this.concourrants;
    }

    /**
     * Définit l'intervalle de temps entre les départs des embarcations dans le tournoi.
     *
     * @param startTimeInterval L'intervalle de temps entre les départs à définir.
     */
    public void setStartTimeInterval(float startTimeInterval) {
        this.startTimeInterval = startTimeInterval;
    }

    /**
     * Obtient l'intervalle de temps entre les départs des embarcations dans le tournoi.
     *
     * @return L'intervalle de temps entre les départs.
     */
    public float getStartTimeInterval() {
        return startTimeInterval;
    }

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
     * Vérifie si le nombre de participants au tournoi est correct.
     *
     * @return Vrai si le nombre de participants correspond à l'attendu, sinon une exception est levée.
     * @throws Exception Si le nombre de participants ne correspond pas à l'attendu.
     */
    public boolean isOk() throws Exception {
        if (this.concourrants.size() == this.nb_participants) {
            return true;
        } else {
            throw new Exception("Tournoi non valide");
        }
    }

    /**
     * Crée des embarcations avec les participants en se basant sur une liste de informations d'embarcations.
     *
     * @param infosEmbarcation Une liste des informations des embarcations.
     * Chaque embarcation est une liste de chaînes de caractères qui contient les informations de chaque participant
     * (nom, date de naissance et position dans l'embarcation).
     * Le premier élément de chaque liste est le nom de l'embarcation.
     */
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

    /**
     * Retourne l'objet Embarcation dont le nom est passé en paramètre.
     * Si aucun embarcation avec ce nom n'est trouvé, une exception est lancée.
     *
     * @param nomE Le nom de l'embarcation à chercher.
     * @return L'objet Embarcation correspondant.
     * @throws Exception si aucun embarcation avec ce nom n'est trouvé.
     */
    public Embarcation getEmbarcationWithName(String nomE) throws Exception {
        Embarcation e = null;
        for (int i = 0; i < this.concourrants.size(); i++) {
            // Convertit le nom en majuscules pour la comparaison, afin d'éviter les problèmes de casse.
            if (this.concourrants.get(i).getName().toUpperCase().equals(nomE.toUpperCase())) {
                e = this.concourrants.get(i);
            }
        }

        // Si aucune embarcation correspondante n'a été trouvée, lance une exception.
        if (e == null) {
            throw new Exception("Équipe inconnue");
        }

        return e;
    }

    /**
     * Récupère les noms de toutes les embarcations participant à la course.
     *
     * @return Une ArrayList contenant les noms de toutes les embarcations.
     */
    public ArrayList getNomsEmbarcations() {
        ArrayList<String> noms = new ArrayList<>();
        for (int i = 0; i < concourrants.size(); i++) {
            noms.add(concourrants.get(i).getName());
        }
        return noms;
    }

    /**
     * Génère un fichier CSV contenant le classement de la course.
     * Le fichier CSV contiendra les informations suivantes : intervalle, place, temps, nom de l'embarcation.
     *
     * @param filePath Le chemin du fichier CSV à générer.
     */
    public void generateCSV(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Écrit l'en-tête du fichier CSV
            writer.write("Intervalle, Place, Temps, Nom de l'embarcation\n");

            // Trie les clés du classement (les temps)
            List<Float> sortedKeys = new ArrayList<>(classement.keySet());
            Collections.sort(sortedKeys);

            // Parcours les intervalles de temps dans l'ordre
            for (Float temps : sortedKeys) {
                // Récupère le classement à chaque intervalle
                Map<Integer, Pair<Embarcation, Float>> classementParTemps = classement.get(temps);
                // Parcours chaque position dans le classement
                for (Integer place : classementParTemps.keySet()) {
                    // Récupère l'embarcation et le temps de course à la position actuelle
                    Pair<Embarcation, Float> pair = classementParTemps.get(place);
                    Embarcation embarcation = pair.getKey();
                    Float tempsDeCourse = pair.getValue();
                    String nomEmbarcation;
                    // Récupère le nom de l'embarcation, ou null si l'embarcation est null
                    try {
                        nomEmbarcation = embarcation.getName();
                    } catch (Exception e) {
                        nomEmbarcation = null;
                    }
                    String placement = Integer.toString(place+1) ;
                    // Écrit une ligne dans le fichier CSV pour cette position
                    writer.write("" + temps + ',' + placement + "," + tempsDeCourse + "," + nomEmbarcation + "\n");
                }
            }
        } catch (IOException e) {
            // Affiche une erreur si une exception est lancée lors de la création du fichier
            System.err.println("Erreur lors de la génération du fichier CSV : " + e.getMessage());
        }
    }

    /**
     * Lit un tournoi à partir d'un fichier.
     *
     * @param filePath Le chemin d'accès du fichier à lire.
     * @param c Le contrôleur de l'application.
     * @return Un objet Tournoi qui représente le tournoi lu.
     * @throws Exception Si une erreur se produit lors de la lecture du fichier.
     */
    public static Tournoi readTournoi(String filePath, ControllerAppli c) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String ligne;
        Tournoi tournoi = null;
        Embarcation embarcationCourante = null;
        List<Embarcation> embarcations = new ArrayList<>();

        // Boucle de lecture du fichier ligne par ligne
        while ((ligne = br.readLine()) != null) {
            // Séparation des éléments de la ligne
            String[] elements = ligne.split(";");
            String type = elements[0];

            // Test du type d'élément
            switch (type) {
                case "TOURNOI":
                    // Création d'un nouveau tournoi
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
                    // Création d'une nouvelle embarcation
                    String nomEmbarcation = elements[1];
                    int nbRameurs = Integer.parseInt(elements[2]);
                    embarcationCourante = new Embarcation(nomEmbarcation, nbRameurs);
                    embarcations.add(embarcationCourante);
                    break;
                case "PARTICIPANT":
                    // Ajout d'un participant à l'embarcation courante
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

        // Ajout des embarcations au tournoi
        for (Embarcation embarcation : embarcations) {
            tournoi.addConcourrant(embarcation);
        }

        return tournoi;
    }

    @Override
    public Tournoi clone() {
        try {
            // Cloner l'objet
            Tournoi cloned = (Tournoi) super.clone();

            // Vider les concurrents du clone
            cloned.clearConcourrants();

            // Assigner un nouveau contrôleur à l'objet cloné
            // Supposer qu'il y a un constructeur par défaut. Remplacer par la méthode de clonage réelle si nécessaire.
            cloned.controlleur = new ControllerAppli();

            // Cloner les concurrents
            // Supposer qu'il y a un constructeur de copie. Remplacer par la méthode de clonage réelle si nécessaire.
            cloned.concourrants = new MaxSizeArrayList<>(this.nb_participants);

            // Cloner le classement
            // Supposer que la méthode de clonage de HashMap fonctionnera. Remplacer par un clone profond réel si nécessaire.
            cloned.classement = new HashMap<>(this.classement);

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Ne peut pas arriver car nous implémentons Cloneable
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
}

