/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;

/**
 *
 * @author ZePedro
 */
public class MyControl extends RigidBodyControl implements PhysicsCollisionListener {

    MyControl(float mass) {
        super(mass);
    }

    public void collision(PhysicsCollisionEvent pce) {
        
    }
    
}
