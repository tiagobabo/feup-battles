/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.superPower;

import com.jme3.material.Material;

/**
 *
 * @author helder
 */
public class CivilSuperPower extends SuperPower{
    
     public CivilSuperPower(){
        manaCost = 5;
        duration = 1;
    }
    public void usePower(int playerNumber) {
        switch(playerNumber){
            case 1: if (!mygame.Main.player1.immune && this.manaCost <= mygame.Main.player1.getManaPoints()) { 
                        mygame.Main.player1.setImmune(1);
                        mygame.Main.player1.setManaPoints(mygame.Main.player1.getManaPoints() - this.manaCost);
                        mygame.Main.mana1.loseLife(this.manaCost);
                        System.out.println("p1 immune");
                    }
                    break;
            case 2: if (!mygame.Main.player2.immune && this.manaCost <= mygame.Main.player1.getManaPoints()) {
                        mygame.Main.player2.setImmune(1);            
                        mygame.Main.player1.setManaPoints(mygame.Main.player1.getManaPoints() - this.manaCost);
                        mygame.Main.mana2.loseLife(this.manaCost);
                        System.out.println("p2 immune");
                    }
                    break;
            default:
                    break;
        }
    }
    public void cancelSuperPower(int playerNumber){
        switch(playerNumber){
            case 1: if (mygame.Main.player1.immune) {
                        mygame.Main.player1.setImmune(0);     
                        System.out.println("p1 not immune");
                    }
                    break;
            case 2: if (mygame.Main.player2.immune) {
                        mygame.Main.player2.setImmune(0);     
                        System.out.println("p2 not immune");
                    }
                    break;
            default:
                    break;
        }
    }
}
