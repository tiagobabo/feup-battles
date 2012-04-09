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


     private float oldVelocity;
     public MechanicsSuperPower(){
        manaCost = 30;
        duration = 3;
       }
    
     public void usePower(int playerNumber) {
       
        oldVelocity = mygame.Main.players[playerNumber].getVelocity();
        mygame.Main.players[playerNumber].setVelocity(oldVelocity*3);
        warnPlayers(playerNumber);
       
        
    }
    public void cancelSuperPower(int playerNumber){
       
        mygame.Main.players[playerNumber].setVelocity(oldVelocity);
       
        removeWarning( playerNumber);
    }
    
}
