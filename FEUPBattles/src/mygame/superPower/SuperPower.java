/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.superPower;

import mygame.Main;



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
    protected String superPowerImage = null;

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

    /**
     * @return the superPowerImgaer
     */
    public String getSuperPowerImage() {
        return superPowerImage;
    }
    public void warnPlayers(int playerNumber) {
        switch(playerNumber){
            case 1: Main.p1Pic.setHeight(80);
                    Main.p1Pic.setWidth(80);
                    break;
            case 2: Main.p2Pic.setHeight(80);
                    Main.p2Pic.setWidth(80);
                    break;
            default:
                    break;
        }
      
    }
    public void removeWarning(int playerNumber) {
       switch(playerNumber){
            case 1: Main.p1Pic.setHeight(0);
                    Main.p1Pic.setWidth(0);
                    break;
            case 2: Main.p2Pic.setHeight(0);
                    Main.p2Pic.setWidth(0);
                    break;
            default:
                    break;
        }
    }
    
}
