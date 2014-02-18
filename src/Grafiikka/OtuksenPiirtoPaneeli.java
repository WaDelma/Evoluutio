/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Grafiikka;

import Logiikka.Genomi;
import Logiikka.Genomi.Gene;
import Logiikka.Mutaatio;
import Logiikka.Otus;
import Logiikka.Vector;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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
    /**
     * Otuksen piirtämiselle elintärkeä oksalista.
     */
    private LinkedList<Oksa> oksat = new LinkedList<Oksa>();
    /**
     * Kulmien määrittämisen mahdollistava perusarvo.
     */
    private double nearlyTenDegrees = (2 * Math.PI / 360.0) * 9.5;
    /**
     * Puun viivojen pituuteen vaikuttava perusarvo.
     */
    private int oletusPituus = 15;

    private Mutaatio mutaatio;

    public OtuksenPiirtoPaneeli(Otus otus) {
        mutaatio = new Mutaatio();
        this.otus = otus;
    }

    /**
     * Paneelin sisältämän otuksen päivittämisen mahdollistava setteri.
     *
     * @param otus
     */
    public void setOtus(Otus otus) {
        this.otus = otus;
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
        Genomi genome = otus.getGenomi();
        Oksa oksa = new Oksa(200, 150, 0, 0, otus);
        saadaVari(g, 1, oksa);
        g.drawLine(200, 150 + oletusPituus + 2 * (int) genome.get(Gene.LENGTH_CHANGE), 200, 150);
        oksat.addFirst(oksa);
        while (!oksat.isEmpty()) {
            seuraavatPisteet(g);
        }
        g.setColor(Color.BLACK);
        g.drawString(otus.toString(), 0, 10);
    }

    private float[] varit = {0, 0, 0};

    private void saadaVari(Graphics g, float kerroin, Oksa oksa) {
        Genomi genome = oksa.getOtus().getGenomi();
        Color.RGBtoHSB((int) genome.get(Gene.REDNESS), (int) genome.get(Gene.GREENNES), (int) genome.get(Gene.BLUENESS), varit);
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
        Genomi genome = oksa.getOtus().getGenomi();
        if (oksa.getHaara() <= genome.get(Gene.TREE_BRANCHING)) {
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
        Vector xyKasvu = xJayKasvu(oksa);
        for (double degree : getBranchDegrees(oksa)) {
            piirraJaLisaaOksa(oksa, degree, xyKasvu.getX(), xyKasvu.getY(), g);
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
    private List<Double> getBranchDegrees(Oksa oksa) {
        List<Double> result = new ArrayList<Double>();
        Genomi genome = oksa.getOtus().getGenomi();
        double angleChange = 9 * nearlyTenDegrees;
        angleChange += nearlyTenDegrees * genome.get(Gene.ANGLE_CHANGE);
        angleChange += (nearlyTenDegrees / 10) * (genome.get(Gene.TWISTING) * oksa.getHaara());
        angleChange += 2 * nearlyTenDegrees * (genome.get(Gene.ANTI_TWISTING) - oksa.getHaara());
        angleChange += genome.get(Gene.BRANCHING_ANGLE);
        int amount = (int) genome.get(Gene.AMOUNT_BRANCHES);
        double change = 2 * angleChange / (amount - 1);
        double m = 0;
        for (int i = 0; i < amount; i++) {
            result.add(oksa.getKulma() - angleChange + m);
            m += change;
        }
//        result.add(oksa.getKulma() + angleChange);
//        result.add(oksa.getKulma() - angleChange);
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
    private Vector xJayKasvu(Oksa oksa) {
        Vector result = new Vector();
        Genomi genome = oksa.getOtus().getGenomi();
        double branchDiminishing = (100 - genome.get(Gene.BRANCH_DIMINISHING) * oksa.getHaara() * 5.0) / 100.0;
        double pituus = (oletusPituus + (genome.get(Gene.LENGTH_CHANGE) * 2)) * branchDiminishing;
        result.setX(pituus * (5.5 + genome.get(Gene.X_GROWTH)) / 9.0);
        result.setY(pituus * (5.5 + genome.get(Gene.Y_GROWTH)) / 9.0);
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
     * @param graphics Grafiikka-olio
     * @param haara haaran järjestysnumero
     */
    private void piirraJaLisaaOksa(Oksa oksa, double xKasvu, double yKasvu, double kulma, Graphics graphics) {
        Genomi genome = oksa.getOtus().getGenomi();
        double loppux = oksa.getX() - (Math.sin(kulma) * xKasvu);
        double loppuy = oksa.getY() - (Math.cos(kulma) * yKasvu);
        float amount = (float) (100 - genome.get(Gene.COLORSHIFT) * oksa.getHaara() * 5.0) / 100.0f;
        saadaVari(graphics, amount, oksa);
        graphics.drawLine((int) oksa.getX(), (int) oksa.getY(), (int) loppux, (int) loppuy);
        Random random = new Random((int) genome.get(Gene.CHILDREN) ^ (int) (loppux * loppuy * kulma));
        Otus newOtus = mutaatio.mutatoi(oksa.getOtus(), random, 0.1 + 1.0 / oksa.getHaara());
        oksat.addFirst(new Oksa(loppux, loppuy, oksa.getHaara() + 1, kulma, newOtus));
    }
}
