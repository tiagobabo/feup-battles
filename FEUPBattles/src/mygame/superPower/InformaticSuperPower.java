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
        manaCost = 1;
        duration = 3;
    }
    public void usePower(int playerNumber) {
        switch(playerNumber){
            case 1: 
                if(mygame.Main.player2.getSwapped() == 1 && mygame.Main.player1.getManaPoints() >= this.manaCost) {
                        mygame.Main.player2.swapKeys();
                        mygame.Main.player1.setManaPoints(mygame.Main.player1.getManaPoints() - this.manaCost);
                        mygame.Main.mana1.loseLife(this.manaCost);
                        System.out.println("Swapping p2 keys");
                }
                    break;
            case 2: 
                 if(mygame.Main.player1.getSwapped() == 1 && mygame.Main.player2.getManaPoints() >= this.manaCost) {
                        mygame.Main.player1.swapKeys();
                        mygame.Main.player2.setManaPoints(mygame.Main.player2.getManaPoints() - this.manaCost);
                        mygame.Main.mana2.loseLife(this.manaCost);
                        System.out.println("Swapping p1 keys");
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
    }
    
}
