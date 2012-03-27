/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.superPower;



/**
 *
 * @author ZePedro
 */
public abstract class SuperPower {
    
    protected int manaCost;
    protected int duration;
    public abstract void usePower( int playerNumber);
    public abstract void cancelSuperPower(int playerNumber);
    private boolean inUse = false;

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
    
}
