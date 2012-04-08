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
       
    }
    public void usePower(int playerNumber) {
    
         mygame.Main.players[playerNumber].setImmune(true);
         warnPlayers(playerNumber);
        
    }
    public void cancelSuperPower(int playerNumber){
        mygame.Main.players[playerNumber].setImmune(false);
         removeWarning( playerNumber);
    }

   
   
}
