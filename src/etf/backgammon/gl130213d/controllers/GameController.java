/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etf.backgammon.gl130213d.controllers;

import etf.backgammon.gl130213d.models.Board;
import etf.backgammon.gl130213d.models.Dice;
import etf.backgammon.gl130213d.models.Token;
import etf.backgammon.gl130213d.wrappers.SceneWrapper;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author lazar
 */
public class GameController implements Initializable {

    final String[] STAGE = {"Roll the dice",
        "Select first token",
        "Select the field where to move selected token",
        "Select second token",
        "Select the field where to move the second token"
    };

    final String FILL_BLANK = "0x11111100";
    final String FILL_WHITE = "0xffffffff";
    final String FILL_RED = "0xff0000ff";

    private boolean colorRed;
    private boolean enemyComputer;
    private int matchPoints;
    private int treeDepth;

    private Dice dice;

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

    int selectedRow = -1;
    int selectedColumn = -1;

    private Token[][] spikes = new Token[24][6];
    private Token[][] tokens = new Token[11][13];

    private int playerRedPoints = 167;
    private int playerWhitePoints = 167; //-br polja, -broj predjenih polja od pocetka table na kom si pojeo protivnika, + ovo prethodno ako si ti pojeden

    private int[][] allowedFields = {{-1, -1}, {-1, -1}}; //first dice, second dice
    private int[] allowedFieldPoints = {-1, -1};//first dice, second dice

    private Board board = new Board();

