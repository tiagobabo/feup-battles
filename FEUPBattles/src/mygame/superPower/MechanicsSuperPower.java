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
        superPowerImage = "inf.png";
    }
    
     public void usePower(int playerNumber) {
       
        switch(playerNumber){
            case 1: 
                if( mygame.Main.mana1.getCurrentMana() >= this.manaCost) {
                        oldVelocity = mygame.Main.player1.getVelocity();
                        mygame.Main.player1.setVelocity(newVelocity);
                        mygame.Main.mana1.loseMana(this.manaCost);
                       
                }
                    break;
            case 2: 
                 if(mygame.Main.mana2.getCurrentMana() >= this.manaCost) {
                        oldVelocity = mygame.Main.player2.getVelocity();
                        mygame.Main.player2.setVelocity(newVelocity);
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
            case 1: mygame.Main.player1.setVelocity(oldVelocity);
                    break;
            case 2: mygame.Main.player2.setVelocity(oldVelocity);
                    
                    break;
            default:
                    break;
        }
        removeWarning( playerNumber);
    }
    
}
