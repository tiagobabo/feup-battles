/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;


import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 *
 * @author ZePedro
 */
public class Player {

    private Geometry playerGeo;
    private int hitPoints = 10;
    private RigidBodyControl playerControl;
    private float mass = 2.0f;
    private Box playerBox;
    private String playerName;
  
   

    public Player(String name, Material playerMaterial, Vector3f initialPosition) {
        playerBox = new Box(Vector3f.ZERO, 1, 1, 1);
        playerGeo = new Geometry(name, playerBox);
        playerGeo.setLocalTranslation(initialPosition);
        playerGeo.setMaterial(playerMaterial);
        playerControl = new RigidBodyControl(mass);
        playerGeo.addControl(playerControl);
        playerControl.setKinematic(true);
        playerName = name;
        
     


    }

    /**
     * @return the hitPoints
     */
    public int getHitPoints() {
        return hitPoints;
    }

    /**
     * @param hitPoints the hitPoints to set
     */
    public void setHitPoints(int hitPoints) {
        if(hitPoints >= 0)
            this.hitPoints = hitPoints;
    }

    /**
     * @return the playerGeo
     */
    public Geometry getPlayerGeo() {
        return playerGeo;
    }

    /**
     * @param playerGeo the playerGeo to set
     */
    public void setPlayerGeo(Geometry playerGeo) {
        this.playerGeo = playerGeo;
    }

    /**
     * @return the playerControl
     */
    public RigidBodyControl getPlayerControl() {
        return playerControl;
    }

    /**
     * @param playerControl the playerControl to set
     */
    public void setPlayerControl(RigidBodyControl playerControl) {
        this.playerControl = playerControl;
    }

    /**
     * @return the mass
     */
    public float getMass() {
        return mass;
    }

    /**
     * @param mass the mass to set
     */
    public void setMass(float mass) {
        this.mass = mass;
    }

    /**
     * @return the playerBox
     */
    public Box getPlayerBox() {
        return playerBox;
    }

    /**
     * @param playerBox the playerBox to set
     */
    public void setPlayerBox(Box playerBox) {
        this.playerBox = playerBox;
    }

    public Vector3f getLocalTranslation() {
        return playerGeo.getLocalTranslation();
    }

    public void setLocalTranslation(Vector3f translation) {
        playerGeo.setLocalTranslation(translation);
    }

    /**
     * @return the playerName
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * @param playerName the playerName to set
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

   
}
