/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;


import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import mygame.superPower.CancelSuperPower;
import mygame.superPower.SuperPower;

/**
 *
 * @author ZePedro
 */
public class Player {

    private Geometry playerGeo;
    private RigidBodyControl playerControl;
    private float mass = 2.0f;
    private Box playerBox;
    private String playerName;
    private Node playerNode;
    private boolean alive = true;
    private RigidBodyControl ball;
    private Keys keys;
    private SuperPower sp;
    private int swapped = 1;
    public boolean immune = false;
    
   

    public Player(String name, Material playerMaterial, Vector3f initialPosition, SuperPower sp) {
        playerBox = new Box(Vector3f.ZERO, 1, 1, 1);
        playerGeo = new Geometry(name, playerBox);
        playerGeo.setLocalTranslation(initialPosition);
        playerGeo.setMaterial(playerMaterial);
        playerControl = new RigidBodyControl(mass);
        playerGeo.addControl(playerControl);
        playerControl.setKinematic(true);
        playerName = name;
        playerNode = new Node(name);
        this.sp = sp;
        playerNode.attachChild(playerGeo);
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

    /**
     * @return the playerNode
     */
    public Node getPlayerNode() {
        return playerNode;
    }

    /**
     * @param playerNode the playerNode to set
     */
    public void setPlayerNode(Node playerNode) {
        this.playerNode = playerNode;
    }

    /**
     * @return the alive
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * @param alive the alive to set
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * @return the ball
     */
    public RigidBodyControl getBall() {
        return ball;
    }

    /**
     * @param ball the ball to set
     */
    public void setBall(RigidBodyControl ball) {
        this.ball = ball;
    }
    
    public int getLeftKey(){
        return keys.getLeftKey();
    }
    public int getRightKey(){
        return keys.getRightKey();
    }
    public int getFireKey(){
        return keys.getFireKey();
    }
    public int getSuperPowerKey(){
        return keys.getSuperPowerKey();
    }
    public void useSuperPower(int pnum){
        this.sp.usePower(pnum);
        new CancelSuperPower(sp,sp.getDuration(),pnum).start();
        
    }

    /**
     * @param keys the keys to set
     */
    public void setKeys(Keys keys) {
        this.keys = keys;
    }

    public void swapKeys() {
        setSwapped(getSwapped() * -1);
        
    }

    /**
     * @return the swapped
     */
    public int getSwapped() {
        return swapped;
    }

    /**
     * @param swapped the swapped to set
     */
    public void setSwapped(int swapped) {
        this.swapped = swapped;
    }

    public void setImmune(int immune) {
        switch(immune) {
            case 1:
                this.immune = true;
                this.playerGeo.getMaterial().setColor("Color", ColorRGBA.Red);
                break;
            case 0:
                this.immune = false;
                this.playerGeo.getMaterial().setColor("Color", ColorRGBA.Blue);
                break;
            default:
                break;
        }
    }
    
    
}
