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
        superPowerImage = "star.png";
    }
    
    public void usePower(int playerNumber) {
        switch(playerNumber){
            case 1: if (this.manaCost <= mygame.Main.mana1.getCurrentMana()) { 
                        mygame.Main.player2.setSilenced(true);
                        mygame.Main.mana1.loseMana(this.manaCost);
                    
                    }
                    break;
            case 2: if (this.manaCost <= mygame.Main.mana1.getCurrentMana()) {
                        mygame.Main.player1.setSilenced(true);
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
            case 1: mygame.Main.player1.setSilenced(false);     
                    break;
            case 2: 
                    mygame.Main.player2.setSilenced(false);     
                    break;
            default:
                    break;
        }
         removeWarning( playerNumber);
    }
    
}
