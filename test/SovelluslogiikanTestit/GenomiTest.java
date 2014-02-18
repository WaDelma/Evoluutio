/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SovelluslogiikanTestit;

import Logiikka.Genomi;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author lvapaaka
 */
public class GenomiTest {

    private Genomi genomi;

    @Before
    public void setUp() {
        int[] geenit = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        genomi = new Genomi(geenit);
    }

    @Test
    public void genominKopiointiLuoAidonKopion() {
        Genomi genominKopio = new Genomi(genomi);
        assertFalse(genominKopio == genomi);
    }
}
