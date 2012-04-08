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
       
    }
   
    public void usePower(int playerNumber) {
       
      
          mygame.Main.hps[playerNumber].loseLife(-1);
          mygame.Main.players[playerNumber].setNeedChange(true);
          warnPlayers(playerNumber);
       
        
    }
    public void cancelSuperPower(int playerNumber){
       
        mygame.Main.players[playerNumber].setNeedChange(false);
        removeWarning( playerNumber);
    }
    
}
