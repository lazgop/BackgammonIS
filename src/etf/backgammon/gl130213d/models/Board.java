/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etf.backgammon.gl130213d.models;

import etf.backgammon.gl130213d.controllers.GameController;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author lazar
 */
public class Board {

    private static int[][][] MAPPING = { //First pair are grid coordinates, second pair are virtual table stripe coordinates
        {{10, 12}, {0, 0}},
        {{9, 12}, {0, 1}},
        {{8, 12}, {0, 2}},
        {{7, 12}, {0, 3}},
        {{6, 12}, {0, 4}},
        {{10, 11}, {1, 0}},
        {{9, 11}, {1, 1}},
        {{8, 11}, {1, 2}},
        {{7, 11}, {1, 3}},
        {{6, 11}, {1, 4}},
        {{10, 10}, {2, 0}},
        {{9, 10}, {2, 1}},
        {{8, 10}, {2, 2}},
        {{7, 10}, {2, 3}},
        {{6, 10}, {2, 4}},
        {{10, 9}, {3, 0}},
        {{9, 9}, {3, 1}},
        {{8, 9}, {3, 2}},
        {{7, 9}, {3, 3}},
        {{6, 9}, {3, 4}},
        {{10, 8}, {4, 0}},
        {{9, 8}, {4, 1}},
        {{8, 8}, {4, 2}},
        {{7, 8}, {4, 3}},
        {{6, 8}, {4, 4}},
        {{10, 7}, {5, 0}},
        {{9, 7}, {5, 1}},
        {{8, 7}, {5, 2}},
        {{7, 7}, {5, 3}},
        {{6, 7}, {5, 4}},
        {{10, 5}, {6, 0}},
        {{9, 5}, {6, 1}},
        {{8, 5}, {6, 2}},
        {{7, 5}, {6, 3}},
        {{6, 5}, {6, 4}},
        {{10, 4}, {7, 0}},
        {{9, 4}, {7, 1}},
        {{8, 4}, {7, 2}},
        {{7, 4}, {7, 3}},
        {{6, 4}, {7, 4}},
        {{10, 3}, {8, 0}},
        {{9, 3}, {8, 1}},
        {{8, 3}, {8, 2}},
        {{7, 3}, {8, 3}},
        {{6, 3}, {8, 4}},
        {{10, 2}, {9, 0}},
        {{9, 2}, {9, 1}},
        {{8, 2}, {9, 2}},
        {{7, 2}, {9, 3}},
        {{6, 2}, {9, 4}},
        {{10, 1}, {10, 0}},
        {{9, 1}, {10, 1}},
        {{8, 1}, {10, 2}},
        {{7, 1}, {10, 3}},
        {{6, 1}, {10, 4}},
        {{10, 0}, {11, 0}},
        {{9, 0}, {11, 1}},
        {{8, 0}, {11, 2}},
        {{7, 0}, {11, 3}},
        {{6, 0}, {11, 4}},
        {{0, 0}, {12, 0}},
        {{1, 0}, {12, 1}},
        {{2, 0}, {12, 2}},
        {{3, 0}, {12, 3}},
        {{4, 0}, {12, 4}},
        {{0, 1}, {13, 0}},
        {{1, 1}, {13, 1}},
        {{2, 1}, {13, 2}},
        {{3, 1}, {13, 3}},
        {{4, 1}, {13, 4}},
        {{0, 2}, {14, 0}},
        {{1, 2}, {14, 1}},
        {{2, 2}, {14, 2}},
        {{3, 2}, {14, 3}},
        {{4, 2}, {14, 4}},
        {{0, 3}, {15, 0}},
        {{1, 3}, {15, 1}},
        {{2, 3}, {15, 2}},
        {{3, 3}, {15, 3}},
        {{4, 3}, {15, 4}},
        {{0, 4}, {16, 0}},
        {{1, 4}, {16, 1}},
        {{2, 4}, {16, 2}},
        {{3, 4}, {16, 3}},
        {{4, 4}, {16, 4}},
        {{0, 5}, {17, 0}},
        {{1, 5}, {17, 1}},
        {{2, 5}, {17, 2}},
        {{3, 5}, {17, 3}},
        {{4, 5}, {17, 4}},
        {{0, 7}, {18, 0}},
        {{1, 7}, {18, 1}},
        {{2, 7}, {18, 2}},
        {{3, 7}, {18, 3}},
        {{4, 7}, {18, 4}},
        {{0, 8}, {19, 0}},
        {{1, 8}, {19, 1}},
        {{2, 8}, {19, 2}},
        {{3, 8}, {19, 3}},
        {{4, 8}, {19, 4}},
        {{0, 9}, {20, 0}},
        {{1, 9}, {20, 1}},
        {{2, 9}, {20, 2}},
        {{3, 9}, {20, 3}},
        {{4, 9}, {20, 4}},
        {{0, 10}, {21, 0}},
        {{1, 10}, {21, 1}},
        {{2, 10}, {21, 2}},
        {{3, 10}, {21, 3}},
        {{4, 10}, {21, 4}},
        {{0, 11}, {22, 0}},
        {{1, 11}, {22, 1}},
        {{2, 11}, {22, 2}},
        {{3, 11}, {22, 3}},
        {{4, 11}, {22, 4}},
        {{0, 12}, {23, 0}},
        {{1, 12}, {23, 1}},
        {{2, 12}, {23, 2}},
        {{3, 12}, {23, 3}},
        {{4, 12}, {23, 4}}
    };

