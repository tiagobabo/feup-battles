/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.superPower;

/**
 *
 * @author helder
 */
public class EletroSuperPower extends SuperPower {

     public EletroSuperPower(){
        manaCost = 50;
        duration = 10;
       
    }
    
    public void usePower(int playerNumber) {
      
          mygame.Main.players[1-playerNumber].setSilenced(true);
          warnPlayers(playerNumber);
       
    }
    public void cancelSuperPower(int playerNumber){
        mygame.Main.players[1-playerNumber].setSilenced(false);
         removeWarning( playerNumber);
    }
    
}
