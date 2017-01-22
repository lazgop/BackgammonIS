/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etf.backgammon.gl130213d.controllers;

import etf.backgammon.gl130213d.wrappers.SceneWrapper;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author lazar
 */
public class GameController implements Initializable {
    
    @FXML
    private TreeView treeView;
    @FXML
    private Button diceButton;
    @FXML
    private Button setUpButton;
    
    private boolean colorRed;
    private boolean enemyComputer;
    private int matchPoints;
    private int treeDepth;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) { 
    }
    
    @FXML
    private void handleDiceButtonAction(ActionEvent event) {
        System.out.println("klik na dice");
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
    }
}
