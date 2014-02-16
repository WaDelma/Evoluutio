/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logiikka;

import Logiikka.Genomi.Gene;
import java.util.ArrayList;
import java.util.Random;

/**
 * Luokka joka suorittaa Genomien mutaation ja jälkeläisten tuottamisen.
 *
 * @author lvapaaka
 */
public class Mutaatio {

    /**
     * Genomia ja mutaatiota arpova Random.
     */
    private Random arpoja = new Random();
    /**
     * Halutun jälkeläismäärän määrittävä arvo. Enintään 18, koska se on
     * maksimaalinen permutaatioiden määrä.
     */
    private int jalkelaistenMaara = 8;

    public Mutaatio() {
    }

    public Mutaatio(int jalkelaistenMaara) {
        this.jalkelaistenMaara = jalkelaistenMaara;
    }

    /**
     * Metodi muuttaa halutun Otuksen Genomin sattumanvaraista geeniä, joko +1
     * tai -1:llä käyttäen apumetodia teeMutaatioIndeksiin
     *
     * @see Logiikka.Mutaattori#teeMutaatioIndeksiin(Logiikka.Genomi, int)
     *
     * @param vanhempi Otus josta halutaan tehdä mutatoitu versio
     * @return
     */
    public Otus mutatoi(Otus vanhempi) {
        Genomi mutatoitavaGenomi = new Genomi(vanhempi.getGenomi());
        teeMutaatioIndeksiin(mutatoitavaGenomi, Gene.get(arpoja));
        return new Otus(mutatoitavaGenomi);
    }

    /**
     * Metodi suorittaa mutaation haluttuun geeniin, ja varmistaa että se ei
     * ylitä maksimiarvoa.
     *
     * @param genomi muutettava genomi
     * @param gene mutatoitavan geenin indeksinumero genomissa.
     */
    private void teeMutaatioIndeksiin(Genomi geenit, Gene gene) {
        int mutaatio = geenit.get(gene);
        mutaatio += arpoja.nextBoolean() ? -gene.getStep() : gene.getStep();
        if(mutaatio < gene.getMin()){
            mutaatio = gene.getMin();
        }
        if(mutaatio > gene.getMax()){
            mutaatio = gene.getMax();
        }
        geenit.setGene(gene, mutaatio);
    }

    /**
     * Metodi kutsuu mutatoi-metodia, kunnes se saa Mutaatiolle määritetyn
     * määrän erilaisia jälkeläisiä ja sitten lisää "emon" listan viimeiseksi.
     *
     * @see Logiikka.Mutaatio#eiOleJoJalkelaisissa(Logiikka.Otus,
     * java.util.ArrayList)
     *
     * @param vanhempi Otus, jonka jälkeläiset halutaan saada
     * @return halutun kokoinen lista jälkeläisiä ja "emo"
     */
    public ArrayList<Otus> jalkelaisetJaVanhempi(Otus vanhempi) {
        ArrayList<Otus> jalkelaiset = new ArrayList<Otus>();
        Otus otus;
        for (int i = 1; i < (jalkelaistenMaara + 1); i++) {
            while (jalkelaiset.size() != i) {
                otus = mutatoi(vanhempi);
                if (eiOleJoJalkelaisissa(otus, jalkelaiset)) {
                    jalkelaiset.add(otus);
                }
            }
        }
        jalkelaiset.add(vanhempi);
        return jalkelaiset;
    }

    /**
     * Metodi jolla varmistetaan, että jälkeläiset eroavat toisistaan, tämä
     * tehdään käyttäen otusten toString-metodia, joka on identtinen genomiltaan
     * samanlaisten otusten kanssa.
     *
     * @param otus jälkeläiskandidaatti
     * @param jalkelaiset lista tähän mennessä hyväksytyistä jälkeläisistä
     * @return palauttaa false, jos vastaava Olio on jo jälkeläisissä, true jos
     * ei.
     */
    private boolean eiOleJoJalkelaisissa(Otus otus, ArrayList<Otus> jalkelaiset) {
        for (Otus elio : jalkelaiset) {
            if (otus.toString().equals(elio.toString())) {
                return false;
            }
        }
        return true;
    }
}
