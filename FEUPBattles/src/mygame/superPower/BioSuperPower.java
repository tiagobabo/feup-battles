/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.superPower;

import mygame.Main;

/**
 *
 * @author ZePedro
 */
public class BioSuperPower extends SuperPower{
    
   
    public BioSuperPower(){
        
        manaCost = 50;
        duration = 1;
        superPowerImage = "keys.png";
    }
   
    public void usePower(int playerNumber) {
       
        switch(playerNumber){
            case 1: 
                if( mygame.Main.mana1.getCurrentMana() >= this.manaCost) {
                        mygame.Main.hp1.loseLife(-1);
                        mygame.Main.mana1.loseMana(this.manaCost);
                        mygame.Main.player1.setNeedChange(true);
                         warnPlayers(playerNumber);
                }
                    break;
            case 2: 
                 if(mygame.Main.mana2.getCurrentMana() >= this.manaCost) {
                        mygame.Main.hp1.loseLife(-1);
                        mygame.Main.player2.setNeedChange(true);
                        mygame.Main.mana2.loseMana(this.manaCost);
                        warnPlayers(playerNumber);
                 }
                    break;
            default:
                    break;
        }
       
        
    }
    public void cancelSuperPower(int playerNumber){
       
         switch(playerNumber){
            case 1:mygame.Main.player1.setNeedChange(false);
                    break;
            case 2:mygame.Main.player2.setNeedChange(false);
                    
                    break;
            default:
                    break;
        }
        removeWarning( playerNumber);
    }
    
}
