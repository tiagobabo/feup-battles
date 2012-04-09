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

    
     private float oldVelocity;
     public ChemistrySuperPower(){
        manaCost = 30;
        duration = 3;
       
    }
    
     public void usePower(int playerNumber) {
       
        oldVelocity = mygame.Main.players[1-playerNumber].getVelocity();
        mygame.Main.players[1-playerNumber].setVelocity((float)(oldVelocity/5.0));
        warnPlayers(playerNumber);
        
        
    }
    public void cancelSuperPower(int playerNumber){
       
         mygame.Main.players[1-playerNumber].setVelocity(oldVelocity);
        removeWarning( playerNumber);
    }
    
}
