/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etf.backgammon.gl130213d.controllers;

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
    
    final private int STARTING_FIELDS[][][] = {{
        {10, 12},
        {9, 12},
        {10, 0},
        {9, 0},
        {8, 0},
        {7, 0},
        {6, 0},
        {0, 4},
        {1, 4},
        {2, 4},
        {0, 7},
        {1, 7},
        {2, 7},
        {3, 7},
        {4, 7}
    }, {
        {0, 12},
        {1, 12},
        {0, 0},
        {1, 0},
        {2, 0},
        {3, 0},
        {4, 0},
        {10, 4},
        {9, 4},
        {8, 4},
        {10, 7},
        {9, 7},
        {8, 7},
        {7, 7},
        {6, 7}
    }}; //Red and white

    int selectedRow = -1;
    int selectedColumn = -1;

    private Node spikes[][] = new Node[24][6];
    private Token tokens[][] = new Token[11][13];

    private int playerOnePoints = 167;
    private int playerTwoPoints = 167;

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
                int rowIndex = STARTING_FIELDS[color][i][0];
                int columnIndex = STARTING_FIELDS[color][i][1];

                tokens[rowIndex][columnIndex].getCircle().setFill(Color.web(color == 0 ? FILL_RED : FILL_WHITE));
            }
        }
        playerOnePoints = 167;
        playerTwoPoints = 167;

        dice = new Dice();

        nowPlayingLabel.setText(colorRed ? "Red" : "White");
        pointsRedLabel.setText((colorRed ? playerOnePoints : playerTwoPoints) + "");
        pointsWhiteLabel.setText((!colorRed ? playerOnePoints : playerTwoPoints) + "");
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
                    String nextStage = stageLabel.getText().equals(STAGE[1]) ? STAGE[2] : STAGE[4];
                    System.out.println(nextStage);
                    stageLabel.setText(nextStage);
                }
            }
        } else if (stageLabel.getText().equals(STAGE[2]) || stageLabel.getText().equals(STAGE[4])) {
            if (rowIndex == selectedRow && columnIndex == selectedColumn) {
                tokens[rowIndex][columnIndex].getCircle().setStroke(Color.BLACK);
                String nextStage = stageLabel.getText().equals(STAGE[2]) ? STAGE[1] : STAGE[3];
                System.out.println(nextStage);
                stageLabel.setText(nextStage);
            } else {
                if (clickedTokenColor.equals(FILL_BLANK)) {
                    tokens[selectedRow][selectedColumn].getCircle().setStroke(Color.BLACK);
                    tokens[selectedRow][selectedColumn].getCircle().setFill(Color.web(FILL_BLANK));
                    tokens[rowIndex][columnIndex].getCircle().setFill(Color.web(currentPlayerColor));
                    String nextStage = stageLabel.getText().equals(STAGE[2]) ? STAGE[3] : STAGE[0];
                    if (nextStage.equals(STAGE[0])) {
                        nowPlayingLabel.setText(currentPlayerColor.equals(FILL_WHITE) ? "Red" : "White");
                    }
                    System.out.println(nextStage);
                    stageLabel.setText(nextStage);
                }
            }
        }

    }

    private void calculateAllowedPositions() {
    }

}
