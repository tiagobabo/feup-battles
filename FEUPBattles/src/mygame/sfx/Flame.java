/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.sfx;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import mygame.Main;

/**
 *
 * @author ZePedro
 */
public class Flame {

    private ParticleEmitter flame;

    public Flame(Material mat) {
        flame = new ParticleEmitter("Flame", Main.EMITTER_TYPE, 32 * Main.COUNT_FACTOR);
        flame.setSelectRandomImage(true);
        flame.setStartColor(new ColorRGBA(1f, 0.4f, 0.05f, (float) (1f / Main.COUNT_FACTOR_F)));
        flame.setEndColor(new ColorRGBA(.4f, .22f, .12f, 0f));
        flame.setStartSize(1.3f);
        flame.setEndSize(2f);
        flame.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
        flame.setParticlesPerSec(0);
        flame.setGravity(0, -5, 0);
        flame.setLowLife(.4f);
        flame.setHighLife(.5f);
        flame.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 7, 0));
        flame.getParticleInfluencer().setVelocityVariation(1f);
        flame.setImagesX(2);
        flame.setImagesY(2);
        flame.setMaterial(mat);

    }

    /**
     * @return the flame
     */
    public ParticleEmitter getFlame() {
        return flame;
    }
}
