/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.superPower;

/**
 *
 * @author ZePedro
 */
public class BioSuperPower extends SuperPower{
    
    public BioSuperPower(){
        manaCost = 50;
        duration = 0;
        superPowerImage = "keys.png";
    }
   
    public void usePower(int playerNumber) {
       
        switch(playerNumber){
            case 1: 
                if( mygame.Main.mana1.getCurrentMana() >= this.manaCost) {
                        mygame.Main.hp1.regainHP(10);
                        mygame.Main.mana1.loseMana(this.manaCost);
                       
                }
                    break;
            case 2: 
                 if(mygame.Main.mana2.getCurrentMana() >= this.manaCost) {
                        mygame.Main.hp2.regainHP(10);
                        mygame.Main.mana2.loseMana(this.manaCost);
                      
                 }
                    break;
            default:
                    break;
        }
         warnPlayers(playerNumber);
        
    }
    public void cancelSuperPower(int playerNumber){
       
        
        removeWarning( playerNumber);
    }
    
}
