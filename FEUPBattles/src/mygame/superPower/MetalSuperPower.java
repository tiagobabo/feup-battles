/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.superPower;

import com.jme3.material.Material;
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
        superPowerImage = "star.png";
    }
    public void usePower(int playerNumber) {
        switch(playerNumber){
            case 1: if (this.manaCost <= mygame.Main.mana1.getCurrentMana()) { 
                        oldDamage = mygame.Main.player1.getDamage();
                      
                        mygame.Main.player1.setDamage(newDamage);
                        mygame.Main.player1.getBallMaterial().setColor("m_Ambient", ColorRGBA.Red);
                        mygame.Main.player1.getBallMaterial().setColor("m_Diffuse", ColorRGBA.Red);
                        if( mygame.Main.player1.getBall()!=null){
                            mygame.Main.player1.getBall().getMaterial().setColor("m_Ambient", ColorRGBA.Red);
                            mygame.Main.player1.getBall().getMaterial().setColor("m_Diffuse", ColorRGBA.Red);
                        }
                        mygame.Main.mana1.loseMana(this.manaCost);
                        warnPlayers(playerNumber);
                    }
                    break;
            case 2: if (this.manaCost <= mygame.Main.mana2.getCurrentMana()) {
                        oldDamage = mygame.Main.player2.getDamage();
                       
                        mygame.Main.player2.setDamage(newDamage);
                        mygame.Main.player2.getBallMaterial().setColor("m_Ambient", ColorRGBA.Red);
                        mygame.Main.player2.getBallMaterial().setColor("m_Diffuse", ColorRGBA.Red);
                        if( mygame.Main.player1.getBall()!=null){
                            mygame.Main.player2.getBall().getMaterial().setColor("m_Ambient", ColorRGBA.Red);
                            mygame.Main.player2.getBall().getMaterial().setColor("m_Diffuse", ColorRGBA.Red);
                        }
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
                    mygame.Main.player1.getBallMaterial().setColor("m_Ambient", ColorRGBA.Orange);
                    mygame.Main.player1.getBallMaterial().setColor("m_Diffuse", ColorRGBA.Orange); 
                    if( mygame.Main.player1.getBall()!=null){
                        mygame.Main.player1.getBall().getMaterial().setColor("m_Ambient", ColorRGBA.Orange);
                        mygame.Main.player1.getBall().getMaterial().setColor("m_Diffuse", ColorRGBA.Orange);
                    }
                    break;
            case 2: 
                    mygame.Main.player2.setDamage(oldDamage);
                    mygame.Main.player2.getBallMaterial().setColor("m_Ambient", ColorRGBA.Orange);
                    mygame.Main.player2.getBallMaterial().setColor("m_Diffuse", ColorRGBA.Orange);
                    if( mygame.Main.player2.getBall()!=null){
                        mygame.Main.player2.getBall().getMaterial().setColor("m_Ambient", ColorRGBA.Orange);
                        mygame.Main.player2.getBall().getMaterial().setColor("m_Diffuse", ColorRGBA.Orange);
                       }
                    break;
            default:
                    break;
        }
         removeWarning( playerNumber);
    }
}
