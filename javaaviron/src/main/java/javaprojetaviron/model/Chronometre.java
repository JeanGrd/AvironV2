package javaprojetaviron.model;

public class Chronometre {
    private float temps;

    public void running(float temps_limite) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long debut = System.currentTimeMillis();
                while (true) {
                    long temps_ecoule = System.currentTimeMillis() - debut;
                    temps = temps_ecoule / 1000f; // Convertir en secondes
                    if (temps >= temps_limite) {
                        break;
                    }
                }
            }
        });
        thread.start();
    }

    public float getTemps() {
        return temps;
    }
}
