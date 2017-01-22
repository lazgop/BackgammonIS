/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etf.backgammon.gl130213d.models;

import javafx.scene.shape.Circle;

/**
 *
 * @author lazar
 */
public class Token {
    private Circle circle;

    public Token(Circle circle) {
        this.circle = circle;
    }

    public Circle getCircle() {
        return circle;
    }
    
    
}
