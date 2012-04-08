/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.superPower;


import com.jme3.math.ColorRGBA;

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
        
    }
    public void usePower(int playerNumber) {
      
        oldDamage = mygame.Main.players[playerNumber].getDamage();
        mygame.Main.players[playerNumber].setDamage(newDamage);
        mygame.Main.players[playerNumber].getBallMaterial().setColor("m_Ambient", ColorRGBA.Red);
        mygame.Main.players[playerNumber].getBallMaterial().setColor("m_Diffuse", ColorRGBA.Red);
        if (mygame.Main.players[playerNumber].getBall() != null) {
            mygame.Main.players[playerNumber].getBall().getMaterial().setColor("m_Ambient", ColorRGBA.Red);
            mygame.Main.players[playerNumber].getBall().getMaterial().setColor("m_Diffuse", ColorRGBA.Red);
        }
        warnPlayers(playerNumber);
       
    }
    public void cancelSuperPower(int playerNumber){
      
         
        mygame.Main.players[playerNumber].setDamage(oldDamage);
        mygame.Main.players[playerNumber].getBallMaterial().setColor("m_Ambient", ColorRGBA.Orange);
        mygame.Main.players[playerNumber].getBallMaterial().setColor("m_Diffuse", ColorRGBA.Orange);
        if (mygame.Main.players[playerNumber].getBall() != null) {
            mygame.Main.players[playerNumber].getBall().getMaterial().setColor("m_Ambient", ColorRGBA.Orange);
            mygame.Main.players[playerNumber].getBall().getMaterial().setColor("m_Diffuse", ColorRGBA.Orange);
        }
         removeWarning( playerNumber);
    }
}
