package javaprojetaviron.model;
import java.util.ArrayList;

/**
 * Une ArrayList avec une taille maximale.
 *
 * @param <E> Le type des éléments de la liste.
 */
public class MaxSizeArrayList<E> extends ArrayList<E> {

    /**
     * La taille maximale de la liste.
     */
    private int maxSize;

    /**
     * Constructeur pour créer une nouvelle MaxSizeArrayList.
     *
     * @param maxSize La taille maximale de la liste.
     */
    public MaxSizeArrayList(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    /**
     * Ajoute un nouvel élément à la liste. Si la taille de la liste atteint sa taille maximale, une exception est levée.
     *
     * @param e L'élément à ajouter.
     * @return Vrai si l'élément a été ajouté avec succès, sinon une exception est levée.
     * @throws RuntimeException si la taille maximale de la liste est atteinte.
     */
    public boolean add(E e) {
        if (size() < maxSize) {
            return super.add(e);
        }
        throw new RuntimeException("Nombre total d'embarcations est dépassé !");
    }
}
