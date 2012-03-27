/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.sfx;

import com.jme3.effect.ParticleEmitter;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import mygame.Main;
import com.jme3.effect.ParticleMesh.Type;

/**
 *
 * @author ZePedro
 */
public class ShockWave {
    
    private ParticleEmitter shockwave;
    
    public ShockWave(Material mat){
       
        shockwave = new ParticleEmitter("Shockwave", Type.Triangle, 1 * Main.COUNT_FACTOR);
//        shockwave.setRandomAngle(true);
        shockwave.setFaceNormal(Vector3f.UNIT_Y);
        shockwave.setStartColor(new ColorRGBA(.48f, 0.17f, 0.01f, (float) (.8f / Main.COUNT_FACTOR_F)));
        shockwave.setEndColor(new ColorRGBA(.48f, 0.17f, 0.01f, 0f));

        shockwave.setStartSize(0f);
        shockwave.setEndSize(7f);

        shockwave.setParticlesPerSec(0);
        shockwave.setGravity(0, 0, 0);
        shockwave.setLowLife(0.5f);
        shockwave.setHighLife(0.5f);
        shockwave.setInitialVelocity(new Vector3f(0, 0, 0));
        shockwave.setVelocityVariation(0f);
        shockwave.setImagesX(1);
        shockwave.setImagesY(1);
        shockwave.setMaterial(mat);
    
    }

    /**
     * @return the shockwave
     */
    public ParticleEmitter getShockwave() {
        return shockwave;
    }
}