    final private int STARTING_FIELDS[][][][] = {{
        {{10, 12}, {0, 0}},
        {{9, 12}, {0, 1}},
        {{10, 0}, {11, 0}},
        {{9, 0}, {11, 1}},
        {{8, 0}, {11, 2}},
        {{7, 0}, {11, 3}},
        {{6, 0}, {11, 4}},
        {{0, 4}, {16, 0}},
        {{1, 4}, {16, 1}},
        {{2, 4}, {16, 2}},
        {{0, 7}, {18, 0}},
        {{1, 7}, {18, 1}},
        {{2, 7}, {18, 2}},
        {{3, 7}, {18, 3}},
        {{4, 7}, {18, 4}}
    }, {
        {{0, 12}, {23, 0}},
        {{1, 12}, {23, 1}},
        {{0, 0}, {12, 0}},
        {{1, 0}, {12, 1}},
        {{2, 0}, {12, 2}},
        {{3, 0}, {12, 3}},
        {{4, 0}, {12, 4}},
        {{10, 4}, {7, 0}},
        {{9, 4}, {7, 1}},
        {{8, 4}, {7, 2}},
        {{10, 7}, {5, 0}},
        {{9, 7}, {5, 1}},
        {{8, 7}, {5, 2}},
        {{7, 7}, {5, 3}},
        {{6, 7}, {5, 4}}
    }}; //Red and white
    
    public void calculateAllowedPositionsForDice(boolean isFirst,
            int diceValue,
            int spikeRow,
            boolean isCurrentPlayerWhite,
            int[][] allowedFields,
            Token[][] spikes,
            int[] allowedFieldPoints) {
        
        int spiker = isCurrentPlayerWhite ? spikeRow - diceValue : spikeRow + diceValue;
        String currentPlayerColor = isCurrentPlayerWhite ? GameController.FILL_WHITE : GameController.FILL_RED;
        int allFieldsRow = isFirst ? 0 : 1;
        if (spiker >= 0 && spiker < 24) {
            if (spikes[spiker][0] == null) { // If empty row
                System.out.println("row empty" + spiker);
                allowedFields[allFieldsRow][0] = getRowSpikeToToken(spiker, 0);
                allowedFields[allFieldsRow][1] = getColumnSpikeToToken(spiker, 0);
                allowedFieldPoints[allFieldsRow] = diceValue;
            } else if (spikes[spiker][0].getCircle().getFill().toString().equals(currentPlayerColor) == false
                    && spikes[spiker][1] == null) { // If only one enemy token in a row
                
                
                System.out.println("only one enemy " + spiker + "color " + spikes[spiker][0].getCircle().getFill().toString() + " playerColor" + currentPlayerColor);
                allowedFields[allFieldsRow][0] = getRowSpikeToToken(spiker, 0);
                allowedFields[allFieldsRow][1] = getColumnSpikeToToken(spiker, 0);
                allowedFieldPoints[allFieldsRow] = isCurrentPlayerWhite ? 24 - spiker : spiker;
            } else { // Check if all are friend tokens and if there is available spot
                System.out.println("check if there is a free field on top of friends" + spiker);
                int firstEmptySpikeColumn = 0;
                while (spikes[spiker][firstEmptySpikeColumn] != null 
                        && spikes[spiker][firstEmptySpikeColumn].getCircle().getFill().toString().equals(currentPlayerColor)
                        && firstEmptySpikeColumn != 5) {
                    System.out.println("boja1 : " + spiker + " " + firstEmptySpikeColumn + " " + spikes[spiker][firstEmptySpikeColumn].getCircle().getFill().toString() + " boja2 " + currentPlayerColor);
                    firstEmptySpikeColumn++;
                }
                
                if (firstEmptySpikeColumn != 0) {
                    allowedFields[allFieldsRow][0] = getRowSpikeToToken(spiker, firstEmptySpikeColumn);
                    allowedFields[allFieldsRow][1] = getColumnSpikeToToken(spiker, firstEmptySpikeColumn);
                    allowedFieldPoints[allFieldsRow] = diceValue;
                }
            }
        }
    }

    public int getRowSpikeToToken(int spikeRow, int spikeColumn) {
        for (int i = 0; i < 120; i++) {
            if (MAPPING[i][1][0] == spikeRow && MAPPING[i][1][1] == spikeColumn) {
                return MAPPING[i][0][0];
            }
        }
        return -1;
    }

    public int getColumnSpikeToToken(int spikeRow, int spikeColumn) {
        for (int i = 0; i < 120; i++) {
            if (MAPPING[i][1][0] == spikeRow && MAPPING[i][1][1] == spikeColumn) {
                return MAPPING[i][0][1];
            }
        }
        return -1;
    }

    public int getRowTokenToSpike(int tokenRow, int tokenColumn) {
        for (int i = 0; i < 120; i++) {
            if (MAPPING[i][0][0] == tokenRow && MAPPING[i][0][1] == tokenColumn) {
                return MAPPING[i][1][0];
            }
        }
        return -1;
    }

    public int getColumnTokenToSpike(int tokenRow, int tokenColumn) {
        for (int i = 0; i < 120; i++) {
            if (MAPPING[i][0][0] == tokenRow && MAPPING[i][0][1] == tokenColumn) {
                return MAPPING[i][1][1];
            }
        }
        return -1;
    }
    
    public void setColor(Circle circle, String color){
        switch(color) {
            case "white":{
                circle.setFill(Color.web("0xffffffff"));
                break;
            }
            case "red":{
                circle.setFill(Color.web("0xff0000ff"));
                break;
            }
            case "blank":{
                circle.setFill(Color.web("0x11111100"));
                break;
            }
            default: {
                System.out.println("ERROR IN COLOR");
            }
        }
    }
    
    public boolean compareColors(String c1, String c2){
        return c1.equals(c2);
    }
}
