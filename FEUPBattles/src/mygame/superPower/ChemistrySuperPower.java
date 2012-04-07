/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.superPower;

/**
 *
 * @author ZePedro
 */
public class ChemistrySuperPower extends SuperPower {

     private float newVelocity = 0.01f;
     private float oldVelocity;
     public ChemistrySuperPower(){
        manaCost = 30;
        duration = 3;
        superPowerImage = "keys.png";
    }
    
     public void usePower(int playerNumber) {
       
        switch(playerNumber){
            case 1: 
                if( mygame.Main.mana1.getCurrentMana() >= this.manaCost) {
                        oldVelocity = mygame.Main.player2.getVelocity();
                        mygame.Main.player2.setVelocity(newVelocity);
                        mygame.Main.mana1.loseMana(this.manaCost);
                        warnPlayers(playerNumber);
                }
                    break;
            case 2: 
                 if(mygame.Main.mana2.getCurrentMana() >= this.manaCost) {
                        oldVelocity = mygame.Main.player1.getVelocity();
                        mygame.Main.player1.setVelocity(newVelocity);
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
            case 1: mygame.Main.player2.setVelocity(oldVelocity);
                    break;
            case 2: mygame.Main.player1.setVelocity(oldVelocity);
                    
                    break;
            default:
                    break;
        }
        removeWarning( playerNumber);
    }
    
}
