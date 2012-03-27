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
public class Spark {
    private ParticleEmitter spark;
    
    public Spark(Material mat){
        spark = new ParticleEmitter("Spark", Type.Triangle, 30 * Main.COUNT_FACTOR);
        spark.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, (float) (1.0f / Main.COUNT_FACTOR_F)));
        spark.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
        spark.setStartSize(.5f);
        spark.setEndSize(.5f);
        spark.setFacingVelocity(true);
        spark.setParticlesPerSec(0);
        spark.setGravity(0, 5, 0);
        spark.setLowLife(1.1f);
        spark.setHighLife(1.5f);
        spark.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 20, 0));
        spark.getParticleInfluencer().setVelocityVariation(1);
        spark.setImagesX(1);
        spark.setImagesY(1);
        spark.setMaterial(mat);
        
        
    }

    /**
     * @return the spark
     */
    public ParticleEmitter getSpark() {
        return spark;
    }
}
