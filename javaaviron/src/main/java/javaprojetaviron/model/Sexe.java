package javaprojetaviron.model;

/**
 * Enumération pour les sexes possibles d'un participant.
 */
public enum Sexe {

    FEMME, HOMME;

    /**
     * Méthode pour obtenir le sexe correspondant à une chaîne de caractères.
     * @param type La chaîne de caractères représentant le sexe ("F" pour femme, "H" pour homme)
     * @return L'objet Gender correspondant, ou null si la chaîne ne correspond à aucun sexe
     */
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
