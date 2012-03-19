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
        duration = 5;
    }
    public void usePower(int playerNumber) {
        switch(playerNumber){
            case 1: mygame.Main.player2.swapKeys();
                    System.out.println("Swapping p2 keys");
                    break;
            case 2: mygame.Main.player1.swapKeys();
                     System.out.println("Swapping p1 keys");
                    break;
            default:
                    break;
        }
    }
    public void cancelSuperPower(int playerNumber){
        switch(playerNumber){
            case 1: mygame.Main.player2.swapKeys();
                    System.out.println("Swapping p2 keys");
                    break;
            case 2: mygame.Main.player1.swapKeys();
                     System.out.println("Swapping p1 keys");
                    break;
            default:
                    break;
        }
    }
    
}
