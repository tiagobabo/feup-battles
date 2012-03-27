package mygame;

import mygame.sfx.ExplosionCleaner;
import com.bulletphysics.collision.shapes.CollisionShape;
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
import com.jme3.audio.AudioNode;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.SkyFactory;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.sfx.*;
import mygame.superPower.*;

public class Main extends SimpleApplication implements PhysicsCollisionListener {

    static final float RELOAD_TIME = 0.5f;
    private BulletAppState bulletAppState;
    float velocity = 0.05f;
    public static Player player1;
    public static Player player2;
    public static final int COUNT_FACTOR = 1;
    public static final float COUNT_FACTOR_F = 1f;
    public static final boolean POINT_SPRITE = true;
    public static final Type EMITTER_TYPE = POINT_SPRITE ? Type.Point : Type.Triangle;
    HitPointsBox hp1;
    HitPointsBox hp2;
    public static ManaBox mana1;
    public static ManaBox mana2;
    BallPowerBox bp1;
    BallPowerBox bp2;
    BitmapText info;
    BitmapText reloadP1;
    BitmapText reloadP2;
    long player1_power = 0;
    long player2_power = 0;
    float powerScale = 5.0f;
    float controlForce = 0.1f;
    boolean player1_shoot = false, player2_shoot = false;
    boolean player1_reload = false, player2_reload = false;
    boolean firstPlayer = true;
    float p1_reloadTime = RELOAD_TIME, p2_reloadTime = RELOAD_TIME;
    float time = 0f;
    public ArrayList< Future<Node>> tasks = new ArrayList< Future<Node>>();
    BasicShadowRenderer bsr;
    public float boxX = 10.0f;
    public float boxY = 10.0f;
    public float boxZ = 2.0f;
    public Vector3f plat1_pos = new Vector3f(15f, -20f, -140f);
    public Vector3f plat2_pos = new Vector3f(-20f, -20f, -140f);
    private RigidBodyControl landscape;
    private TerrainQuad terrain;
    private Material mat_terrain;
    static private Main app;

    public static void main(String[] args) {
        app = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setResolution(640, 480);
        app.setShowSettings(false); // splashscreen
        app.setSettings(settings);
        app.start();
    }
    private boolean inGame = false;


   

    @Override
    public void simpleInitApp() {

        app.setDisplayFps(false);
        app.setDisplayStatView(false);

        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        flyCam.setDragToRotate(true);

        //nifty.loadStyleFile("nifty-default-styles.xml");
        //nifty.loadControlFile("nifty-default-controls.xml");

        nifty.fromXml("homeScreen.xml", "start", new MyStartScreen(app));
        

    }

