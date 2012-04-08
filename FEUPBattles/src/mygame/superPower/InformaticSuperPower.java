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
        superPowerImage = "keys.png";
    }
    public void usePower(int playerNumber) {
       
        switch(playerNumber){
            case 1: 
                if( mygame.Main.mana1.getCurrentMana() >= this.manaCost) {
                        mygame.Main.player2.swapKeys();
                        mygame.Main.mana1.loseMana(this.manaCost);
                        warnPlayers(playerNumber);
                }
                    break;
            case 2: 
                 if(mygame.Main.mana2.getCurrentMana() >= this.manaCost) {
                        mygame.Main.player1.swapKeys();
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
            case 1: if(mygame.Main.player2.getSwapped()==-1) 
                        mygame.Main.player2.swapKeys();
                   
                    break;
            case 2:if(mygame.Main.player1.getSwapped()==-1) 
                        mygame.Main.player1.swapKeys();
                     
                    break;
            default:
                    break;
        }
        removeWarning( playerNumber);
    }

    
   
    
    
}
