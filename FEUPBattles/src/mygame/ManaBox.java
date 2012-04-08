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


public class ManaBox {
    private Quad greenManaQuad;
    private Geometry greenManaGeometry;
    private Quad blackManaQuad;
    private Geometry blackManaGeometry;
    private Node manaNode;
    private float currentMana = 1f;
    private float maxMana = 200.0f;
    float increase = 0.1f;
    
    public ManaBox(String name, Vector3f position, Material green, Material black) {
        manaNode = new Node("hpbNode");

        greenManaQuad = new Quad(currentMana, 5f);
        greenManaGeometry = new Geometry("green" + name, greenManaQuad);
        greenManaGeometry.setLocalTranslation(position);
        greenManaGeometry.setMaterial(green);

        manaNode.attachChild(greenManaGeometry);

        blackManaQuad = new Quad(maxMana, 5f);
        blackManaGeometry = new Geometry("black" + name, blackManaQuad);
        Vector3f pos2 = new Vector3f(position);
        pos2.setZ(-1.0f);
        blackManaGeometry.setLocalTranslation(pos2);
        blackManaGeometry.setMaterial(black);

        manaNode.attachChild(blackManaGeometry);

    }

    /**
     * @return the greenManaQuad
     */
    public Quad getGreenManaQuad() {
        return greenManaQuad;
    }

    /**
     * @param greenManaQuad the greenManaQuad to set
     */
    public void setGreenManaQuad(Quad greenManaQuad) {
        this.greenManaQuad = greenManaQuad;
    }

    /**
     * @return the greenManaGeometry
     */
    public Geometry getGreenManaGeometry() {
        return greenManaGeometry;
    }

    /**
     * @param greenManaGeometry the greenManaGeometry to set
     */
    public void setGreenManaGeometry(Geometry greenManaGeometry) {
        this.greenManaGeometry = greenManaGeometry;
    }

    /**
     * @return the blackManaQuad
     */
    public Quad getBlackManaQuad() {
        return blackManaQuad;
    }

    /**
     * @param blackManaQuad the blackManaQuad to set
     */
    public void setBlackManaQuad(Quad blackManaQuad) {
        this.blackManaQuad = blackManaQuad;
    }

    /**
     * @return the blackManaGeometry
     */
    public Geometry getBlackManaGeometry() {
        return blackManaGeometry;
    }

    /**
     * @param blackManaGeometry the blackManaGeometry to set
     */
    public void setBlackManaGeometry(Geometry blackManaGeometry) {
        this.blackManaGeometry = blackManaGeometry;
    }

    /**
     * @return the manaNode
     */
    public Node getManaNode() {
        return manaNode;
    }

    /**
     * @param manaNode the manaNode to set
     */
    public void setManaNode(Node manaNode) {
        this.manaNode = manaNode;
    }

    /**
     * @return the currentMana
     */
    public float getCurrentMana() {
        return currentMana;
    }

    /**
     * @param currentMana the currentMana to set
     */
    public void setCurrentMana(float currentMana) {
        this.currentMana = currentMana;
    }
    
    public void regainMana(){
        if(currentMana+increase <= maxMana){
            float scale = (currentMana+increase)*(1/currentMana);
            greenManaGeometry.scale(scale,1.0f,1.0f);
            currentMana = currentMana+increase;
        }
        
        
    }
    
    public void loseMana(float q){
        float scale = (currentMana-q)*(1/currentMana);
        greenManaGeometry.scale(scale,1.0f,1.0f);
        currentMana = Math.max(currentMana-q,1);
    }

    /**
     * @return the maxMana
     */
    public float getMaxMana() {
        return maxMana;
    }

    /**
     * @param maxMana the maxMana to set
     */
    public void setMaxMana(float maxMana) {
        this.maxMana = maxMana;
    }
    
    
}
