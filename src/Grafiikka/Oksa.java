/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Grafiikka;

import Logiikka.Genomi;
import Logiikka.Otus;

/**
 * Koordinaatistopistettä, haarasukupolvea ja tulokulmaa säilövä olio, jonka
 * avulla Otukset piirretään.
 *
 * @author Lassi
 */
public class Oksa {

    /**
     * x-koordinaatin arvo.
     */
    private double x;
    /**
     * y-koordinaatin arvo.
     */
    private double y;
    /**
     * Monettako haaraa piirretään.
     */
    private int haara;
    /**
     * Tulokulma pisteeseen.
     */
    private double kulma;
    private final Otus otus;

    public Oksa(double x, double y, int haara, double kulma, Otus genome) {
        this.otus = genome;
        this.x = x;
        this.y = y;
        this.haara = haara;
        this.kulma = kulma;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getHaara() {
        return haara;
    }

    public double getKulma() {
        return kulma;
    }

    public Otus getOtus() {
        return otus;
    }
}
