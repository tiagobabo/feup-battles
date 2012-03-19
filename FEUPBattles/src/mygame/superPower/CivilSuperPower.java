/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.superPower;

/**
 *
 * @author helder
 */
public class CivilSuperPower extends SuperPower{
    
     public CivilSuperPower(){
        manaCost = 5;
        duration = 1;
    }
    public void usePower(int playerNumber) {
        switch(playerNumber){
            case 1: if (!mygame.Main.player1.immune) mygame.Main.player1.immune = true;
                    System.out.println("p1 immune");
                    break;
            case 2: if (!mygame.Main.player2.immune) mygame.Main.player2.immune = true;
                    System.out.println("p2 immune");
                    break;
            default:
                    break;
        }
    }
    public void cancelSuperPower(int playerNumber){
        switch(playerNumber){
            case 1: if (mygame.Main.player1.immune) mygame.Main.player1.immune = false;
                    System.out.println("p1 not immune");
                    break;
            case 2: if (mygame.Main.player2.immune) mygame.Main.player2.immune = false;
                    System.out.println("p2 not immune");
                    break;
            default:
                    break;
        }
    }
}
