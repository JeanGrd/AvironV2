package javaprojetaviron.model;

import java.util.Random;

public class Senseur {
    private float distance;

    public Senseur(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return distance;
    }

    public float genererTemps() {
        Random rand = new Random();
        // On génère un nombre aléatoire compris entre 0.25 et 0.35
        // pour simuler une certaine variabilité dans les temps de parcours
        float randFloat = 0.25f + rand.nextFloat() * 0.15f;
        // On calcule le temps en fonction de la distance et du nombre aléatoire
        float temps = distance * randFloat;
        // On retourne le temps généré
        return temps;
    }

}