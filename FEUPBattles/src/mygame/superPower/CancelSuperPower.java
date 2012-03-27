/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.superPower;

import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author ZePedro
 */
public class CancelSuperPower extends Thread {

    public SuperPower power;
    public int waitTime;
    public int pNum;
    public CancelSuperPower(SuperPower power, int waitTime,int playerNum){
     this.power = power;
     this.waitTime = waitTime;
     pNum = playerNum;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(waitTime*1000);
            power.cancelSuperPower(pNum);
            power.setInUse(false);
            System.out.println("POWER CANCELED");
        } catch (InterruptedException ex) {
            Logger.getLogger(CancelSuperPower.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    
    
}
