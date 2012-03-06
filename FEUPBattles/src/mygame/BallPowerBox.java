/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author felipeschmitt
 */
public class BallPowerBox {
    
    private Quad greenPowerQuad;
    private Geometry greenPowerGeometry;
    private Node powerNode;
    private Material matGreen;
    private Material matYellow;
    private Material matRed;
    private float currentPower = 1.0f;
    private float barWidth = 10.0f;
    float increaseScale = 1.003f;
    
    
    public BallPowerBox(String name,  Vector3f position, Material green, Material yellow, Material red){
        powerNode = new Node("powerNode");
        matGreen = green;
        matYellow = yellow;
        matRed = red;
        greenPowerQuad = new Quad(barWidth, getCurrentPower());
        greenPowerGeometry = new Geometry("green"+name, getGreenPowerQuad());
        greenPowerGeometry.setLocalTranslation(position);
        greenPowerGeometry.setMaterial(green);
  
        powerNode.attachChild(greenPowerGeometry);
    }

    /**
     * @return the greenPowerQuad
     */
    public Quad getGreenPowerQuad() {
        return greenPowerQuad;
    }

    /**
     * @param greenPowerQuad the greenPowerQuad to set
     */
    public void setGreenPowerQuad(Quad greenPowerQuad) {
        this.greenPowerQuad = greenPowerQuad;
    }

    /**
     * @return the greenPowerGeometry
     */
    public Geometry getGreenPowerGeometry() {
        return greenPowerGeometry;
    }

    /**
     * @param greenPowerGeometry the greenPowerGeometry to set
     */
    public void setGreenPowerGeometry(Geometry greenPowerGeometry) {
        this.greenPowerGeometry = greenPowerGeometry;
    }

    /**
     * @return the powerNode
     */
    public Node getPowerNode() {
        return powerNode;
    }

    /**
     * @param powerNode the powerNode to set
     */
    public void setPowerNode(Node powerNode) {
        this.powerNode = powerNode;
    }

    /**
     * @return the currentPower
     */
    public float getCurrentPower() {
        return currentPower;
    }

    /**
     * @param currentPower the currentPower to set
     */
    public void setCurrentPower(float currentPower) {
        this.currentPower = currentPower;
    }
    
    public void resetPower(){
        greenPowerGeometry.scale(1.0f,1f/currentPower,1.0f);
        currentPower = 1;
        greenPowerGeometry.setMaterial(matGreen);
    }
    
    public void increasePower(float amount){
        
        if(currentPower*increaseScale < 100){
            greenPowerGeometry.scale(1.0f,increaseScale,1.0f);
            currentPower = currentPower*increaseScale;
        }
        
        if(currentPower > 50 && currentPower < 80)
            greenPowerGeometry.setMaterial(matYellow);
        
        if(currentPower >= 80)
            greenPowerGeometry.setMaterial(matRed);
    }
    
    
}
