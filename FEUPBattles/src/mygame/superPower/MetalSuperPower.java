/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.superPower;

/**
 *
 * @author ZePedro
 */
public class MetalSuperPower  extends SuperPower{
    
    private int newDamage = 2;
    private int oldDamage;
    
    public MetalSuperPower(){
        manaCost = 30;
        duration = 3;
        superPowerImage = "star.png";
    }
    public void usePower(int playerNumber) {
        switch(playerNumber){
            case 1: if (this.manaCost <= mygame.Main.mana1.getCurrentMana()) { 
                        oldDamage = mygame.Main.player1.getDamage();
                        mygame.Main.player1.setDamage(newDamage);
                        mygame.Main.mana1.loseMana(this.manaCost);
                        warnPlayers(playerNumber);
                    }
                    break;
            case 2: if (this.manaCost <= mygame.Main.mana2.getCurrentMana()) {
                        oldDamage = mygame.Main.player2.getDamage();
                        mygame.Main.player2.setDamage(newDamage);
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
            case 1: mygame.Main.player1.setDamage(oldDamage);    
                    break;
            case 2: 
                    mygame.Main.player2.setDamage(oldDamage);     
                    break;
            default:
                    break;
        }
         removeWarning( playerNumber);
    }
}
