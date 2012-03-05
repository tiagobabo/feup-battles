/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.scene.Node;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author helder
 */
class ExplosionCleaner
    implements Callable<Node>
{
    Node clean;
    public ExplosionCleaner(Node clean) {
        this.clean = clean;
    }
    public Node call()
    {
        try {
            // sleep for 2 seconds
            Thread.sleep(2 * 1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ExplosionCleaner.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clean;
    }
}