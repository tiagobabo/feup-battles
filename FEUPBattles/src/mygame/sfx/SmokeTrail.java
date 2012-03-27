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
public class SmokeTrail {
    private ParticleEmitter smoketrail;
    
    public SmokeTrail(Material mat){

        smoketrail = new ParticleEmitter("SmokeTrail", Type.Triangle, 22 * Main.COUNT_FACTOR);
        smoketrail.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, (float) (1.0f / Main.COUNT_FACTOR_F)));
        smoketrail.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
        smoketrail.setStartSize(.2f);
        smoketrail.setEndSize(1f);

//        smoketrail.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
        smoketrail.setFacingVelocity(true);
        smoketrail.setParticlesPerSec(0);
        smoketrail.setGravity(0, 1, 0);
        smoketrail.setLowLife(.4f);
        smoketrail.setHighLife(.5f);
        smoketrail.setInitialVelocity(new Vector3f(0, 12, 0));
        smoketrail.setVelocityVariation(1);
        smoketrail.setImagesX(1);
        smoketrail.setImagesY(3);
        smoketrail.setMaterial(mat);

    }

    /**
     * @return the smoketrail
     */
    public ParticleEmitter getSmoketrail() {
        return smoketrail;
    }
}
