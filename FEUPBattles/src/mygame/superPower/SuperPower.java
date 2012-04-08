/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.superPower;

import java.util.concurrent.Callable;
import mygame.Main;

/**
 *
 * @author ZePedro
 */
public abstract class SuperPower {

    protected int manaCost;
    protected int duration;

    public abstract void usePower(int playerNumber);

    public abstract void cancelSuperPower(int playerNumber);
    private boolean inUse = false;
   
    private ESuperPower type;

    /**
     * @return the manaCost
     */
    public int getManaCost() {
        return manaCost;
    }

    /**
     * @param manaCost the manaCost to set
     */
    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @return the inUse
     */
    public boolean isInUse() {
        return inUse;
    }

    /**
     * @param inUse the inUse to set
     */
    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    

    public void warnPlayers(final int playerNumber) {
      
        
        Main.app.enqueue(new Callable<Void>() {

                    public Void call() throws Exception {
                        Main.spPics[playerNumber].setHeight(80);
                        Main.spPics[playerNumber].setWidth(80);
                        Main.pics[playerNumber].setHeight(0);
                        Main.pics[playerNumber].setWidth(0);
                        return null;
                    }
                });

    }

    public void removeWarning(final int playerNumber) {
        Main.app.enqueue(new Callable<Void>() {

                    public Void call() throws Exception {
                        Main.spPics[playerNumber].setHeight(0);
                        Main.spPics[playerNumber].setWidth(0);
                        Main.pics[playerNumber].setHeight(80);
                        Main.pics[playerNumber].setWidth(80);
                        return null;
                    }
                });
    }

    /**
     * @return the type
     */
    public ESuperPower getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(ESuperPower type) {
        this.type = type;
    }
}
