/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etf.backgammon.gl130213d.models;
import java.util.concurrent.ThreadLocalRandom;
/**
 *
 * @author lazar
 */
public class Dice {

    private int[][] lastTwoDiceValues = {{0, 0}, {0, 0}};

    private int diceOneValue = 0;
    private boolean diceOneUsed = false;

    private int diceTwoValue = 0;
    private boolean diceTwoUsed = false;

    public void roll() {
        do {
             
            diceOneValue = ThreadLocalRandom.current().nextInt(1, 6 + 1);
            diceTwoValue = ThreadLocalRandom.current().nextInt(1, 6 + 1);
        } while ((diceOneValue == lastTwoDiceValues[0][0]
                && diceOneValue == lastTwoDiceValues[1][0]
                && diceTwoValue == lastTwoDiceValues[0][1]
                && diceTwoValue == lastTwoDiceValues[1][1])
                || (diceOneValue == lastTwoDiceValues[0][1]
                && diceOneValue == lastTwoDiceValues[1][1]
                && diceTwoValue == lastTwoDiceValues[0][0]
                && diceTwoValue == lastTwoDiceValues[1][0])
                || (diceOneValue == lastTwoDiceValues[0][0]
                && diceOneValue == lastTwoDiceValues[1][1]
                && diceTwoValue == lastTwoDiceValues[0][1]
                && diceTwoValue == lastTwoDiceValues[1][0])
                || (diceOneValue == lastTwoDiceValues[0][1]
                && diceOneValue == lastTwoDiceValues[1][0]
                && diceTwoValue == lastTwoDiceValues[0][0]
                && diceTwoValue == lastTwoDiceValues[1][1]));
        lastTwoDiceValues[0][0] = lastTwoDiceValues[1][0];
        lastTwoDiceValues[0][1] = lastTwoDiceValues[1][1];
        lastTwoDiceValues[1][0] = diceOneValue;
        lastTwoDiceValues[1][1] = diceTwoValue;
        
        diceOneUsed = false;
        diceTwoUsed = false;
    }

    public int getDiceOneValue() {
        return diceOneValue;
    }

    public int getDiceTwoValue() {
        return diceTwoValue;
    }

    public boolean isDiceOneUsed() {
        return diceOneUsed;
    }

    public void setDiceOneUsed(boolean diceOneUsed) {
        this.diceOneUsed = diceOneUsed;
    }

    public boolean isDiceTwoUsed() {
        return diceTwoUsed;
    }

    public void setDiceTwoUsed(boolean diceTwoUsed) {
        this.diceTwoUsed = diceTwoUsed;
    }

}
