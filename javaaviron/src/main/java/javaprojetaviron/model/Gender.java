package javaprojetaviron.model;

/**
 * Enumération pour les sexes possibles d'un participant.
 */
public enum Gender {

    FEMALE, MALE;

    /**
     * Méthode pour obtenir le sexe correspondant à une chaîne de caractères.
     * @param type La chaîne de caractères représentant le sexe ("F" pour femme, "H" pour homme)
     * @return L'objet Gender correspondant, ou null si la chaîne ne correspond à aucun sexe
     */
    public static Gender getGender(String type){
        if (type.equals("F")) {
            return Gender.FEMALE;
        } else if (type.equals("H")) {
            return Gender.MALE;
        } else {
            return null;
        }
    }
}
