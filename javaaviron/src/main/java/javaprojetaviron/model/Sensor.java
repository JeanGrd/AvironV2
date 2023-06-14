package javaprojetaviron.model;

import java.util.Random;

/**
 * Classe représentant un senseur dans le système.
 */
public class Sensor {
    private float distance;

    /**
     * Constructeur pour la classe Sensor.
     * @param distance La distance à parcourir
     */
    public Sensor(float distance) {
        this.distance = distance;
    }

    /**
     * Méthode pour obtenir la distance à parcourir.
     * @return La distance à parcourir
     */
    public float getDistance() {
        return distance;
    }

    /**
     * Méthode pour générer un temps de parcours aléatoire.
     * @return Le temps de parcours généré
     */
    public float generateTime() {
        Random rand = new Random();
        // On génère un nombre aléatoire compris entre 0.25 et 0.35
        // pour simuler une certaine variabilité dans les temps de parcours
        float randFloat = 0.17f + rand.nextFloat() * 0.12f;
        // On calcule le temps en fonction de la distance et du nombre aléatoire
        float time = distance * randFloat;
        // On retourne le temps généré
        return time;
    }

}
