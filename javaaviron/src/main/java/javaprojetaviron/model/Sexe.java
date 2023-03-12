import java.util.Objects;

public enum Sexe {

    FEMME, HOMME;

    public static Sexe getSexe(String type){
        if (type.equals("F")) {
            return Sexe.FEMME;
        } else if (type.equals("H")) {
            return Sexe.HOMME;
        } else {
           return null;
        }
    }
}
