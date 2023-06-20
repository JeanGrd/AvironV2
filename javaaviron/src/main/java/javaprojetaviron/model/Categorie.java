package javaprojetaviron.model;

/**
 * Enum représentant les différentes catégories dans le système.
 */
public enum Categorie {
    MINIME(1500, 10),
    CADET (3000, 14),
    JUNIOR(4000, 18),
    SENIOR(6000, 40),
    VETERANT(6000, 200);

    // Distance maximale en mètres que les participants de cette catégorie peuvent parcourir
    private int maxDistanceM;
    // Âge maximal des participants de cette catégorie
    private int maxAge;

    public int getMaxDistanceM() {
        return maxDistanceM;
    }

    public int getMaxAge() {
        return maxAge;
    }

    /**
     * Constructeur pour l'énumération Category.
     * @param maxDistanceM La distance maximale que les participants de cette catégorie peuvent parcourir
     * @param maxAge L'âge maximal des participants de cette catégorie
     */
    Categorie(int maxDistanceM, int maxAge) {
        this.maxDistanceM = maxDistanceM;
        this.maxAge = maxAge;
    }

    /**
     * Méthode pour obtenir la catégorie correspondante à une chaîne de caractères donnée.
     * @param type Le type de catégorie à obtenir (par exemple, "M" pour MINIME, "C" pour CADET, etc.)
     * @return La catégorie correspondante, ou null si aucune catégorie ne correspond
     */
    public static Categorie getCategorie(String type){
        return switch (type) {
            case "M" -> Categorie.MINIME;
            case "C" -> Categorie.CADET;
            case "J" -> Categorie.JUNIOR;
            case "S" -> Categorie.SENIOR;
            case "V" -> Categorie.VETERANT;
            default -> null;
        };
    }
}
