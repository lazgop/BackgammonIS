/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etf.backgammon.gl130213d.wrappers;

import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 *
 * @author lazar
 */
public class SceneWrapper extends Scene {

    private final boolean isEnemyComputer;
    private final String matchPoints;
    private final boolean isColorRed;
    private final int treeDepth;

    public SceneWrapper(Parent root, boolean isEnemyComputer, String matchPoints, boolean isColorRed, int treeDepth) {
        super(root);
        this.isEnemyComputer = isEnemyComputer;
        this.matchPoints = matchPoints;
        this.isColorRed = isColorRed;
        this.treeDepth = treeDepth;
    }

    public boolean isEnemyComputer() {
        return isEnemyComputer;
    }

    public String getMatchPoints() {
        return matchPoints;
    }

    public boolean isColorRed() {
        return isColorRed;
    }

    public int getTreeDepth() {
        return treeDepth;
    }
    
    
}