    @FXML
    private TreeView treeView;
    @FXML
    private Button diceButton;
    @FXML
    private Button setUpButton;
    @FXML
    private Label nowPlayingLabel;
    @FXML
    private Label pointsRedLabel;
    @FXML
    private Label pointsWhiteLabel;
    @FXML
    private Label diceOneLabel;
    @FXML
    private Label diceTwoLabel;
    @FXML
    private Label stageLabel;
    @FXML
    private GridPane boardGrid;
    @FXML
    private Label whiteBarLabel;
    @FXML
    private Label redBarLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void handleSetUpButtonAction(ActionEvent event) {
        SceneWrapper scene = null;
        if (setUpButton.getScene() instanceof SceneWrapper) {
            scene = (SceneWrapper) setUpButton.getScene();
        } else {
            System.out.println("ERROR");
            return;
        }
        colorRed = scene.isColorRed();
        enemyComputer = scene.isEnemyComputer();
        matchPoints = Integer.parseInt(scene.getMatchPoints());
        treeDepth = scene.getTreeDepth();
        for (Node element : boardGrid.getChildren()) {

            //Set initial colors of all tokens to
            Integer columnIndex = GridPane.getColumnIndex(element);
            Integer rowIndex = GridPane.getRowIndex(element);

            if (!((columnIndex == 6 && rowIndex == 4) || (columnIndex == 6 && rowIndex == 6))) { //Don't change color of bar tokens
                ((Circle) element).setFill(Color.web(FILL_BLANK));
                tokens[rowIndex][columnIndex] = new Token((Circle) element);
            }

            //Set onClick listeners
            element.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    handleTokenClick(event, element);
                }
            });
        }

        //Set color of red and white tokens
        for (int color = 0; color < 2; color++) {
            for (int i = 0; i < 15; i++) {
                int rowIndex = STARTING_FIELDS[color][i][0][0];
                int columnIndex = STARTING_FIELDS[color][i][0][1];
                tokens[rowIndex][columnIndex].getCircle().setFill(Color.web(color == 0 ? FILL_RED : FILL_WHITE));
                int rowIndexS = STARTING_FIELDS[color][i][1][0];
                int columnIndexS = STARTING_FIELDS[color][i][1][1];
                spikes[rowIndexS][columnIndexS] = tokens[rowIndex][columnIndex];
            }
        }

        playerRedPoints = 167;
        playerWhitePoints = 167;

        dice = new Dice();

        nowPlayingLabel.setText(colorRed ? "Red" : "White");
        pointsRedLabel.setText(playerRedPoints + "");
        pointsWhiteLabel.setText(playerWhitePoints + "");
        stageLabel.setText(STAGE[0]);
        diceOneLabel.setText(0 + "");
        diceTwoLabel.setText(0 + "");

        setUpButton.setText("Restart game");
    }

    @FXML
    private void handleDiceButtonAction(ActionEvent event) {
        if (stageLabel.getText().equals(STAGE[0])) {

            dice.roll();

            diceOneLabel.setText(dice.getDiceOneValue() + "");
            diceTwoLabel.setText(dice.getDiceTwoValue() + "");

            stageLabel.setText(STAGE[1]);
        }
    }

    private void handleTokenClick(MouseEvent event, Node element) {
        int rowIndex = GridPane.getRowIndex(element);
        int columnIndex = GridPane.getColumnIndex(element);

        String currentPlayerColor = nowPlayingLabel.getText().equals("White") ? FILL_WHITE : FILL_RED;
        String clickedTokenColor = ((Circle) element).getFill().toString();

        if (stageLabel.getText().equals(STAGE[1]) || stageLabel.getText().equals(STAGE[3])) {
            if (tokens[rowIndex][columnIndex] != null) {
                if (clickedTokenColor.equals(currentPlayerColor)) {
                    selectedRow = rowIndex;
                    selectedColumn = columnIndex;
                    tokens[rowIndex][columnIndex].getCircle().setStroke(Color.GREEN);
                    calculateAllowedPositions(tokens[rowIndex][columnIndex]);
                    for (int i = 0; i < 2; i++) {
                        if (allowedFields[i][0] != -1 && allowedFields[i][1] != -1) {
                            tokens[allowedFields[i][0]][allowedFields[i][1]].getCircle().setStroke(Color.GREEN);
                        }
                    }
                    String nextStage = stageLabel.getText().equals(STAGE[1]) ? STAGE[2] : STAGE[4];
                    stageLabel.setText(nextStage);
                }
            }
        } else if (stageLabel.getText().equals(STAGE[2]) || stageLabel.getText().equals(STAGE[4])) {
            if (allowedFields[0][0] != -1 && allowedFields[0][1] != -1) {
                tokens[allowedFields[0][0]][allowedFields[0][1]].getCircle().setStroke(Color.BLACK);
            }
            if (allowedFields[1][0] != -1 && allowedFields[1][1] != -1) {
                tokens[allowedFields[1][0]][allowedFields[1][1]].getCircle().setStroke(Color.BLACK);
            }
            if (rowIndex == selectedRow && columnIndex == selectedColumn) {
                tokens[rowIndex][columnIndex].getCircle().setStroke(Color.BLACK);
                String nextStage = stageLabel.getText().equals(STAGE[2]) ? STAGE[1] : STAGE[3];
                allowedFields[0][0] = -1;
                allowedFields[0][1] = -1;
                allowedFields[1][0] = -1;
                allowedFields[1][1] = -1;
                stageLabel.setText(nextStage);
            } else {
                if (isAllowedPosition(rowIndex, columnIndex)) {
                    
                    int oldRowSpike = board.getRowTokenToSpike(selectedRow, selectedColumn);
                    int oldColumnSpike = board.getColumnTokenToSpike(selectedRow, selectedColumn);

                    int newRowSpike = board.getRowTokenToSpike(rowIndex, columnIndex);
                    int newColumnSpike = board.getColumnTokenToSpike(rowIndex, columnIndex);
                    
                    if (rowIndex == allowedFields[0][0] && columnIndex == allowedFields[0][1]) {
                        if (currentPlayerColor.equals(FILL_RED)) {
                            playerRedPoints -= allowedFieldPoints[0];
                        } else {
                            playerWhitePoints -= allowedFieldPoints[0];
                        }
                        if(spikes[newRowSpike][newColumnSpike] != null) {
                            if (currentPlayerColor.equals(FILL_RED)) {
                                playerWhitePoints +=allowedFieldPoints[0];
                                whiteBarLabel.setText((Integer.parseInt(whiteBarLabel.getText())+1) + "");
                            } else {
                                playerRedPoints +=allowedFieldPoints[0];
                                redBarLabel.setText((Integer.parseInt(redBarLabel.getText())+1) + "");
                            }
                        }
                        dice.setDiceOneUsed(true);
                    } else {
                        if (currentPlayerColor.equals(FILL_RED)) {
                            playerRedPoints -= allowedFieldPoints[1];
                        } else {
                            playerWhitePoints -= allowedFieldPoints[1];
                        }
                        if(spikes[newRowSpike][newColumnSpike] != null) {
                            if (currentPlayerColor.equals(FILL_RED)) {
                                playerWhitePoints +=allowedFieldPoints[1];
                                whiteBarLabel.setText((Integer.parseInt(whiteBarLabel.getText())+1) + "");
                            } else {
                                playerRedPoints +=allowedFieldPoints[1];
                                redBarLabel.setText((Integer.parseInt(redBarLabel.getText())+1) + "");
                            }
                        }
                        dice.setDiceTwoUsed(true);
                    }
                    tokens[selectedRow][selectedColumn].getCircle().setStroke(Color.BLACK);
                    tokens[selectedRow][selectedColumn].getCircle().setFill(Color.web(FILL_BLANK));
                    tokens[rowIndex][columnIndex].getCircle().setFill(Color.web(currentPlayerColor));
                    
                    spikes[oldRowSpike][oldColumnSpike] = null;
                    spikes[newRowSpike][newColumnSpike] = tokens[rowIndex][columnIndex];
                    
                    String nextStage = stageLabel.getText().equals(STAGE[2]) ? STAGE[3] : STAGE[0];
                    if (nextStage.equals(STAGE[0])) {
                        nowPlayingLabel.setText(currentPlayerColor.equals(FILL_WHITE) ? "Red" : "White");
                    }
                    stageLabel.setText(nextStage);
                    
                    pointsRedLabel.setText(playerRedPoints + "");
                    pointsWhiteLabel.setText(playerWhitePoints + "");
                }
            }
        }
    }

    private void calculateAllowedPositions(Token token) {
        int tokenRow = GridPane.getRowIndex(token.getCircle());
        int tokenColumn = GridPane.getColumnIndex(token.getCircle());

        int spikeRow = board.getRowTokenToSpike(tokenRow, tokenColumn);
        int spikeColumn = board.getColumnTokenToSpike(tokenRow, tokenColumn);

        boolean isPlayerWhite = nowPlayingLabel.getText().equals("White");
        if (!dice.isDiceOneUsed()) {
            int diceOneValue = dice.getDiceOneValue();
            int spiker = isPlayerWhite ? spikeRow - diceOneValue : spikeRow + diceOneValue;
            if (spiker >= 0 && spiker < 24) {
                if (spikes[spiker][0] == null) { // If empty row
                    allowedFields[0][0] = board.getRowSpikeToToken(spiker, 0);
                    allowedFields[0][1] = board.getColumnSpikeToToken(spiker, 0);
                    allowedFieldPoints[0] = diceOneValue;
                } else if (spikes[spiker][0].getCircle().getFill().toString().equals(isPlayerWhite ? FILL_RED : FILL_WHITE)
                        && spikes[spiker][1] == null) { // If only one enemy token in a row
                    allowedFields[0][0] = board.getRowSpikeToToken(spiker, 0);
                    allowedFields[0][1] = board.getColumnSpikeToToken(spiker, 0);
                    allowedFieldPoints[0] = isPlayerWhite ? 24 - spiker : spiker;
                } else { // Check if all are friend tokens and if there is available spot
                    int firstEmptySpikeColumn = -1;
                    for (int i = 1; i < 5; i++) {
                        if (spikes[spiker][i - 1].getCircle().getFill().toString().equals(isPlayerWhite ? FILL_WHITE : FILL_RED) && spikes[spiker][i] == null) {
                            firstEmptySpikeColumn = i;
                            break;
                        }
                    }
                    if (firstEmptySpikeColumn != -1) {
                        allowedFields[0][0] = board.getRowSpikeToToken(spiker, firstEmptySpikeColumn);
                        allowedFields[0][1] = board.getColumnSpikeToToken(spiker, firstEmptySpikeColumn);
                        allowedFieldPoints[0] = diceOneValue;
                    }
                }
            } else {
                //TODO: Check if all player tokens are in his quarter and if they are he can push his token out of the board
            }
        }

        if (!dice.isDiceTwoUsed()) {
            int diceTwoValue = dice.getDiceTwoValue();
            int spiker = isPlayerWhite ? spikeRow - diceTwoValue : spikeRow + diceTwoValue;
            if (spiker >= 0 && spiker < 24) {
                if (spikes[spiker][0] == null) { // If empty row
                    allowedFields[1][0] = board.getRowSpikeToToken(spiker, 0);
                    allowedFields[1][1] = board.getColumnSpikeToToken(spiker, 0);
                    allowedFieldPoints[0] = diceTwoValue;
                } else if (spikes[spiker][0].getCircle().getFill().toString().equals(isPlayerWhite ? FILL_RED : FILL_WHITE)
                        && spikes[spiker][1] == null) { // If only one enemy token in a row
                    allowedFields[1][0] = board.getRowSpikeToToken(spiker, 0);
                    allowedFields[1][1] = board.getColumnSpikeToToken(spiker, 0);
                    allowedFieldPoints[0] = isPlayerWhite ? 24 - spiker : spiker;
                } else { // Check if all are friend tokens and if there is available spot
                    int firstEmptySpikeColumn = -1;
                    for (int i = 1; i < 5; i++) {
                        if (spikes[spiker][i - 1].getCircle().getFill().toString().equals(isPlayerWhite ? FILL_WHITE : FILL_RED) && spikes[spiker][i] == null) {
                            firstEmptySpikeColumn = i;
                            break;
                        }
                    }
                    if (firstEmptySpikeColumn != -1) {
                        allowedFields[1][0] = board.getRowSpikeToToken(spiker, firstEmptySpikeColumn);
                        allowedFields[1][1] = board.getColumnSpikeToToken(spiker, firstEmptySpikeColumn);
                        allowedFieldPoints[0] = diceTwoValue;
                    }
                }
            } else {
                //TODO: Check if all player tokens are in his quarter and if they are he can push his token out of the board
            }
        }
    }

    private boolean isAllowedPosition(int rowIndex, int columnIndex) {
        return (allowedFields[0][0] == rowIndex && allowedFields[0][1] == columnIndex) || (allowedFields[1][0] == rowIndex && allowedFields[1][1] == columnIndex);
    }

    private void calculateAllowedTokens() {

    }

    private boolean isAllowedToken() {
        return false;
    }

}
