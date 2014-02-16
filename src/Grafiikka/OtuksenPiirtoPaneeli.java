/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Grafiikka;

import Logiikka.Genomi;
import Logiikka.Genomi.Gene;
import Logiikka.Otus;
import Logiikka.Vector;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;

/**
 * Otusten piirtämisen hoitava JPanel-luokka.
 *
 * @author lvapaaka
 */
public class OtuksenPiirtoPaneeli extends JPanel {

    /**
     * Piirrettävä otus.
     */
    private Otus otus;
    private Genomi genome;
    /**
     * Otuksen piirtämiselle elintärkeä oksalista.
     */
    private LinkedList<Oksa> oksat = new LinkedList<Oksa>();
    /**
     * Kulmien määrittämisen mahdollistava perusarvo.
     */
    private double kymmenisenAstetta = (Math.PI / 180) * 9.5;
    /**
     * Puun viivojen pituuteen vaikuttava perusarvo.
     */
    private int oletusPituus = 15;

    public OtuksenPiirtoPaneeli(Otus otus) {
        this.otus = otus;
        this.genome = otus.getGenomi();
    }

    /**
     * Paneelin sisältämän otuksen päivittämisen mahdollistava setteri.
     *
     * @param otus
     */
    public void setOtus(Otus otus) {
        this.otus = otus;
        this.genome = otus.getGenomi();
    }

    /**
     * Metodi jossa otuksen puurakenne määritetään listan avulla piste pisteeltä
     * käyttäen apumetodia seuraavatPisteet
     *
     * @see Logiikka.GenominTulkitsin#seuraavatPisteet(java.util.LinkedList)
     *
     * @return palauttaa (tyhjän) listan. Ilman syytä?
     */
    @Override
    public void paintComponent(Graphics g) {
        saadaVari(g, 1);


        g.drawLine(200, 150 + (oletusPituus + (genome.get(Gene.LENGTH_CHANGE) * 2)), 200, 150);
        oksat.addFirst(new Oksa(200, 150, 0, 0));
        while (oksat.peekFirst() != null) {
            seuraavatPisteet(g);
        }
    }

    private void saadaVari(Graphics g, float kerroin) {
        float[] varit = Color.RGBtoHSB(genome.get(Gene.REDNESS), genome.get(Gene.GREENNES), genome.get(Gene.BLUENESS), null);
        float savy = varit[0] * kerroin;
        float satu = varit[1] * kerroin;
        float kirk = varit[2] * kerroin;
        g.setColor(Color.getHSBColor(savy, satu, kirk));
    }

    /**
     * Metodi, joka ottaa listan ensimmäisen Oksan, tarkistaa haaramäärän ja
     * toimii "portsarina" piirtometodeille.
     *
     * @param g grafiikka-Olio
     */
    private void seuraavatPisteet(Graphics g) {
        Oksa oksa = oksat.poll();
        int haara = oksa.getHaara();
        if (!(haara > genome.get(Gene.TREE_BRANCHING))) {
            piirraHaarat(g, oksa);
        }
    }

    /**
     * Metodi, joka kerää piirtämiseen tarvittavat arvot oksalta ja
     * apumetodeilta, jotka se lähettää lopulliselle piirtometodille.
     *
     * @param g Grafiikka-olio
     * @param oksa listasta vedetty ensimmäinen oksa.
     */
    private void piirraHaarat(Graphics g, Oksa oksa) {
        int haara = oksa.getHaara();
        double kulma = oksa.getKulma();
        Vector xyKasvu = xJayKasvu(haara);
        List<Double> degrees = vasenJaOikeaKulma(kulma, haara);
        for (double degree : degrees) {
            piirraJaLisaaOksa(oksa.getX(), oksa.getY(), degree, xyKasvu.getX(), xyKasvu.getY(), g, haara);
        }
    }

    /**
     * Metodi, joka palauttaa vasemman ja oikean kulman arvot taulukkona.
     *
     * Metodi käyttää Otuksen geenejä numerot: 1: kulmanMuutos 7: kieroutuminen
     * 8: käänteiskieroutuminen 9: haarautuvuus
     *
     * @param kulma edellisen oksan kulma, johon lisäämällä ja vähentämällä uusi
     * kulma saadaan.
     * @param haara käsiteltävän haaran järjestysnumero
     * @return kaksipaikkainen taulukko [vasen kulma, oikea kulma]
     */
    private List<Double> vasenJaOikeaKulma(double kulma, int haara) {
        List<Double> result = new ArrayList<Double>();
        double haaraKohtainenKulmanMuutos = (9 * kymmenisenAstetta)
                + (kymmenisenAstetta * genome.get(Gene.ANGLE_CHANGE))
                + ((kymmenisenAstetta / 10) * (genome.get(Gene.TWISTING) * haara))
                + (kymmenisenAstetta * 2 * (genome.get(Gene.ANTI_TWISTING) - haara));
        result.add(kulma + haaraKohtainenKulmanMuutos + genome.get(Gene.BRANCHING_ANGLE));
        result.add(kulma - haaraKohtainenKulmanMuutos - genome.get(Gene.BRANCHING_ANGLE));
        return result;
    }

    /**
     * Metodi, joka palauttaa x:n ja y:n kasvumäärät taulukossa.
     *
     * Metodi käyttää Otuksen geenejä numerot: 2: pituudenMuutos 4:
     * oksienLyhenemisVauhti 5: xnkasvunVahvistin 6: ynkasvunVahvistin
     *
     * @param haara käsiteltävän haaran järjestysnumero
     * @return kaksipaikkainen taulukko [xKasvu,yKasvu]
     */
    private Vector xJayKasvu(int haara) {
        Vector result = new Vector();
        double oksienLyhenemisKerroin = ((100 - (genome.get(Gene.BRANCH_DIMINISHING) * haara * 5.0)) / 100.0);
        double pituus = (oletusPituus + (genome.get(Gene.LENGTH_CHANGE) * 2)) * oksienLyhenemisKerroin;
        result.setX(pituus * ((5.5 + genome.get(Gene.X_GROWTH)) / 9.0));
        result.setY(pituus * ((5.5 + genome.get(Gene.Y_GROWTH)) / 9.0));
        return result;
    }

    /**
     * Hyvin paljon arvoja kaipaava puunoksia eli viivoja piirtävä metodi.
     * Metodi myös lisää listaan uuden Oksan.
     *
     * @param alkux edellisen oksan loppupisteen x:n arvo
     * @param alkuy edellisen oksan loppupisteen y:n arvo
     * @param kulma kulma jossa viiva piirretään
     * @param xKasvu x:n pituuden määrittävä arvo
     * @param yKasvu y:n pituuden määrittävä arvo
     * @param g Grafiikka-olio
     * @param haara haaran järjestysnumero
     */
    private void piirraJaLisaaOksa(double alkux, double alkuy, double kulma, double xKasvu, double yKasvu, Graphics g, int haara) {
        double loppux = alkux - (Math.sin(kulma) * xKasvu);
        double loppuy = alkuy - (Math.cos(kulma) * yKasvu);
        saadaVari(g, (float) (1 * ((100 - (genome.get(Gene.COLORSHIFT) * haara * 5.0)) / 100.0)));
        g.drawLine((int) alkux, (int) alkuy, (int) loppux, (int) loppuy);
//        g.drawString(otus.toString(), 0, 10);
        oksat.addFirst(new Oksa(loppux, loppuy, haara + 1, kulma));
    }
}
