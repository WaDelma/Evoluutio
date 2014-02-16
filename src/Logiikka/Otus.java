/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logiikka;

import Logiikka.Genomi.Gene;
import java.util.Map.Entry;

/**
 * Ohjelman kohde ja perusyksikkö, Genomin sisältävä luokka, joita
 * OtuksenPiirtoPaneeli piirtää.
 *
 * @see Logiikka.Genomi
 * @see Grafiikka.OtuksenPiirtoPaneeli
 * @author lvapaaka
 */
public class Otus {

    /**
     * Koko Otus perustuu sen Genomiin.
     */
    private Genomi genomi;

    public Otus(Genomi genomi) {
        this.genomi = genomi;
    }

    /**
     * Palauttaa otuksen genomin
     *
     * @return palautettava Genomi
     */
    public Genomi getGenomi() {
        return genomi;
    }

    /**
     * Otuksesta printataaan jokaisen geenin arvo.
     *
     * @return muotoa (x,x,x,x,x,x,x,x,x)
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Entry<Gene, Integer> gene : genomi) {
            builder.append(gene.getValue());
            builder.append(",");
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}
