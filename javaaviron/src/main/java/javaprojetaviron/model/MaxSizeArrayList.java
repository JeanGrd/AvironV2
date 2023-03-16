package javaprojetaviron.model;

import java.util.ArrayList;

public class MaxSizeArrayList<E> extends ArrayList<E> {
    private int maxSize;

    public MaxSizeArrayList(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    public boolean add(E e) {
        if (size() < maxSize) {
            return super.add(e);
        }
        throw new RuntimeException("Nombre total d'embarcations est dépassé !");
    }
}