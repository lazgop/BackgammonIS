/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etf.backgammon.gl130213d.models;

/**
 *
 * @author lazar
 */
public class Field {

    public int spikeRowIndex;
    public int spikeColumnIndex;

    public Field() {
    }
    
    public Field(int spikeRowIndex, int spikeColumnIndex) {
        this.spikeRowIndex = spikeRowIndex;
        this.spikeColumnIndex = spikeColumnIndex;
    }

    
}
