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
            case 1: mygame.Main.player2.swapKeys();
                    System.out.println("Swapping p2 keys back to normal");
                    break;
            case 2: mygame.Main.player1.swapKeys();
                     System.out.println("Swapping p1 keys back to normal");
                    break;
            default:
                    break;
        }
        removeWarning( playerNumber);
    }

    
   
    
    
}
