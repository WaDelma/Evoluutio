package Logiikka;

import Logiikka.Genomi.Gene;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

/**
 * Otuksen genomista kirjaa pitävä luokka, joka voi myös kopioida sen
 * tarvittaessa.
 *
 * @author lvapaaka
 */
public class Genomi implements Iterable<Entry<Gene, Integer>> {

    /**
     * Genomin sisältö, tulkitaan geeneiksi GenominTulkitsimessa
     */
    private final EnumMap<Gene, Integer> genes;

    public Genomi() {
        this.genes = new EnumMap<Gene, Integer>(Gene.class);
    }

    public Genomi(int[] geenit) {
        this();
        for (int i = 0; i < geenit.length; i++) {
            genes.put(Gene.values()[i], geenit[i]);
        }
    }

    public Genomi(Genomi genomi) {
        this();
        for (Entry<Gene, Integer> entry : genomi) {
            genes.put(entry.getKey(), entry.getValue().intValue());
        }

    }

    public int get(Gene gene) {
        Integer result = genes.get(gene);
        return result == null ? 0 : result;
    }

    public void setGene(Gene gene, int value) {
        genes.put(gene, value);
    }

    public int[] toArray() {
        Object[] from = genes.values().toArray();
        int[] result = new int[from.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (Integer) from[i];
        }
        return result;
    }

    @Override
    public Iterator<Entry<Gene, Integer>> iterator() {
        return genes.entrySet().iterator();
    }

    public static enum Gene {

        /**
         * Ensimmäinen Geeni, lisää (tai vähentää) haarautumiskulmaa.
         */
        ANGLE_CHANGE(-9, 9),
        /**
         * Toinen Geeni, pidentää (tai lyhentää) piirrettäviä viivoja.
         */
        LENGTH_CHANGE(-9, 9),
        /**
         * Kolmas Geeni, määrittää puun haarautumisten määrän välillä 0-18.
         */
        TREE_BRANCHING(0, 18),
        /**
         * Neljäs Geeni, jolla määritetään viivojen pituuteen vaikuttava
         * kerroin.
         */
        BRANCH_DIMINISHING(-9, 9),
        /**
         * Viides Geeni, joka määrittää otuksen leveyden kertoimen välillä (n.)
         * 0.1-2.0.
         */
        Y_GROWTH(-9, 9),
        /**
         * Kuudes Geeni, joka määrittää otuksen korkeuden kertoimen välillä (n.)
         * 0.1-2.0.
         */
        X_GROWTH(-9, 9),
        /**
         * Seitsemäs Geeni, joka muuttaa haarautumiskulmaa tulona haaran
         * suhteen.
         */
        TWISTING(-9, 9),
        /**
         * Kahdeksas Geeni, joka toimii kieroutumisen kaltaisesti, mutta
         * vahvemmin alussa ja heikommin lopussa.
         */
        ANTI_TWISTING(-9, 9),
        /**
         * Viimeinen Geeni, joka lisää puun haarautumista, eli oksien
         * lähtösuuntaa suhteessa toisiinsa.
         */
        BRANCHING_ANGLE(-9, 9),
        REDNESS(0, 255, 4, 16),
        GREENNES(0, 255, 4, 16),
        BLUENESS(0, 255, 4, 16),
        COLORSHIFT(-9, 9);

        public static Gene get(Random random) {
            return values()[random.nextInt(values().length)];
        }
        private final int min;
        private final int max;
        private final int minStep;
        private final int maxStep;

        private Gene(int min, int max, int minStep, int maxStep) {
            this.min = min;
            this.max = max;
            this.minStep = minStep;
            this.maxStep = maxStep;
        }

        private Gene(int min, int max) {
            this.min = min;
            this.max = max;
            this.minStep = 1;
            this.maxStep = 1;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public int getMinStep() {
            return minStep;
        }

        public int getMaxStep() {
            return maxStep;
        }
    }
}
