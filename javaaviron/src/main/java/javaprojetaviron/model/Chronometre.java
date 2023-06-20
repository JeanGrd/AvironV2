package javaprojetaviron.model;

/**
 * Classe pour gérer un chronomètre.
 */
public class Chronometre {
    // Temps enregistré par le chronomètre en secondes
    private float temps;
    // Thread pour gérer le chronomètre
    private Thread thread;

    /**
     * Méthode pour démarrer le chronomètre.
     */
    public void running() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                // Boucle tant que le thread n'est pas interrompu
                while (!Thread.interrupted()) {
                    // Calcul du temps écoulé en millisecondes
                    long elapsedMillis = System.currentTimeMillis() - startTime;
                    // Convertir en secondes
                    temps = elapsedMillis / 1000f;
                }
            }
        });
        thread.start();
    }

    /**
     * Méthode pour arrêter le chronomètre.
     */
    public void stop() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    /**
     * Méthode pour obtenir le temps enregistré par le chronomètre.
     * @return le temps enregistré en secondes
     */
    public float getTemps() {
        return temps;
    }
}
