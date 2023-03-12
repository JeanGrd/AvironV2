import java.util.ArrayList;

public class Tournoi{
    private int nb_participants;
    private String nom;
    private String lieu;
    private String code;
    private float metres;
    private int nb_participants_par_embarcation;
    private boolean estBarre;
    private TypeTournoi type;
    private Categorie categorie;
    private Sexe sexe;
    private Armature armature;
    private MaxSizeArrayList<Integer> concourrants;

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

    public Tournoi(String nom, String lieu, String code, float metres, int nb_participants ,TypeTournoi type) {
        this.nom = nom;
        this.lieu = lieu;
        this.metres = metres;
        this.type = type;
        this.nb_participants = nb_participants;
        this.concourrants = new MaxSizeArrayList<>(this.nb_participants);
        decodeCode(code);
    }

    public void showConcourrants() {
        for (Integer c : this.concourrants) {
            System.out.println(c);
        }

    }

    public void decodeCode(String code) {
        // Vérification de la longueur du code
        if (code.length() < 4 || code.length() > 5) {
            System.out.println("Le code n'est pas valide");
            return;
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

    public ArrayList<Integer> getConcourrants() {
        return concourrants;
    }

    public void addConcourrant(Integer embarcation){
        this.concourrants.add(embarcation);
    }

    public void rmConcourrant(Integer embarcation){
        this.concourrants.remove(embarcation);
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
}