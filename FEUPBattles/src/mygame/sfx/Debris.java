/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.sfx;

import com.jme3.effect.ParticleEmitter;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import mygame.Main;
import com.jme3.effect.ParticleMesh.Type;

/**
 *
 * @author ZePedro
 */
public class Debris {
    private ParticleEmitter debris;
    
    public Debris(Material mat){
        debris = new ParticleEmitter("Debris", Type.Triangle, 15 * Main.COUNT_FACTOR);
        debris.setSelectRandomImage(true);
        debris.setRandomAngle(true);
        debris.setRotateSpeed(FastMath.TWO_PI * 4);
        debris.setStartColor(new ColorRGBA(1f, 0.59f, 0.28f, (float) (1.0f / Main.COUNT_FACTOR_F)));
        debris.setEndColor(new ColorRGBA(.5f, 0.5f, 0.5f, 0f));
        debris.setStartSize(.2f);
        debris.setEndSize(.2f);

//        debris.setShape(new EmitterSphereShape(Vector3f.ZERO, .05f));
        debris.setParticlesPerSec(0);
        debris.setGravity(0, 12f, 0);
        debris.setLowLife(1.4f);
        debris.setHighLife(1.5f);
        debris.setInitialVelocity(new Vector3f(0, 15, 0));
        debris.setVelocityVariation(.60f);
        debris.setImagesX(3);
        debris.setImagesY(3);
  
        debris.setMaterial(mat);

    }

    /**
     * @return the debris
     */
    public ParticleEmitter getDebris() {
        return debris;
    }
    
}
