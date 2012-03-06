package mygame;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends SimpleApplication implements PhysicsCollisionListener {

    static final float RELOAD_TIME = 2.0f;
    private BulletAppState bulletAppState;
    float velocity = 0.01f;
    Player player1;
    Player player2;
    private static final int COUNT_FACTOR = 1;
    private static final float COUNT_FACTOR_F = 1f;
    private static final boolean POINT_SPRITE = true;
    private static final Type EMITTER_TYPE = POINT_SPRITE ? Type.Point : Type.Triangle;
    HitPointsBox hp1;
    HitPointsBox hp2;
    BitmapText reloadP1;
    BitmapText reloadP2;
    long player1_power = 0;
    long player2_power = 0;
    public ArrayList< Future<Node> > tasks = new ArrayList< Future<Node> >();
    boolean player1_reload = false, player2_reload = false;
    float p1_reloadTime = RELOAD_TIME, p2_reloadTime = RELOAD_TIME;
    float time = 0f;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    private ParticleEmitter createFlame(ParticleEmitter flame) {
        flame = new ParticleEmitter("Flame", EMITTER_TYPE, 32 * COUNT_FACTOR);
        flame.setSelectRandomImage(true);
        flame.setStartColor(new ColorRGBA(1f, 0.4f, 0.05f, (float) (1f / COUNT_FACTOR_F)));
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
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        mat.setBoolean("PointSprite", POINT_SPRITE);
        flame.setMaterial(mat);
        return flame;

    }

    private ParticleEmitter createFlash(ParticleEmitter flash) {
        flash = new ParticleEmitter("Flash", EMITTER_TYPE, 24 * COUNT_FACTOR);
        flash.setSelectRandomImage(true);
        flash.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, (float) (1f / COUNT_FACTOR_F)));
        flash.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
        flash.setStartSize(.1f);
        flash.setEndSize(3.0f);
        flash.setShape(new EmitterSphereShape(Vector3f.ZERO, .05f));
        flash.setParticlesPerSec(0);
        flash.setGravity(0, 0, 0);
        flash.setLowLife(.2f);
        flash.setHighLife(.2f);
        flash.setInitialVelocity(new Vector3f(0, 5f, 0));
        flash.setVelocityVariation(1);
        flash.setImagesX(2);
        flash.setImagesY(2);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flash.png"));
        mat.setBoolean("PointSprite", POINT_SPRITE);
        flash.setMaterial(mat);
        return flash;
    }

    private ParticleEmitter createRoundSpark(ParticleEmitter roundspark) {
        roundspark = new ParticleEmitter("RoundSpark", EMITTER_TYPE, 20 * COUNT_FACTOR);
        roundspark.setStartColor(new ColorRGBA(1f, 0.29f, 0.34f, (float) (1.0 / COUNT_FACTOR_F)));
        roundspark.setEndColor(new ColorRGBA(0, 0, 0, (float) (0.5f / COUNT_FACTOR_F)));
        roundspark.setStartSize(1.2f);
        roundspark.setEndSize(1.8f);
        roundspark.setShape(new EmitterSphereShape(Vector3f.ZERO, 2f));
        roundspark.setParticlesPerSec(0);
        roundspark.setGravity(0, -.5f, 0);
        roundspark.setLowLife(1.8f);
        roundspark.setHighLife(2f);
        roundspark.setInitialVelocity(new Vector3f(0, 3, 0));
        roundspark.setVelocityVariation(.5f);
        roundspark.setImagesX(1);
        roundspark.setImagesY(1);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/roundspark.png"));
        mat.setBoolean("PointSprite", POINT_SPRITE);
        roundspark.setMaterial(mat);
        return roundspark;
    }

    private ParticleEmitter createSpark(ParticleEmitter spark) {
        spark = new ParticleEmitter("Spark", Type.Triangle, 30 * COUNT_FACTOR);
        spark.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, (float) (1.0f / COUNT_FACTOR_F)));
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
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/spark.png"));
        spark.setMaterial(mat);
        return spark;
    }

    private ParticleEmitter createSmokeTrail(ParticleEmitter smoketrail) {
        smoketrail = new ParticleEmitter("SmokeTrail", Type.Triangle, 22 * COUNT_FACTOR);
        smoketrail.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, (float) (1.0f / COUNT_FACTOR_F)));
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
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/smoketrail.png"));
        smoketrail.setMaterial(mat);
        return smoketrail;
    }

    private ParticleEmitter createDebris(ParticleEmitter debris) {
        debris = new ParticleEmitter("Debris", Type.Triangle, 15 * COUNT_FACTOR);
        debris.setSelectRandomImage(true);
        debris.setRandomAngle(true);
        debris.setRotateSpeed(FastMath.TWO_PI * 4);
        debris.setStartColor(new ColorRGBA(1f, 0.59f, 0.28f, (float) (1.0f / COUNT_FACTOR_F)));
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
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/Debris.png"));
        debris.setMaterial(mat);
        return debris;
    }

    private ParticleEmitter createShockwave(ParticleEmitter shockwave) {
        shockwave = new ParticleEmitter("Shockwave", Type.Triangle, 1 * COUNT_FACTOR);
//        shockwave.setRandomAngle(true);
        shockwave.setFaceNormal(Vector3f.UNIT_Y);
        shockwave.setStartColor(new ColorRGBA(.48f, 0.17f, 0.01f, (float) (.8f / COUNT_FACTOR_F)));
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
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/shockwave.png"));
        shockwave.setMaterial(mat);
        return shockwave;
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0f, -1f, 0f));
        //bulletAppState.getPhysicsSpace().setAccuracy(0.005f);
        cam.setLocation(new Vector3f(15f, 20f, 60f));
        //cam.lookAt(new Vector3f(1, 1, 0), Vector3f.UNIT_Y);
        
        //Objetos básicos
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        Box b2 = new Box(Vector3f.ZERO, 10, 10, 10);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setColor("Color", ColorRGBA.Green);

        //Player 1
        Vector3f p1_pos = new Vector3f(-5.0f, 1f, 0f);
        player1 = new Player("player 1", mat, p1_pos);
        rootNode.attachChild(player1.getPlayerNode());

        //Player 2
        Vector3f p2_pos = new Vector3f(30.0f, 1f, 0f);
        player2 = new Player("player 2", mat, p2_pos);
        rootNode.attachChild(player2.getPlayerNode());


        //Platform 1
        Vector3f plat1_pos = new Vector3f(0, -10, 0);
        Geometry plat1 = new Geometry("Plat1", b2);
        plat1.setLocalTranslation(plat1_pos);
        plat1.setMaterial(mat2);
        rootNode.attachChild(plat1);

        //Platform 2
        Vector3f plat2_pos = new Vector3f(30, -10, 0);
        Geometry plat2 = new Geometry("Plat2", b2);
        plat2.setLocalTranslation(plat2_pos);
        plat2.setMaterial(mat2);
        rootNode.attachChild(plat2);

        //Fisica dos objetos
        RigidBodyControl plat1_rb = new RigidBodyControl(2.0f);
        RigidBodyControl plat2_rb = new RigidBodyControl(0.0f);

        //Associacao da fisica
        plat1.addControl(plat1_rb);
        plat2.addControl(plat2_rb);



        plat1_rb.setKinematic(true);
        plat2_rb.setKinematic(true);

        bulletAppState.getPhysicsSpace().add(player1.getPlayerControl());
        bulletAppState.getPhysicsSpace().add(player2.getPlayerControl());
        bulletAppState.getPhysicsSpace().add(plat1_rb);
        bulletAppState.getPhysicsSpace().add(plat2_rb);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
        bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        initKeys();
        initHPs();
        initReloads();
    }
    private AnalogListener analogListener = new AnalogListener() {

        public void onAnalog(String name, float value, float tpf) {
            Vector3f player1_pos = player1.getLocalTranslation();
            Vector3f player2_pos = player2.getLocalTranslation();
            if (name.equals("P1_Left")) {
                Vector3f temp = new Vector3f(player1_pos.x - velocity, player1_pos.y, player1_pos.z);
                player1.setLocalTranslation(temp);
            } else if (name.equals("P1_Right")) {
                Vector3f temp = new Vector3f(player1_pos.x + velocity, player1_pos.y, player1_pos.z);
                player1.setLocalTranslation(temp);
            } else if (name.equals("P1_Up")) {
                Vector3f temp = new Vector3f(player1_pos.x, player1_pos.y, player1_pos.z - velocity);
                player1.setLocalTranslation(temp);
            } else if (name.equals("P1_Down")) {
                Vector3f temp = new Vector3f(player1_pos.x, player1_pos.y, player1_pos.z + velocity);
                player1.setLocalTranslation(temp);
            } else if (name.equals("P2_Left")) {
                Vector3f temp = new Vector3f(player2_pos.x - velocity, player2_pos.y, player2_pos.z);
                player2.setLocalTranslation(temp);
            } else if (name.equals("P2_Right")) {
                Vector3f temp = new Vector3f(player2_pos.x + velocity, player2_pos.y, player2_pos.z);
                player2.setLocalTranslation(temp);
            } else if (name.equals("P2_Up")) {
                Vector3f temp = new Vector3f(player2_pos.x, player2_pos.y, player2_pos.z - velocity);
                player2.setLocalTranslation(temp);
            } else if (name.equals("P2_Down")) {
                Vector3f temp = new Vector3f(player2_pos.x, player2_pos.y, player2_pos.z + velocity);
                player2.setLocalTranslation(temp);
            }
           
        }
    };

    private void initKeys() {
        inputManager.addMapping("P1_Left", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("P1_Right", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("P1_Up", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("P1_Down", new KeyTrigger(KeyInput.KEY_G));
        inputManager.addMapping("P2_Left", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("P2_Right", new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping("P2_Up", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("P2_Down", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("P1_Shoot", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("P2_Shoot", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(analogListener, new String[]{"P1_Left", "P1_Right", "P1_Up", "P1_Down",
                    "P2_Left", "P2_Right", "P2_Up", "P2_Down"});
        inputManager.addListener(actionListener, new String[]{"P1_Shoot", "P2_Shoot"});

    }
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("P1_Shoot") && !keyPressed && !player1_reload) {
                player1_power = System.currentTimeMillis() - player1_power;
                makeBall(player1_power, player1.getPlayerGeo(), 1);
                player1_reload = true;
                p1_reloadTime = RELOAD_TIME;
                
            } else if (name.equals("P1_Shoot")) {
                player1_power = System.currentTimeMillis();
            } else if (name.equals("P2_Shoot") && !keyPressed && !player2_reload) {
                player2_power = System.currentTimeMillis() - player2_power;
                makeBall(player2_power, player2.getPlayerGeo(), -1);
                player2_reload = true;
                p2_reloadTime = RELOAD_TIME;
            } else {
                player2_power = System.currentTimeMillis();
            }
        }
    };

    private void makeBall(long power, Geometry geom, int d) {

        Sphere sphere = new Sphere(32, 32, 0.4f, true, false);
        sphere.setTextureMode(TextureMode.Projected);

        Geometry ball_geo = new Geometry("cannon ball", sphere);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Yellow);
        ball_geo.setMaterial(mat);
        rootNode.attachChild(ball_geo);

        Vector3f v = geom.getLocalTranslation();
        ball_geo.setLocalTranslation(new Vector3f(v.x, v.y + 1.5f, v.z));

        RigidBodyControl ball_phy = new RigidBodyControl(1f);

        ball_geo.addControl(ball_phy);

        bulletAppState.getPhysicsSpace().add(ball_phy);

        ball_phy.setLinearVelocity(new Vector3f(d * power / 20, power / 40, 0));
    }

    public void explosion(Vector3f pos) {

        Node explosionEffect = new Node("explosionFX");
        ParticleEmitter flame = null, flash = null, spark = null, roundspark = null, smoketrail = null, debris = null,
                shockwave = null;
        flame = createFlame(flame);
        explosionEffect.attachChild(flame);

        flash = createFlash(flash);
        explosionEffect.attachChild(flash);

        spark = createSpark(spark);
        explosionEffect.attachChild(spark);

        roundspark = createRoundSpark(roundspark);
        explosionEffect.attachChild(roundspark);

        smoketrail = createSmokeTrail(smoketrail);
        explosionEffect.attachChild(smoketrail);

        debris = createDebris(debris);
        explosionEffect.attachChild(debris);

        shockwave = createShockwave(shockwave);
        explosionEffect.attachChild(shockwave);

        explosionEffect.setLocalScale(0.5f);
        renderManager.preloadScene(explosionEffect);

        rootNode.attachChild(explosionEffect);

        explosionEffect.setLocalTranslation(pos);

        flash.emitAllParticles();
        spark.emitAllParticles();
        smoketrail.emitAllParticles();
        debris.emitAllParticles();
        shockwave.emitAllParticles();
        
        final ExecutorService service;
        final Future<Node>  task;

        service = Executors.newFixedThreadPool(1);        
        task    = service.submit(new ExplosionCleaner(explosionEffect));
        tasks.add(task);
      
    }

    public void initHPs() {
        guiNode.detachAllChildren();
        Material black = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        black.setColor("Color", ColorRGBA.Black);
        Material green = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        green.setColor("Color", ColorRGBA.Green);
        Vector3f leftPosition = new Vector3f(10, settings.getHeight() - 20, 0);
        Vector3f rightPosition = new Vector3f(settings.getWidth() - 200 - 20, settings.getHeight() - 20, 0);
        hp1 = new HitPointsBox("hp1b", leftPosition, green, black);
        hp2 = new HitPointsBox("hp2b", rightPosition, green, black);
        // player1.addHitPointBox(hp1);
        guiNode.attachChild(hp1.getHpNode());
        guiNode.attachChild(hp2.getHpNode());
    }

    @Override
    public void simpleUpdate(float tpf) {
        time += tpf / speed;
        
        int i = 0;
        while(tasks.size() > 0 && tasks.get(i).isDone()) {
            try {
                rootNode.detachChild(tasks.get(i).get());
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            tasks.remove(i);
        }
        
        
        if (player1_reload){
            p1_reloadTime -= tpf / speed;
            reloadP1.setText("RELOAD");
        }
        if (player2_reload){    
            p2_reloadTime -= tpf / speed;
            reloadP2.setText("RELOAD");
            reloadP2.setLocalTranslation(settings.getWidth() - reloadP2.getLineWidth() - 20, settings.getHeight() - reloadP2.getLineHeight()- 20, 0); // position
        }
        if (p1_reloadTime <= 0f){
            player1_reload = false;
            reloadP1.setText("");
        }
        if (p2_reloadTime <= 0f){
            player2_reload = false;
             reloadP2.setText("");
        }
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }

    public void collision(PhysicsCollisionEvent pce) {
        Spatial cannon = null;
        Player p = null;
        Vector3f pos = null;
        if (pce.getNodeA().getName().equals("cannon ball")) {
            cannon = pce.getNodeA();
            pos = cannon.getLocalTranslation();
        } else if (pce.getNodeB().getName().equals("cannon ball")) {
            cannon = pce.getNodeB();
            pos = cannon.getLocalTranslation();

        }

        if (pce.getNodeA().getName().equals(player1.getPlayerName()) || pce.getNodeB().getName().equals(player1.getPlayerName())) {
            p = player1;
        }

        if (pce.getNodeA().getName().equals(player2.getPlayerName()) || pce.getNodeB().getName().equals(player2.getPlayerName())) {
            p = player2;
        }

        if (p != null && cannon != null) {

            if (rootNode.hasChild(cannon)) {
                p.setHitPoints(p.getHitPoints() - 1);
                if (p.equals(player1)) {
                    hp1.loseLife(1);
                } else {
                    hp2.loseLife(1);
                }
                System.out.println(p.getPlayerName() + " GOT HIT!\n HITPOINTS LEFT:" + p.getHitPoints());
                explosion(cannon.getLocalTranslation());
                rootNode.detachChild(cannon);

                bulletAppState.getPhysicsSpace().remove(cannon.getControl(0));

            }

        }
    }
  
    private void initReloads() {
        reloadP1 = new BitmapText(guiFont, false);          
        reloadP1.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        reloadP1.setColor(ColorRGBA.Red);                             // font color
        reloadP1.setText("");             // the text
        reloadP1.setLocalTranslation(10, settings.getHeight() - reloadP1.getLineHeight()- 20, 0); // position
        guiNode.attachChild(reloadP1);
        
        reloadP2 = new BitmapText(guiFont, false);          
        reloadP2.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        reloadP2.setColor(ColorRGBA.Red);                             // font color
        reloadP2.setText("");             // the text
        reloadP2.setLocalTranslation(settings.getWidth() - reloadP2.getLineWidth() - 20, settings.getHeight() - reloadP2.getLineHeight()- 20, 0); // position
        guiNode.attachChild(reloadP2);
    }
}
