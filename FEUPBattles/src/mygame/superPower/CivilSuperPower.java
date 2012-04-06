/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.superPower;


import com.jme3.math.ColorRGBA;
import mygame.Main;

/**
 *
 * @author helder
 */
public class CivilSuperPower extends SuperPower{
    
     public CivilSuperPower(){
        manaCost = 50;
        duration = 1;
        superPowerImage = "star.png";
    }
    public void usePower(int playerNumber) {
        switch(playerNumber){
            case 1: if (this.manaCost <= mygame.Main.mana1.getCurrentMana()) { 
                        mygame.Main.player1.setImmune(true);
                        mygame.Main.mana1.loseMana(this.manaCost);
                    
                    }
                    break;
            case 2: if (this.manaCost <= mygame.Main.mana1.getCurrentMana()) {
                        mygame.Main.player2.setImmune(true);
                        mygame.Main.mana2.loseMana(this.manaCost);
                       
                    }
                    break;
            default:
                    break;
        }
        warnPlayers(playerNumber);
    }
    public void cancelSuperPower(int playerNumber){
        switch(playerNumber){
            case 1: mygame.Main.player1.setImmune(false);     
                    break;
            case 2: 
                    mygame.Main.player2.setImmune(false);     
                    break;
            default:
                    break;
        }
         removeWarning( playerNumber);
    }

   
   
}
