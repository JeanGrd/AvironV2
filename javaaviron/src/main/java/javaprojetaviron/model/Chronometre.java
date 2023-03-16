package javaprojetaviron.model;

public class Chronometre {
    private float temps;
    private Thread thread;

    public void running() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long debut = System.currentTimeMillis();
                while (!Thread.interrupted()) {
                    long temps_ecoule = System.currentTimeMillis() - debut;
                    temps = temps_ecoule / 1000f; // Convertir en secondes
                }
            }
        });
        thread.start();
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    public float getTemps() {
        return temps;
    }
}