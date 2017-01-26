/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etf.backgammon.gl130213d.controllers;

import etf.backgammon.gl130213d.models.Board;
import etf.backgammon.gl130213d.models.Dice;
import etf.backgammon.gl130213d.models.Field;
import etf.backgammon.gl130213d.models.Token;
import etf.backgammon.gl130213d.wrappers.SceneWrapper;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

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
        "Select the field where to move the second token",
        "Game Over",
        "Computer Playing"
    };

    public static String FILL_BLANK = "0x11111100";
    public static String FILL_WHITE = "0xffffffff";
    public static String FILL_RED = "0xff0000ff";

    public static double STROKE_NONE = 0;
    public static double STROKE_FULL = 1;

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

    private ArrayList<Token> allowedTokens = new ArrayList<>();

    private Board board = new Board();

    private boolean isCurrentPlayerWhite;

    @FXML
    private TreeView<String> treeView;
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
        if (setUpButton.getText().equals("Start Game")) {
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
        }
        
        setupBoard();
    }

    public void setupBoard() {
        board = new Board();
        for (Node element : boardGrid.getChildren()) {
            //Set initial colors of all tokens to
            if (element instanceof Circle) {
                Integer columnIndex = GridPane.getColumnIndex(element);
                Integer rowIndex = GridPane.getRowIndex(element);

                if (!((columnIndex == 6 && rowIndex == 4) || (columnIndex == 6 && rowIndex == 6))) { //Don't change color of bar tokens
                    ((Circle) element).setFill(Color.web(FILL_BLANK));
                    ((Circle) element).setStrokeWidth(STROKE_NONE);
                    tokens[rowIndex][columnIndex] = new Token((Circle) element);
                    //Set onClick listeners

                    if (setUpButton.getText().equals("Start Game")) {
                        element.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                handleTokenClick(event, element);
                            }
                        });
                    }
                }
            }
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

        nowPlayingLabel.setText((colorRed && !enemyComputer) ? "Red" : "White");
        isCurrentPlayerWhite = !(colorRed && !enemyComputer);
        pointsRedLabel.setText(playerRedPoints + "");
        pointsWhiteLabel.setText(playerWhitePoints + "");
        stageLabel.setText(STAGE[0]);
        diceOneLabel.setText(0 + "");
        diceTwoLabel.setText(0 + "");

        redBarLabel.setText("0");
        whiteBarLabel.setText("0");

        setUpButton.setText("Restart game");
    }

    @FXML
    private void handleDiceButtonAction(ActionEvent event) {
        if (stageLabel.getText().equals(STAGE[0])) {

            dice.roll();

            diceOneLabel.setText(dice.getDiceOneValue() + "");
            diceTwoLabel.setText(dice.getDiceTwoValue() + "");

            //Test if there are tokens in bar and goto other stage if there is
            if (areThereTokensInBar()) {
                calculateAllowedPositions(null, isCurrentPlayerWhite);
                for (int i = 0; i < 2; i++) {
                    if (allowedFields[i][0] != -1 && allowedFields[i][1] != -1) {
                        tokens[allowedFields[i][0]][allowedFields[i][1]].getCircle().setStroke(Color.GREEN);
                        tokens[allowedFields[i][0]][allowedFields[i][1]].getCircle().setStrokeWidth(STROKE_FULL);
                    }
                }
                stageLabel.setText(STAGE[2]);
            } else {
                allowedTokens = calculateAllowedTokens();
                stageLabel.setText(STAGE[1]);
            }
            selectedRow = -1;
            selectedColumn = -1;
        }
    }

    public boolean areThereTokensInBar() {
        return (!isCurrentPlayerWhite && Integer.parseInt(redBarLabel.getText()) > 0)
                || (isCurrentPlayerWhite && Integer.parseInt(whiteBarLabel.getText()) > 0);
    }

    private void handleTokenClick(MouseEvent event, Node element) {
        if (element instanceof Circle) {
            int rowIndex = GridPane.getRowIndex(element);
            int columnIndex = GridPane.getColumnIndex(element);

            String clickedTokenColor = ((Circle) element).getFill().toString();
            //Always if currentColor has tokens on the bar force them to use those tokens
            if (stageLabel.getText().equals(STAGE[1]) || stageLabel.getText().equals(STAGE[3])) {
                if (isAllowedToken(tokens[rowIndex][columnIndex])) {
                    selectedRow = rowIndex;
                    selectedColumn = columnIndex;
                    tokens[rowIndex][columnIndex].getCircle().setStroke(Color.GREEN);
                    tokens[rowIndex][columnIndex].getCircle().setStrokeWidth(STROKE_FULL);
                    calculateAllowedPositions(tokens[rowIndex][columnIndex], isCurrentPlayerWhite);
                    for (int i = 0; i < 2; i++) {
                        if (allowedFields[i][0] != -1 && allowedFields[i][1] != -1) {
                            tokens[allowedFields[i][0]][allowedFields[i][1]].getCircle().setStroke(Color.GREEN);
                            tokens[allowedFields[i][0]][allowedFields[i][1]].getCircle().setStrokeWidth(STROKE_FULL);
                        }
                    }
                    String nextStage = stageLabel.getText().equals(STAGE[1]) ? STAGE[2] : STAGE[4];
                    stageLabel.setText(nextStage);
                }
            } else if (stageLabel.getText().equals(STAGE[2]) || stageLabel.getText().equals(STAGE[4])) {

                if (rowIndex == selectedRow && columnIndex == selectedColumn) {
                    tokens[rowIndex][columnIndex].getCircle().setStroke(Color.BLACK);
                    tokens[rowIndex][columnIndex].getCircle().setStrokeWidth(STROKE_NONE);
                    String nextStage = stageLabel.getText().equals(STAGE[2]) ? STAGE[1] : STAGE[3];
                    stageLabel.setText(nextStage);

                    for (int i = 0; i < 2; i++) {
                        if (allowedFields[i][0] != -1 && allowedFields[i][1] != -1) {
                            tokens[allowedFields[i][0]][allowedFields[i][1]].getCircle().setStroke(Color.BLACK);
                            tokens[allowedFields[i][0]][allowedFields[i][1]].getCircle().setStrokeWidth(STROKE_NONE);
                        }
                    }
                    for (int i = 0; i < 2; i++) {
                        allowedFields[i][0] = -1;
                        allowedFields[i][1] = -1;
                    }
                    selectedRow = -1;
                    selectedColumn = -1;
                } else if (isAllowedPosition(rowIndex, columnIndex)) {
                    int oldRowSpike = -1;
                    int oldColumnSpike = -1;
                    if (selectedRow != -1 && selectedColumn != -1) {
                        oldRowSpike = board.getRowTokenToSpike(selectedRow, selectedColumn);
                        oldColumnSpike = board.getColumnTokenToSpike(selectedRow, selectedColumn);
                    }
                    int newRowSpike = board.getRowTokenToSpike(rowIndex, columnIndex);
                    int newColumnSpike = board.getColumnTokenToSpike(rowIndex, columnIndex);

                    if (rowIndex == allowedFields[0][0] && columnIndex == allowedFields[0][1]) {
                        if (!isCurrentPlayerWhite) {
                            playerRedPoints -= allowedFieldPoints[0];
                        } else {
                            playerWhitePoints -= allowedFieldPoints[0];
                        }
                        if (spikes[newRowSpike][newColumnSpike] != null) {
                            if (!isCurrentPlayerWhite) {
                                playerWhitePoints += allowedFieldPoints[0];
                                whiteBarLabel.setText((Integer.parseInt(whiteBarLabel.getText()) + 1) + "");
                            } else {
                                playerRedPoints += allowedFieldPoints[0];
                                redBarLabel.setText((Integer.parseInt(redBarLabel.getText()) + 1) + "");
                            }
                        }
                        dice.setDiceOneUsed(true);
                    } else if (rowIndex == allowedFields[1][0] && columnIndex == allowedFields[1][1]){
                        if (!isCurrentPlayerWhite) {
                            playerRedPoints -= allowedFieldPoints[1];
                        } else {
                            playerWhitePoints -= allowedFieldPoints[1];
                        }
                        if (spikes[newRowSpike][newColumnSpike] != null) {
                            if (!isCurrentPlayerWhite) {
                                playerWhitePoints += allowedFieldPoints[1];
                                whiteBarLabel.setText((Integer.parseInt(whiteBarLabel.getText()) + 1) + "");
                            } else {
                                playerRedPoints += allowedFieldPoints[1];
                                redBarLabel.setText((Integer.parseInt(redBarLabel.getText()) + 1) + "");
                            }
                        }
                        dice.setDiceTwoUsed(true);
                    }
                    if (selectedRow != -1 && selectedColumn != -1) {
                        tokens[selectedRow][selectedColumn].getCircle().setStroke(Color.BLACK);
                        tokens[selectedRow][selectedColumn].getCircle().setStrokeWidth(STROKE_NONE);
                        tokens[selectedRow][selectedColumn].getCircle().setFill(Color.web(FILL_BLANK));
                        spikes[oldRowSpike][oldColumnSpike] = null;
                    } else {
                        if (isCurrentPlayerWhite) {
                            whiteBarLabel.setText((Integer.parseInt(whiteBarLabel.getText()) - 1) + "");
                        } else {
                            redBarLabel.setText((Integer.parseInt(redBarLabel.getText()) - 1) + "");
                        }
                    }
                    tokens[rowIndex][columnIndex].getCircle().setFill(Color.web(isCurrentPlayerWhite ? FILL_WHITE : FILL_RED));
                    tokens[rowIndex][columnIndex].getCircle().setStrokeWidth(STROKE_NONE);
                    spikes[newRowSpike][newColumnSpike] = tokens[rowIndex][columnIndex];

                    String nextStage = stageLabel.getText().equals(STAGE[2]) ? STAGE[3] : STAGE[0];

                    allowedTokens = calculateAllowedTokens();

                    for (int i = 0; i < 2; i++) {
                        if (allowedFields[i][0] != -1 && allowedFields[i][1] != -1) {
                            tokens[allowedFields[i][0]][allowedFields[i][1]].getCircle().setStroke(Color.BLACK);
                            tokens[allowedFields[i][0]][allowedFields[i][1]].getCircle().setStrokeWidth(STROKE_NONE);
                        }
                    }

                    if (stageLabel.getText().equals(STAGE[2]) && areThereTokensInBar()) {
                        calculateAllowedPositions(null, isCurrentPlayerWhite);
                        for (int i = 0; i < 2; i++) {
                            if (allowedFields[i][0] != -1 && allowedFields[i][1] != -1) {
                                tokens[allowedFields[i][0]][allowedFields[i][1]].getCircle().setStroke(Color.GREEN);
                                tokens[allowedFields[i][0]][allowedFields[i][1]].getCircle().setStrokeWidth(STROKE_FULL);
                            }
                        }
                        nextStage = STAGE[4];
                    } else {
                        if (nextStage.equals(STAGE[0])) {
                            if (dice.getDiceOneValue() != dice.getDiceTwoValue()) {
                                nowPlayingLabel.setText(isCurrentPlayerWhite ? "Red" : "White");
                                isCurrentPlayerWhite = !isCurrentPlayerWhite;
                            }
                        }
                        for (int i = 0; i < 2; i++) {
                            allowedFields[i][0] = -1;
                            allowedFields[i][1] = -1;
                        }
                    }

                    stageLabel.setText(nextStage);

                    pointsRedLabel.setText(playerRedPoints + "");
                    pointsWhiteLabel.setText(playerWhitePoints + "");

                    selectedRow = -1;
                    selectedColumn = -1;

                    if (isCurrentPlayerWinner()) {
                        stageLabel.setText(STAGE[5] + (isCurrentPlayerWhite ? ": White Won" : ": Red Won") + " - HOMEBOARD");
                    }
                    if (nextStage.equals(STAGE[0]) && nowPlayingLabel.getText().equals("Red") && enemyComputer) {
                        computerPlay();
                    }
                }
            }
        }
    }

    public void computerPlay() {
        stageLabel.setText(STAGE[6]);

        dice.roll();

        diceOneLabel.setText(dice.getDiceOneValue() + "");
        diceTwoLabel.setText(dice.getDiceTwoValue() + "");


        
        for (int i = 0; i < 2; i++) {
            Field[] fields = expectiMinimax(treeDepth, dice);
            if (fields == null) {
                break;
            }
            
            String[][] currentState = tokenArrayToStringArray(spikes);
            if ("".equals(currentState[fields[1].spikeRowIndex][fields[1].spikeColumnIndex])) {
                if (fields[0] != null) {
                    playerRedPoints -= fields[1].spikeRowIndex - fields[0].spikeRowIndex;
                } else {
                    playerRedPoints -= fields[1].spikeRowIndex;   
                }
            } else {
                playerRedPoints -= fields[1].spikeRowIndex;
                playerWhitePoints += fields[1].spikeRowIndex;
                pointsWhiteLabel.setText(playerWhitePoints + "");
                whiteBarLabel.setText((Integer.parseInt(whiteBarLabel.getText()) + 1) + "");
            }

            pointsRedLabel.setText(playerRedPoints + "");
            if (fields[0] != null) {
                int rowIndexOld = board.getRowSpikeToToken(fields[0].spikeRowIndex, fields[0].spikeColumnIndex);
                int columnIndexOld = board.getColumnSpikeToToken(fields[0].spikeRowIndex, fields[0].spikeColumnIndex);

                tokens[rowIndexOld][columnIndexOld].getCircle().setFill(Color.web(FILL_BLANK));
                tokens[rowIndexOld][columnIndexOld].getCircle().setStrokeWidth(STROKE_NONE);
                
                spikes[fields[0].spikeRowIndex][fields[0].spikeColumnIndex] = null;
            } else {
                redBarLabel.setText((Integer.parseInt(redBarLabel.getText()) - 1) + "");
            }

            int rowIndexNew = board.getRowSpikeToToken(fields[1].spikeRowIndex, fields[1].spikeColumnIndex);
            int columnIndexNew = board.getColumnSpikeToToken(fields[1].spikeRowIndex, fields[1].spikeColumnIndex);

          
            tokens[rowIndexNew][columnIndexNew].getCircle().setFill(Color.web(FILL_RED));
            tokens[rowIndexNew][columnIndexNew].getCircle().setStrokeWidth(STROKE_NONE);

            spikes[fields[1].spikeRowIndex][fields[1].spikeColumnIndex] = tokens[rowIndexNew][columnIndexNew];
        }

        if (dice.getDiceOneValue() == dice.getDiceTwoValue()) {
            computerPlay();
        } else {
            nowPlayingLabel.setText("White");
            isCurrentPlayerWhite = true;
            stageLabel.setText(STAGE[0]);
        }
    }

    private void calculateAllowedPositions(Token token, boolean isCurrentPlayerWhite) {
        int tokenRow, tokenColumn;
        int spikeRow, spikeColumn;
        if (token != null) {
            tokenRow = GridPane.getRowIndex(token.getCircle());
            tokenColumn = GridPane.getColumnIndex(token.getCircle());

            spikeRow = board.getRowTokenToSpike(tokenRow, tokenColumn);
            spikeColumn = board.getColumnTokenToSpike(tokenRow, tokenColumn);
        } else {
            spikeRow = isCurrentPlayerWhite ? 24 : -1;
        }

        if (!dice.isDiceOneUsed()) {
            board.calculateAllowedPositionsForDice(true,
                    dice.getDiceOneValue(),
                    spikeRow,
                    isCurrentPlayerWhite,
                    allowedFields,
                    spikes,
                    allowedFieldPoints);
        }

        if (!dice.isDiceTwoUsed()) {
            board.calculateAllowedPositionsForDice(false,
                    dice.getDiceTwoValue(),
                    spikeRow,
                    isCurrentPlayerWhite,
                    allowedFields,
                    spikes,
                    allowedFieldPoints);
        }
    }

    private boolean isAllowedPosition(int rowIndex, int columnIndex) {
        return (allowedFields[0][0] == rowIndex && allowedFields[0][1] == columnIndex) || (allowedFields[1][0] == rowIndex && allowedFields[1][1] == columnIndex);
    }

    private ArrayList<Token> calculateAllowedTokens() {
        boolean currentPlayerRed = !isCurrentPlayerWhite;
        ArrayList<Token> allowedTokens = new ArrayList<>();
        allowedTokens.clear();
        if (currentPlayerRed && Integer.parseInt(redBarLabel.getText()) != 0) {
            return null;
        } else if (!currentPlayerRed && Integer.parseInt(whiteBarLabel.getText()) != 0) {
            return null;
        } else {
            for (int i = 0; i < 24; i++) {

                if (spikes[i][0] == null) {
                    continue;
                }

                if (spikes[i][4] != null) {
                    if (currentPlayerRed && spikes[i][4].getCircle().getFill().toString().equals(FILL_RED)
                            || !currentPlayerRed && spikes[i][4].getCircle().getFill().toString().equals(FILL_WHITE)) {
                        allowedTokens.add(spikes[i][4]);
                        continue;
                    }
                }

                for (int j = 0; j < 4; j++) {
                    if (spikes[i][j] != null && spikes[i][j + 1] == null) {
                        if (currentPlayerRed && spikes[i][j].getCircle().getFill().toString().equals(FILL_RED)
                                || !currentPlayerRed && spikes[i][j].getCircle().getFill().toString().equals(FILL_WHITE)) {
                            allowedTokens.add(spikes[i][j]);
                            break;
                        }
                    }
                }
            }
        }
        return allowedTokens;
    }

    private boolean isAllowedToken(Token token) {
        if (allowedTokens != null) {
            return allowedTokens.contains(token);
        }
        return false;
    }

    private void cleanUp() {

    }

    private boolean isCurrentPlayerWinner() {
        int start = isCurrentPlayerWhite ? 18 : 0;
        int end = isCurrentPlayerWhite ? 24 : 6;

        int counter = 0;
        for (int i = start; i < end; i++) {
            for (int j = 0; j < 5; j++) {
                if (spikes[i][j] != null && spikes[i][j].getCircle().getFill().toString().equals(isCurrentPlayerWhite ? FILL_WHITE : FILL_RED)) {
                    counter++;
                } else {
                    break;
                }
            }
        }
        return counter == 15;
    }

    public Field[] expectiMinimax(int depth, Dice dice) {

        Field[] returnFields = null; //Starting field, bestField

        String[][] currentState = tokenArrayToStringArray(spikes);

        ArrayList<Field> spikeCoordinates = new ArrayList<>();
        ArrayList<Token> currentAllowedTokens = calculateAllowedTokens();
        if (currentAllowedTokens == null) {
            //TODO: must use bar tokens
        } else {
            for (Token currentAllowedToken : currentAllowedTokens) {
                int tokenRowIndex = GridPane.getRowIndex(currentAllowedToken.getCircle());
                int tokenColumnIndex = GridPane.getColumnIndex(currentAllowedToken.getCircle());
                Field f = new Field();
                f.spikeRowIndex = board.getRowTokenToSpike(tokenRowIndex, tokenColumnIndex);
                f.spikeColumnIndex = board.getColumnTokenToSpike(tokenRowIndex, tokenColumnIndex);
                spikeCoordinates.add(f);
            }
        }

        int player2points = playerRedPoints;

        double min = Double.POSITIVE_INFINITY;
        int[] diceValues;
        if (!dice.isDiceOneUsed() && !dice.isDiceTwoUsed()) {
            diceValues = new int[2];
            diceValues[0] = dice.getDiceOneValue();
            diceValues[1] = dice.getDiceTwoValue();
        } else if (!dice.isDiceOneUsed()) {
            diceValues = new int[1];
            diceValues[0] = dice.getDiceOneValue();
        } else if (!dice.isDiceTwoUsed()) {
            diceValues = new int[1];
            diceValues[0] = dice.getDiceTwoValue();
        } else {
            diceValues = new int[1];
            diceValues[0] = 1000;
        }
        TreeItem<String> rootItem = new TreeItem<String> ("Root");
        treeView.setRoot(rootItem);
        rootItem.setExpanded(true);
        
        if (currentAllowedTokens == null) {
            ArrayList<Field> allowedFields = getAllowedFields(currentState, null, diceValues);
            for (Field allowedField : allowedFields) {
                String[][] newState = copyStrArr(currentState);
                newState[allowedField.spikeRowIndex][allowedField.spikeColumnIndex] = "r";
                TreeItem<String> treeItem = new TreeItem<>("smth");
                double val = 0;
                if (depth > 0) {
                    val = expectiMMforField(allowedField, 1, depth, newState, treeItem);
                }
                val -= allowedField.spikeRowIndex;
                treeItem.setValue("MIN: value = " + val + " row: " + allowedField.spikeRowIndex + " col: " + allowedField.spikeColumnIndex); 
                rootItem.getChildren().add(treeItem);
                if (val < min) {
                    min = val;
                    returnFields = new Field[2];
                    returnFields[0] = null;
                    returnFields[1] = allowedField;
                }
            }
        } else {
            for (Field spikeCoordinate : spikeCoordinates) {
                ArrayList<Field> allowedFields = getAllowedFields(currentState, spikeCoordinate, diceValues);
                for (Field allowedField : allowedFields) {
                    String[][] newState = copyStrArr(currentState);
                    newState[spikeCoordinate.spikeRowIndex][spikeCoordinate.spikeColumnIndex] = "";
                    newState[allowedField.spikeRowIndex][allowedField.spikeColumnIndex] = "r";
                    TreeItem<String> treeItem = new TreeItem<>("smth");
                    double val = 0;
                    if (depth > 0) {
                        val = expectiMMforField(allowedField, 1, depth, newState, treeItem);
                    }
                    if ("".equals(currentState[allowedField.spikeRowIndex][allowedField.spikeColumnIndex])) {
                        val -= allowedField.spikeRowIndex - spikeCoordinate.spikeRowIndex;
                    } else {
                        val -= allowedField.spikeRowIndex;
                    }
                    treeItem.setValue("MIN: value = " + val + " row: " + allowedField.spikeRowIndex + " col: " + allowedField.spikeColumnIndex);
                    rootItem.getChildren().add(treeItem);
                    if (val < min) {
                        min = val;
                        returnFields = new Field[2];
                        returnFields[0] = spikeCoordinate;
                        returnFields[1] = allowedField;
                    }
                }
            }
        }
        
        System.out.println(rootItem.getChildren().toString());
        
        return returnFields;
    }

    public double expectiMMforField(Field f, int curDepth, int maxDepth, String[][] currentState, TreeItem<String> rootItem) {
        double value = 0;
        if (curDepth == maxDepth) {
            if (maxDepth % 2 == 1) {
                rootItem.getChildren().add(new TreeItem<>("MAX: value = " + f.spikeRowIndex + " row: " + f.spikeRowIndex + " col: " + f.spikeColumnIndex));
                return f.spikeRowIndex;
            } else {
                rootItem.getChildren().add(new TreeItem<>("MIN: value = " + (24 - f.spikeRowIndex) + " row: " + f.spikeRowIndex + " col: " + f.spikeColumnIndex));
                return 24 - f.spikeRowIndex;
            }
        }

        int[] diceValues = {1, 2, 3, 4, 5, 6};

        if (curDepth % 2 == 1) {
            double max = Double.NEGATIVE_INFINITY;

            ArrayList<Field> allowedFields = getAllowedFields(currentState, f, diceValues);
            for (Field allowedField : allowedFields) {
                String[][] newState = copyStrArr(currentState);
                newState[f.spikeRowIndex][f.spikeColumnIndex] = "";
                newState[allowedField.spikeRowIndex][allowedField.spikeColumnIndex] = "w";
                TreeItem<String> treeItem = new TreeItem<>("smth1");
                double val = expectiMMforField(allowedField, curDepth + 1, maxDepth, newState, treeItem);
                if ("".equals(currentState[allowedField.spikeRowIndex][allowedField.spikeColumnIndex])) {
                    val += f.spikeRowIndex - allowedField.spikeRowIndex;
                } else {
                    val += allowedField.spikeRowIndex;
                }
                treeItem.setValue("MAX: value = " + val + " row: " + allowedField.spikeRowIndex + " col: " + allowedField.spikeColumnIndex); 
                rootItem.getChildren().add(treeItem);
                
                if (val > max) {
                    max = val;
                }
            }

            return max;
            //return highest expectiMMforField
        }

        if (curDepth % 2 == 0) {
            double min = Double.POSITIVE_INFINITY;

            ArrayList<Field> allowedFields = getAllowedFields(currentState, f, diceValues);
            for (Field allowedField : allowedFields) {
                String[][] newState = copyStrArr(currentState);
                newState[f.spikeRowIndex][f.spikeColumnIndex] = "";
                newState[allowedField.spikeRowIndex][allowedField.spikeColumnIndex] = "r";
                TreeItem<String> treeItem = new TreeItem<>("smth");
                double val = expectiMMforField(allowedField, curDepth + 1, maxDepth, newState, treeItem);
                if ("".equals(currentState[allowedField.spikeRowIndex][allowedField.spikeColumnIndex])) {
                    val -= allowedField.spikeRowIndex - f.spikeRowIndex;
                } else {
                    val -= allowedField.spikeRowIndex;
                }
                treeItem.setValue("MIN: value = " + val + " row: " + allowedField.spikeRowIndex + " col: " + allowedField.spikeColumnIndex); 
                rootItem.getChildren().add(treeItem);
                if (val < min) {
                    min = val;
                }
            }

            return min;
            //return lowest expectiMMforField
        }

        return value;
    }

    public String[][] tokenArrayToStringArray(Token[][] tokens) {
        String[][] returnArray = new String[tokens.length][];
        for (int i = 0; i < tokens.length; i++) {
            returnArray[i] = new String[tokens[i].length];
            for (int j = 0; j < tokens[i].length; j++) {
                String value;
                if (tokens[i][j] == null || tokens[i][j].getCircle().getFill().toString().equals(FILL_BLANK)) {
                    value = "";
                } else if (tokens[i][j].getCircle().getFill().toString().equals(FILL_RED)) {
                    value = "r";
                } else {
                    value = "w";
                }
                returnArray[i][j] = value;
            }
        }

        return returnArray;
    }

    public String[][] copyStrArr(String[][] strArr) {
        String[][] retStrArr = new String[strArr.length][];
        for (int i = 0; i < strArr.length; i++) {
            retStrArr[i] = new String[strArr[i].length];
            for (int j = 0; j < strArr[i].length; j++) {
                retStrArr[i][j] = strArr[i][j];
            }
        }
        return retStrArr;
    }

    private ArrayList<Field> getAllowedFields(String[][] currentState, Field spikeCoordinate, int[] diceValues) {
        ArrayList<Field> allowedFields = new ArrayList<>();
        boolean isCurrentRed;
        if (spikeCoordinate == null) {
            isCurrentRed = true;
        } else {
            isCurrentRed = "r".equals(currentState[spikeCoordinate.spikeRowIndex][spikeCoordinate.spikeColumnIndex]);
        }

        for (int i = 0; i < diceValues.length; i++) {
            int spikeRow;
            if (spikeCoordinate == null) {
                spikeRow = isCurrentRed ? -1 + diceValues[i] : 24 - diceValues[i];
                System.out.println("NULL " + spikeRow);
            } else {
                spikeRow = isCurrentRed ? spikeCoordinate.spikeRowIndex + diceValues[i] : spikeCoordinate.spikeRowIndex - diceValues[i];
            }

            if (spikeRow >= 0 && spikeRow < 24) {
                if ("".equals(currentState[spikeRow][0])) {
                    allowedFields.add(new Field(spikeRow, 0));
                } else if ("".equals(currentState[spikeRow][1]) && (isCurrentRed ? "w".equals(currentState[spikeRow][0]) : "r".equals(currentState[spikeRow][0]))) {
                    allowedFields.add(new Field(spikeRow, 0));
                } else {
                    int firstEmptySpikeColumn = 0;
                    while (currentState[spikeRow][firstEmptySpikeColumn].equals(isCurrentRed ? "r" : "w")
                            && firstEmptySpikeColumn != 5) {
                        firstEmptySpikeColumn++;
                    }

                    if (firstEmptySpikeColumn != 0) {
                        allowedFields.add(new Field(spikeRow, firstEmptySpikeColumn));
                    }
                }
            }
        }
        return allowedFields;
    }

}