    public void startGame() {
        rootNode.setShadowMode(ShadowMode.Off);
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0f, -1f, 0f));
        //bulletAppState.getPhysicsSpace().setAccuracy(0.005f);
        flyCam.setMoveSpeed(50);
        cam.setLocation(new Vector3f(-2.5f, 25f, -87));
        cam.lookAtDirection(new Vector3f(0f, -0.55f, -0.84f), Vector3f.UNIT_Y);
        flyCam.setEnabled(false);
        //cam.setDirection(new Vector3f(0.026962247, -0.3055602, -0.9517908));

        //Objetos básicos
        Box b2 = new Box(Vector3f.ZERO, boxX, boxY, boxZ);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        Material matp2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matp2.setColor("Color", ColorRGBA.Blue);


        Material mat2 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat2.setBoolean("m_UseMaterialColors", true);
        mat2.setColor("m_Ambient", ColorRGBA.White);
        mat2.setColor("m_Diffuse", ColorRGBA.White);
        mat2.setColor("m_Specular", ColorRGBA.White);
        mat2.setFloat("m_Shininess", 12);
        mat2.setTexture("DiffuseMap", assetManager.loadTexture("parede.jpg"));
        mat2.setTexture("NormalMap", assetManager.loadTexture("parede.jpg"));
        //Player 1
        Vector3f p1_pos = new Vector3f(-20f, -9f, -140f);
        SuperPower sp1 = new CivilSuperPower();
        player1 = new Player("player 1", mat, p1_pos, sp1);
        Keys k = new Keys(KeyInput.KEY_A, KeyInput.KEY_D, KeyInput.KEY_LCONTROL, KeyInput.KEY_LSHIFT);
        player1.setKeys(k);
        rootNode.attachChild(player1.getPlayerNode());

        //Player 2
        Vector3f p2_pos = new Vector3f(15.0f, -9f, -140f);
        SuperPower sp2 = new InformaticSuperPower();
        player2 = new Player("player 2", matp2, p2_pos, sp2);

        Keys k1 = new Keys(KeyInput.KEY_LEFT,KeyInput.KEY_RIGHT,KeyInput.KEY_RMENU,KeyInput.KEY_RSHIFT);

        player2.setKeys(k1);
        rootNode.attachChild(player2.getPlayerNode());


        Node platforms = new Node();

        //Platform 1
        Geometry plat1 = new Geometry("Plat1", b2);
        plat1.setLocalTranslation(plat1_pos);
        plat1.setMaterial(mat2);
        platforms.attachChild(plat1);

        //Platform 2
        Geometry plat2 = new Geometry("Plat2", b2);
        plat2.setLocalTranslation(plat2_pos);
        plat2.setMaterial(mat2);
        platforms.attachChild(plat2);
        rootNode.attachChild(platforms);

        //Fisica dos objetos
        RigidBodyControl plat1_rb = new RigidBodyControl(0.0f);
        RigidBodyControl plat2_rb = new RigidBodyControl(0.0f);

        //Associacao da fisica
        plat1.addControl(plat1_rb);
        plat2.addControl(plat2_rb);


        plat1_rb.setRestitution(0.7f);
        plat2_rb.setRestitution(0.7f);
        plat1_rb.setKinematic(true);
        plat2_rb.setKinematic(true);

        bulletAppState.getPhysicsSpace().add(player1.getPlayerControl());
        bulletAppState.getPhysicsSpace().add(player2.getPlayerControl());
        bulletAppState.getPhysicsSpace().add(plat1_rb);
        bulletAppState.getPhysicsSpace().add(plat2_rb);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
        //bulletAppState.getPhysicsSpace().enableDebug(assetManager);

        //sombras
        bsr = new BasicShadowRenderer(assetManager, 1024);
        bsr.setDirection(new Vector3f(-1, -10, -1).normalizeLocal()); // light direction
        viewPort.addProcessor(bsr);

        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        platforms.setShadowMode(ShadowMode.Receive);

        guiNode.detachAllChildren();
        initKeys();
        initHPs();
        initManaBars();
        initPowerBar();
        initReloads();
        initInfo();


        /** 1. Create terrain material and load four textures into it. */
        mat_terrain = new Material(assetManager,
                "Common/MatDefs/Terrain/Terrain.j3md");

        /** 1.1) Add ALPHA map (for red-blue-green coded splat textures) */
        mat_terrain.setTexture("Alpha", assetManager.loadTexture(
                "Textures/Terrain/splat/alphamap.png"));

        /** 1.2) Add GRASS texture into the red layer (Tex1). */
        Texture grass = assetManager.loadTexture(
                "Textures/Terrain/splat/grass.jpg");
        grass.setWrap(WrapMode.Repeat);
        mat_terrain.setTexture("Tex1", grass);
        mat_terrain.setFloat("Tex1Scale", 64f);

        /** 1.3) Add DIRT texture into the green layer (Tex2) */
        Texture dirt = assetManager.loadTexture(
                "Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(WrapMode.Repeat);
        mat_terrain.setTexture("Tex2", dirt);
        mat_terrain.setFloat("Tex2Scale", 32f);

        /** 1.4) Add ROAD texture into the blue layer (Tex3) */
        Texture rock = assetManager.loadTexture(
                "Textures/Terrain/splat/road.jpg");
        rock.setWrap(WrapMode.Repeat);
        mat_terrain.setTexture("Tex3", rock);
        mat_terrain.setFloat("Tex3Scale", 128f);

        /** 2. Create the height map */
        AbstractHeightMap heightmap = null;
        Texture heightMapImage = assetManager.loadTexture(
                "Textures/Terrain/splat/mountains512.png");

        heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();

        /** 3. We have prepared material and heightmap. 
         * Now we create the actual terrain:
         * 3.1) Create a TerrainQuad and name it "my terrain".
         * 3.2) A good value for terrain tiles is 64x64 -- so we supply 64+1=65.
         * 3.3) We prepared a heightmap of size 512x512 -- so we supply 512+1=513.
         * 3.4) As LOD step scale we supply Vector3f(1,1,1).
         * 3.5) We supply the prepared heightmap itself.
         */
        terrain = new TerrainQuad("my terrain", 65, 513, heightmap.getHeightMap());

        /** 4. We give the terrain its material, position & scale it, and attach it. */
        terrain.setMaterial(mat_terrain);
        terrain.setLocalTranslation(0, -100, 0);
        terrain.setLocalScale(2f, 1f, 2f);
        rootNode.attachChild(terrain);

        /** 5. The LOD (level of detail) depends on were the camera is: */
        List<Camera> cameras = new ArrayList<Camera>();
        cameras.add(getCamera());
        TerrainLodControl control = new TerrainLodControl(terrain, cameras);
        terrain.addControl(control);



        rootNode.attachChild(SkyFactory.createSky(
                assetManager, "sky2.jpg", true));

        AudioNode back = new AudioNode(assetManager, "back.wav");
        back.setLooping(true);
        back.play();
        inGame = true;
    }
    private AnalogListener analogListener = new AnalogListener() {

        public void onAnalog(String name, float value, float tpf) {
            Vector3f player1_pos = player1.getLocalTranslation();
            Vector3f player2_pos = player2.getLocalTranslation();
            if (player1.isAlive()) {
                if (name.equals("P1_Left")) {
                    if (player1.getBall() != null) {
                        player1.getBall().getObjectId().applyCentralImpulse(new javax.vecmath.Vector3f(-controlForce * player1.getSwapped(), 0.0f, 0.0f));
                    } else {
                        if ((player1.getSwapped() == 1 && player1.getPlayerControl().getPhysicsLocation().x > (plat2_pos.x - boxX + player1.getSizeX()))
                                || (player1.getSwapped() == -1 && player1.getPlayerControl().getPhysicsLocation().x < (plat2_pos.x + boxX - player1.getSizeX()))) {
                            Vector3f temp = new Vector3f(player1_pos.x - velocity * player1.getSwapped(), player1_pos.y, player1_pos.z);
                            player1.setLocalTranslation(temp);
                        }
                    }
                } else if (name.equals("P1_Right")) {
                    if (player1.getBall() != null) {
                        player1.getBall().getObjectId().applyCentralImpulse(new javax.vecmath.Vector3f(controlForce * player1.getSwapped(), 0.0f, 0.0f));
                    } else {
                        if ((player1.getSwapped() == 1 && player1.getPlayerControl().getPhysicsLocation().x < (plat2_pos.x + boxX - player1.getSizeX()))
                                || (player1.getSwapped() == -1 && player1.getPlayerControl().getPhysicsLocation().x > (plat2_pos.x - boxX + player1.getSizeX()))) {
                            Vector3f temp = new Vector3f(player1_pos.x + velocity * player1.getSwapped(), player1_pos.y, player1_pos.z);
                            player1.setLocalTranslation(temp);
                        }
                    }

                } else if (name.equals("P1_Up")) {
                    if (player1.getBall() != null) {
                        player1.getBall().getObjectId().applyCentralImpulse(new javax.vecmath.Vector3f(0.0f, 0.0f, -controlForce));
                    } else {
                        Vector3f temp = new Vector3f(player1_pos.x, player1_pos.y, player1_pos.z - velocity * player1.getSwapped());
                        player1.setLocalTranslation(temp);
                    }
                } else if (name.equals("P1_Down")) {
                    if (player1.getBall() != null) {
                        player1.getBall().getObjectId().applyCentralImpulse(new javax.vecmath.Vector3f(0.0f, 0.0f, controlForce * player1.getSwapped()));
                    } else {
                        Vector3f temp = new Vector3f(player1_pos.x, player1_pos.y, player1_pos.z + velocity * player1.getSwapped());
                        player1.setLocalTranslation(temp);
                    }
                }
            }

            if (player2.isAlive()) {
                if (name.equals("P2_Left")) {
                    if (player2.getBall() != null) {
                        player2.getBall().getObjectId().applyCentralImpulse(new javax.vecmath.Vector3f(-controlForce * player2.getSwapped(), 0.0f, 0.0f));
                    } else {
                        if ((player2.getSwapped() == 1 && player2.getPlayerControl().getPhysicsLocation().x > (plat1_pos.x - boxX + player2.getSizeX()))
                                || (player2.getSwapped() == -1 && player2.getPlayerControl().getPhysicsLocation().x < (plat1_pos.x + boxX - player2.getSizeX()))) {
                            Vector3f temp = new Vector3f(player2_pos.x - velocity * player2.getSwapped(), player2_pos.y, player2_pos.z);
                            player2.setLocalTranslation(temp);
                        }
                    }
                } else if (name.equals("P2_Right")) {
                    if (player2.getBall() != null) {
                        player2.getBall().getObjectId().applyCentralImpulse(new javax.vecmath.Vector3f(controlForce * player2.getSwapped(), 0.0f, 0.0f));
                    } else {
                        if ((player2.getSwapped() == 1 && player2.getPlayerControl().getPhysicsLocation().x < (plat1_pos.x + boxX - player2.getSizeX()))
                                || (player2.getSwapped() == -1 && player2.getPlayerControl().getPhysicsLocation().x > (plat1_pos.x - boxX + player2.getSizeX()))) {
                            Vector3f temp = new Vector3f(player2_pos.x + velocity * player2.getSwapped(), player2_pos.y, player2_pos.z);
                            player2.setLocalTranslation(temp);
                        }
                    }
                } else if (name.equals("P2_Up")) {
                    if (player2.getBall() != null) {
                        player2.getBall().getObjectId().applyCentralImpulse(new javax.vecmath.Vector3f(0.0f, 0.0f, -controlForce * player2.getSwapped()));
                    } else {
                        Vector3f temp = new Vector3f(player2_pos.x, player2_pos.y, player2_pos.z - velocity * player2.getSwapped());
                        player2.setLocalTranslation(temp);
                    }
                } else if (name.equals("P2_Down")) {
                    if (player2.getBall() != null) {
                        player2.getBall().getObjectId().applyCentralImpulse(new javax.vecmath.Vector3f(0.0f, 0.0f, controlForce * player2.getSwapped()));
                    } else {
                        Vector3f temp = new Vector3f(player2_pos.x, player2_pos.y, player2_pos.z + velocity * player2.getSwapped());
                        player2.setLocalTranslation(temp);
                    }
                }
            }
        }
    };

    private void initKeys() {

        inputManager.addMapping("P1_Left", new KeyTrigger(player1.getLeftKey()));
        inputManager.addMapping("P1_Right", new KeyTrigger(player1.getRightKey()));
        inputManager.addMapping("P1_Up", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("P1_Down", new KeyTrigger(KeyInput.KEY_G));
        inputManager.addMapping("P2_Left", new KeyTrigger(player2.getLeftKey()));
        inputManager.addMapping("P2_Right", new KeyTrigger(player2.getRightKey()));
        inputManager.addMapping("P2_Up", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("P2_Down", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("P1_SP", new KeyTrigger(player1.getSuperPowerKey()));
        inputManager.addMapping("P2_SP", new KeyTrigger(player2.getSuperPowerKey()));
        inputManager.addMapping("P1_Shoot", new KeyTrigger(player1.getFireKey()));
        inputManager.addMapping("P2_Shoot", new KeyTrigger(player2.getFireKey()));
        inputManager.addListener(analogListener, new String[]{"P1_Left", "P1_Right",
                    "P2_Left", "P2_Right"});
        inputManager.addListener(actionListener, new String[]{"P1_Shoot", "P2_Shoot", "P1_SP", "P2_SP"});

    }
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean keyPressed, float tpf) {
            if (firstPlayer && player1.getBall() == null && player1.isAlive()) {
                if (name.equals("P1_Shoot") && !keyPressed && !player1_reload) {
                    if (bp1.getCurrentPower() > 1) {
                        player1.setBall(makeBall(bp1.getCurrentPower() * powerScale, player1.getPlayerGeo(), 1));
                        player1_reload = true;
                        player1_shoot = false;
                        p1_reloadTime = RELOAD_TIME;
                    }
                } else if (name.equals("P1_Shoot") && !player1_reload) {
                    player1_shoot = true;
                }
            } else if (!firstPlayer && player2.getBall() == null && player2.isAlive()) {
                if (name.equals("P2_Shoot") && !keyPressed && !player2_reload) {
                    if (bp2.getCurrentPower() > 1) {
                        player2.setBall(makeBall(bp2.getCurrentPower() * powerScale, player2.getPlayerGeo(), -1));
                        player2_reload = true;
                        player2_shoot = false;
                        p2_reloadTime = RELOAD_TIME;
                    }
                } else if (name.equals("P2_Shoot") && !player2_reload) {
                    player2_shoot = true;
                }
            }
            if (name.equals("P1_SP") && !keyPressed) {
                if (!player1.isSuperPowerInUse()) {
                    System.out.println("PLAYER 1 SP");
                    player1.useSuperPower(1);
                }

            }
            if (name.equals("P2_SP") && !keyPressed) {
                if (!player2.isSuperPowerInUse()) {
                    System.out.println("PLAYER 2 SP");
                    player2.useSuperPower(2);
                }

            }
        }
    };
    Geometry ball_geo = null;

    private RigidBodyControl makeBall(float power, Geometry geom, int d) {

        Sphere sphere = new Sphere(32, 32, 0.4f, true, false);
        sphere.setTextureMode(TextureMode.Projected);

        ball_geo = new Geometry("cannon ball", sphere);
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("m_UseMaterialColors", true);
        mat.setColor("m_Ambient", ColorRGBA.Orange);
        mat.setColor("m_Diffuse", ColorRGBA.Orange);
        mat.setColor("m_Specular", ColorRGBA.White);
        mat.setFloat("m_Shininess", 12);

        ball_geo.setMaterial(mat);


        ball_geo.setShadowMode(ShadowMode.Cast);
        rootNode.attachChild(ball_geo);

        Vector3f v = geom.getLocalTranslation();
        ball_geo.setLocalTranslation(new Vector3f(v.x, v.y + 1.5f, v.z));

        RigidBodyControl ball_phy = new RigidBodyControl(1f);

        ball_geo.addControl(ball_phy);
        bulletAppState.getPhysicsSpace().add(ball_phy);
        ball_phy.setRestitution(0.7f);
        ball_phy.setLinearVelocity(new Vector3f(d * power / 20, power / 20, 0));

        AudioNode boom = new AudioNode(assetManager, "shoot.wav");
        boom.play();

        //ChaseCamera chaseCam = new ChaseCamera(cam, ball_geo, inputManager);

        return ball_phy;
    }

ParticleEmitter flame = null, flash = null, spark = null, roundspark = null, smoketrail = null, debris = null,
                shockwave = null;
    
    private Material createMat(String m,String texture){
        Material mat = new Material(assetManager, m);
        mat.setTexture("Texture", assetManager.loadTexture(texture));
        mat.setBoolean("PointSprite", POINT_SPRITE);
        return mat;
    }
    public void explosion(Vector3f pos, float explosionSize) {

        Node explosionEffect = new Node("explosionFX");
        
        Material flameMat = createMat("Common/MatDefs/Misc/Particle.j3md","Effects/Explosion/flame.png");
        flame = new Flame(flameMat).getFlame();

        explosionEffect.attachChild(flame);

        Material flashMat = createMat("Common/MatDefs/Misc/Particle.j3md","Effects/Explosion/flash.png");
        flash = new Flash(flashMat).getFlash();
        explosionEffect.attachChild(flash);
        
        Material sparkMat = createMat("Common/MatDefs/Misc/Particle.j3md","Effects/Explosion/spark.png");
        spark = new Spark(sparkMat).getSpark();
        explosionEffect.attachChild(spark);


        Material roundSparkMat = createMat("Common/MatDefs/Misc/Particle.j3md","Effects/Explosion/roundspark.png");
        roundspark = new RoundSpark(roundSparkMat).getRoundSpark();
        explosionEffect.attachChild(roundspark);

        Material smokeTrailMat = createMat("Common/MatDefs/Misc/Particle.j3md","Effects/Explosion/smoketrail.png");
        smoketrail = new SmokeTrail(smokeTrailMat).getSmoketrail();
        explosionEffect.attachChild(smoketrail);


        Material debrisMat = createMat("Common/MatDefs/Misc/Particle.j3md","Effects/Explosion/Debris.png");
        debris = new Debris(debrisMat).getDebris();
        explosionEffect.attachChild(debris);


        Material shockMat = createMat("Common/MatDefs/Misc/Particle.j3md","Effects/Explosion/shockwave.png");

        shockwave = new ShockWave(shockMat).getShockwave();
        explosionEffect.attachChild(shockwave);

        explosionEffect.setLocalScale(explosionSize);
        renderManager.preloadScene(explosionEffect);

        rootNode.attachChild(explosionEffect);

        explosionEffect.setLocalTranslation(pos);

        flame.emitAllParticles();
        flash.emitAllParticles();
        spark.emitAllParticles();
        smoketrail.emitAllParticles();
        debris.emitAllParticles();
        shockwave.emitAllParticles();
        AudioNode boom = new AudioNode(assetManager, "boom.wav");
        boom.play();

        final ExecutorService service;
        final Future<Node> task;

        service = Executors.newFixedThreadPool(1);
        task = service.submit(new ExplosionCleaner(explosionEffect));
        tasks.add(task);

    }

    public void initHPs() {

        Material black = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        black.setColor("Color", ColorRGBA.Black);
        Material green = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        green.setColor("Color", ColorRGBA.Green);
        Vector3f leftPosition = new Vector3f(10, settings.getHeight() - 20, 0);
        Vector3f rightPosition = new Vector3f(settings.getWidth() - 200 - 20, settings.getHeight() - 20, 0);
        hp1 = new HitPointsBox("hp1b", leftPosition, green, black);
        hp2 = new HitPointsBox("hp2b", rightPosition, green, black);

        guiNode.attachChild(hp1.getHpNode());
        guiNode.attachChild(hp2.getHpNode());
    }

    public void initManaBars() {

        Material black = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        black.setColor("Color", ColorRGBA.Black);
        Material blue = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blue.setColor("Color", ColorRGBA.Blue);
        Vector3f leftPosition = new Vector3f(10, settings.getHeight() - 40, 0);
        Vector3f rightPosition = new Vector3f(settings.getWidth() - 200 - 20, settings.getHeight() - 40, 0);
        mana1 = new ManaBox("mana1b", leftPosition, blue, black);
        mana2 = new ManaBox("mana2b", rightPosition, blue, black);

        guiNode.attachChild(mana1.getManaNode());
        guiNode.attachChild(mana2.getManaNode());
    }

    private void initPowerBar() {
        Material green = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material yellow = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material red = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        green.setColor("Color", ColorRGBA.Green);
        yellow.setColor("Color", ColorRGBA.Yellow);
        red.setColor("Color", ColorRGBA.Red);
        Vector3f leftPosition = new Vector3f(settings.getWidth() * 0.05f, settings.getHeight() * 0.1f, 0);
        Vector3f rightPosition = new Vector3f(settings.getWidth() * 0.95f, settings.getHeight() * 0.1f, 0);
        bp1 = new BallPowerBox("bp1", leftPosition, green, yellow, red);
        bp2 = new BallPowerBox("bp2", rightPosition, green, yellow, red);
        guiNode.attachChild(bp1.getPowerNode());
        guiNode.attachChild(bp2.getPowerNode());
    }

    @Override
    public void simpleUpdate(float tpf) {


        if (inGame) {

            int i = 0;
            while (tasks.size() > 0 && tasks.get(i).isDone()) {
                try {
                    
                    rootNode.detachChild(tasks.get(i).get());
                    flame.killAllParticles();
                    flash.killAllParticles();
                    spark.killAllParticles();
                    smoketrail.killAllParticles();
                    debris.killAllParticles();
                    shockwave.killAllParticles();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                tasks.remove(i);

            }



            if (player1_shoot) {
                bp1.increasePower();
            }
            if (player2_shoot) {
                bp2.increasePower();
            }

            if (player1.getBall() != null && player1.getBall().getPhysicsLocation().getY() <= -20.0f) {
                //explosion(ball_geo.getLocalTranslation(), 0.5f);
                rootNode.detachChild(ball_geo);
                bulletAppState.getPhysicsSpace().remove(player1.getBall());
                player1.setBall(null);
                changePlayer();
            }
            if (player2.getBall() != null && player2.getBall().getPhysicsLocation().getY() <= -20.0f) {
                //explosion( ball_geo.getLocalTranslation(),0.5f);
                rootNode.detachChild(ball_geo);
                bulletAppState.getPhysicsSpace().remove(player2.getBall());
                player2.setBall(null);
                changePlayer();
            }


            if (player1_reload) {
                if (p1_reloadTime <= 0f) {
                    bp1.resetPower();
                    player1_reload = false;
                    reloadP1.setText("");
                } else {
                    p1_reloadTime -= tpf / speed;
                    reloadP1.setText("RELOAD");
                }
            }

            if (player2_reload) {
                if (p2_reloadTime <= 0f) {
                    bp2.resetPower();
                    player2_reload = false;
                    reloadP2.setText("");
                } else {
                    p2_reloadTime -= tpf / speed;
                    reloadP2.setText("RELOAD");
                    reloadP2.setLocalTranslation(settings.getWidth() - reloadP2.getLineWidth() - 20, settings.getHeight() - reloadP2.getLineHeight() - 20, 0); // position
                }
            }


            if (mana1.getCurrentMana() < mana1.getMaxMana()) {
                mana1.regainMana();
            }
            if (mana2.getCurrentMana() < mana2.getMaxMana()) {
                mana2.regainMana();
            }
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
        } else if (pce.getNodeA().getName().equals(player2.getPlayerName()) || pce.getNodeB().getName().equals(player2.getPlayerName())) {
            p = player2;
        } else {
            explosion(cannon.getLocalTranslation(), 0.01f);
            rootNode.detachChild(cannon);
            bulletAppState.getPhysicsSpace().remove(cannon.getControl(0));
            if (firstPlayer) {
                player1.setBall(null);
            } else {
                player2.setBall(null);
            }
            changePlayer();
        }

        if (p != null && cannon != null && !p.immune) {

            if (rootNode.hasChild(cannon)) {
                if (firstPlayer) {
                    player1.setBall(null);
                    changePlayer();
                } else {
                    player2.setBall(null);
                    changePlayer();
                }

                if (p.equals(player1)) {
                    hp1.loseLife(1);
                    System.out.println(p.getPlayerName() + " GOT HIT!\n HITPOINTS LEFT:" + hp1.getCurrentLife());
                    if (hp1.getCurrentLife() == 0) {
                        death(player1);
                    }
                } else {
                    hp2.loseLife(1);
                    System.out.println(p.getPlayerName() + " GOT HIT!\n HITPOINTS LEFT:" + hp2.getCurrentLife());
                    if (hp1.getCurrentLife() == 0) {
                        death(player2);
                    }
                }

                explosion(cannon.getLocalTranslation(), 0.5f);
                rootNode.detachChild(cannon);

                bulletAppState.getPhysicsSpace().remove(cannon.getControl(0));

            }

        }
    }

    private void initInfo() {
        info = new BitmapText(guiFont, false);
        info.setSize(guiFont.getCharSet().getRenderedSize());
        info.setColor(ColorRGBA.White);
        info.setText("Player 1");
        info.setLocalTranslation(settings.getWidth() / 2 - info.getLineWidth() / 2, settings.getHeight() - info.getLineHeight() - 10, 0.0f);
        guiNode.attachChild(info);
    }

    private void initReloads() {
        reloadP1 = new BitmapText(guiFont, false);
        reloadP1.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        reloadP1.setColor(ColorRGBA.Red);                             // font color
        reloadP1.setText("");             // the text
        reloadP1.setLocalTranslation(10, settings.getHeight() - reloadP1.getLineHeight() - 20, 0); // position
        guiNode.attachChild(reloadP1);

        reloadP2 = new BitmapText(guiFont, false);
        reloadP2.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        reloadP2.setColor(ColorRGBA.Red);                             // font color
        reloadP2.setText("");             // the text
        reloadP2.setLocalTranslation(settings.getWidth() - reloadP2.getLineWidth() - 20, settings.getHeight() - reloadP2.getLineHeight() - 20, 0); // position
        guiNode.attachChild(reloadP2);
    }

    private void death(Player p) {
        explosion(p.getLocalTranslation(), 5.0f);
        rootNode.detachChild(p.getPlayerNode());
        bulletAppState.getPhysicsSpace().remove(p.getPlayerControl());
        p.setAlive(false);
    }

    private void changePlayer() {
        firstPlayer = !firstPlayer;
        if (firstPlayer) {
            info.setText("Player 1");
        } else {
            info.setText("Player 2");
        }
    }
}
