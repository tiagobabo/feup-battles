/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.superPower;





/**
 *
 * @author ZePedro
 */
public class InformaticSuperPower extends SuperPower{

    public InformaticSuperPower(){
        manaCost = 30;
        duration = 3;
     
    }
    public void usePower(int playerNumber) {
       
          mygame.Main.players[1-playerNumber].swapKeys();
          warnPlayers(playerNumber);
        
        
    }
    public void cancelSuperPower(int playerNumber){
       
        mygame.Main.players[1-playerNumber].swapKeys();
        removeWarning( playerNumber);
    }

    
   
    
    
}
