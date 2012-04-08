/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.superPower;

/**
 *
 * @author helder
 */
public class MechanicsSuperPower extends SuperPower {

     private float newVelocity = 0.10f;
     private float oldVelocity;
     public MechanicsSuperPower(){
        manaCost = 30;
        duration = 3;
       }
    
     public void usePower(int playerNumber) {
       
        oldVelocity = mygame.Main.players[1-playerNumber].getVelocity();
        mygame.Main.players[1-playerNumber].setVelocity(newVelocity);
        warnPlayers(playerNumber);
       
        
    }
    public void cancelSuperPower(int playerNumber){
       
        mygame.Main.players[1-playerNumber].setVelocity(oldVelocity);
       
        removeWarning( playerNumber);
    }
    
}
