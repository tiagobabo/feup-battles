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
 * @author ZePedro
 */
public class HitPointsBox {

    private Quad greenHpQuad;
    private Geometry greenHpGeometry;
    private Quad blackHpQuad;
    private Geometry blackHpGeometry;
    private Node hpNode;
    private float currentLife = 200.0f;

    public HitPointsBox(String name, Vector3f position, Material green, Material black) {
        hpNode = new Node("hpbNode");

        greenHpQuad = new Quad(currentLife, 5f);
        greenHpGeometry = new Geometry("green" + name, greenHpQuad);
        greenHpGeometry.setLocalTranslation(position);
        greenHpGeometry.setMaterial(green);

        hpNode.attachChild(greenHpGeometry);

        blackHpQuad = new Quad(20.0f, 0.5f);
        blackHpGeometry = new Geometry("black" + name, blackHpQuad);
        Vector3f pos2 = new Vector3f(position);
        pos2.setZ(-1.0f);
        blackHpGeometry.setLocalTranslation(pos2);
        blackHpGeometry.setMaterial(black);

        hpNode.attachChild(blackHpGeometry);

    }

    /**
     * @return the greenHpQuad
     */
    public Quad getGreenHpQuad() {
        return greenHpQuad;
    }

    /**
     * @param greenHpQuad the greenHpQuad to set
     */
    public void setGreenHpQuad(Quad greenHpQuad) {
        this.greenHpQuad = greenHpQuad;
    }

    /**
     * @return the greenHpGeometry
     */
    public Geometry getGreenHpGeometry() {
        return greenHpGeometry;
    }

    /**
     * @param greenHpGeometry the greenHpGeometry to set
     */
    public void setGreenHpGeometry(Geometry greenHpGeometry) {
        this.greenHpGeometry = greenHpGeometry;
    }

    /**
     * @return the blackHpQuad
     */
    public Quad getBlackHpQuad() {
        return blackHpQuad;
    }

    /**
     * @param blackHpQuad the blackHpQuad to set
     */
    public void setBlackHpQuad(Quad blackHpQuad) {
        this.blackHpQuad = blackHpQuad;
    }

    /**
     * @return the blackHpGeometry
     */
    public Geometry getBlackHpGeometry() {
        return blackHpGeometry;
    }

    /**
     * @param blackHpGeometry the blackHpGeometry to set
     */
    public void setBlackHpGeometry(Geometry blackHpGeometry) {
        this.blackHpGeometry = blackHpGeometry;
    }

    /**
     * @return the hpNode
     */
    public Node getHpNode() {
        return hpNode;
    }

    /**
     * @param hpNode the hpNode to set
     */
    public void setHpNode(Node hpNode) {
        this.hpNode = hpNode;
    }

    public void loseLife(int lifeLost) {
        if (currentLife > 0) {
            float scale = 1 - 20 / currentLife;
            greenHpGeometry.scale(scale, 1.0f, 1.0f);
            currentLife = currentLife * scale;
            System.out.println(currentLife);
        }

    }
    
    public float getCurrentLife(){
        return currentLife;
    }
}
