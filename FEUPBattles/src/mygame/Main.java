package mygame;

import mygame.menus.MyStartScreen;
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
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.font.BitmapText;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
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
import com.jme3.ui.Picture;
import com.jme3.util.SkyFactory;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.tools.SizeValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.menus.AnimationThread;
import mygame.menus.PauseScreen;
import mygame.sfx.*;
import mygame.superPower.*;

public class Main extends SimpleApplication implements PhysicsCollisionListener {

    private BulletAppState bulletAppState;
    public static Player player1;
    public static Player player2;
    public static final int COUNT_FACTOR = 1;
    public static final float COUNT_FACTOR_F = 1f;
    public static final boolean POINT_SPRITE = true;
    public static final Type EMITTER_TYPE = POINT_SPRITE ? Type.Point : Type.Triangle;
    public static HitPointsBox hp1;
    public static HitPointsBox hp2;
    public static ManaBox mana1;
    public static ManaBox mana2;
    BallPowerBox bp1;
    BallPowerBox bp2;
    BallPowerBox[] bps = new BallPowerBox[2];
    BitmapText info;
    long player1_power = 0;
    long player2_power = 0;
    float powerScale = 5.0f;
    float controlForce = 0.1f;
    boolean playerShoot = false;
    float time = 0f;
    public ArrayList< Future<Node>> tasks = new ArrayList< Future<Node>>();
    BasicShadowRenderer bsr;
    public float boxX = 10.0f;
    public float boxY = 10.0f;
    public float boxZ = 2.0f;
    public Vector3f plat1_pos = new Vector3f(15f, -20f, -140f);
    public Vector3f plat2_pos = new Vector3f(-20f, -20f, -140f);
    private TerrainQuad terrain;
    private Material mat_terrain;
    static public Main app;
    private Element progressBarElement;
    private Nifty nifty;
    public static int numHits = 5;
    public static String[] icons = {"inf.png", "civil.png", "chem.png", "electro.png", "bio.png", "mec.png", "metal.png"};
    public static String[] iconsSp = {"inf_sp.png", "civil_sp.png", "chem_sp.png", "electro_sp.png", "bio_sp.png", "mec_sp.png", "metal_sp.png"};
    Random generator;
    int randomFlames = 0;
    ArrayList<AudioNode> scenarioExplosion;
    AudioNode backgroundMusic;
    public static Player[] players = new Player[2];
    public static ManaBox[] manas = new ManaBox[2];
    public static HitPointsBox[] hps = new HitPointsBox[2];
    public static Picture p1Pic;
    public static Picture p2Pic;
    public static Picture p1PicSp;
    public static Picture p2PicSp;
    public static Picture[] pics = new Picture[2];
    public static Picture[] spPics = new Picture[2];
    private int currentPlayer = 0;

    public static void main(String[] args) {
        //Logger.getLogger("").setLevel(Level.OFF);
        app = new Main();
        AppSettings settings = new AppSettings(true);
        //settings.setResolution(1024, 768);
        //settings.setFullscreen(true);
        //app.setShowSettings(false); // splashscreen
        //app.setSettings(settings);
        app.start();
    }
    private boolean inGame = false;
    private TextRenderer textRenderer;
    public int counter = 0;
    public ESuperPower p1Selected = ESuperPower.None;
    public ESuperPower p2Selected = ESuperPower.None;
    AudioNode winner;
    FilterPostProcessor fpp;
    AmbientLight al;
    DirectionalLight sun;
    NiftyJmeDisplay niftyDisplay;

    @Override
    public void simpleInitApp() {

        final MyStartScreen mss = new MyStartScreen(app);
        app.setDisplayFps(false);
        app.setDisplayStatView(false);

        niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        
        flyCam.setDragToRotate(true);
        flyCam.setEnabled(false);
        AnimationThread at = new AnimationThread(mss);
        mss.setAt(at);
        nifty.fromXml("homeScreen.xml", "splashScreen", mss);
        at.start();
        

        generator = new Random();
        scenarioExplosion = new ArrayList<AudioNode>();
        scenarioExplosion.add(new AudioNode(assetManager, "explosion.wav"));
        scenarioExplosion.add(new AudioNode(assetManager, "explosion2.wav"));
        scenarioExplosion.add(new AudioNode(assetManager, "explosion3.wav"));
        scenarioExplosion.add(new AudioNode(assetManager, "explosion4.wav"));
        scenarioExplosion.add(new AudioNode(assetManager, "explosion5.wav"));
        scenarioExplosion.add(new AudioNode(assetManager, "explosion6.wav"));

        for (AudioNode a : scenarioExplosion) {
            a.setVolume(0.5f);
        }

    }

    public void setProgress(final float progress, String loadingText) {
        final int MIN_WIDTH = 32;
        int pixelWidth = (int) (MIN_WIDTH + (progressBarElement.getParent().getWidth() - MIN_WIDTH) * progress);
        progressBarElement.setConstraintWidth(new SizeValue(pixelWidth + "px"));
        progressBarElement.getParent().layoutElements();
        textRenderer.setText(loadingText);

    }

    public void endGame() {

        rootNode.detachAllChildren();
        guiNode.detachAllChildren();
        app.restart();
        currentPlayer = 0;
        nifty.fromXml("homeScreen.xml", "start", new MyStartScreen(app));


    }

    public void startGame(int counter) {


        nifty.gotoScreen("loading");
        Element element = nifty.getScreen("loading").findElementByName("loadingtext");
        textRenderer = element.getRenderer(TextRenderer.class);
        progressBarElement = nifty.getScreen("loading").findElementByName("progressbar");
        if (counter == 1) {
            rootNode.setShadowMode(ShadowMode.Off);
            bulletAppState = new BulletAppState();
            stateManager.attach(bulletAppState);

            flyCam.setMoveSpeed(50);
            cam.setLocation(new Vector3f(-2.8499677f, 5.944004f, -85.42222f));
            cam.lookAtDirection(new Vector3f(0.00555476f, -0.31822094f, -0.9480003f), Vector3f.UNIT_Y);
            flyCam.setEnabled(false);

            setProgress(0.2f, "Loading objects and materials...");
        } else if (counter == 2) {

            //Objetos b�sicos
            Box b2 = new Box(Vector3f.ZERO, boxX, boxY, boxZ);

            Material mat2 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            mat2.setBoolean("m_UseMaterialColors", true);
            mat2.setColor("m_Ambient", ColorRGBA.White);
            mat2.setColor("m_Diffuse", ColorRGBA.White);
            mat2.setColor("m_Specular", ColorRGBA.White);
            mat2.setFloat("m_Shininess", 0.5f);
            mat2.setTexture("DiffuseMap", assetManager.loadTexture("parede.jpg"));
            mat2.setTexture("NormalMap", assetManager.loadTexture("parede.jpg"));



            //Player 1

            Vector3f p1_pos = new Vector3f(-20f, -10.1f, -140f);

            player1 = new Player("player 1", p1_pos, p1Selected, assetManager, Math.PI / 2.0f);
            Keys k = new Keys(KeyInput.KEY_A, KeyInput.KEY_D, KeyInput.KEY_LCONTROL, KeyInput.KEY_LSHIFT);
            player1.setKeys(k);
            rootNode.attachChild(player1.getPlayerNode());

            //Player 2

            Vector3f p2_pos = new Vector3f(15.0f, -10.1f, -140f);

            player2 = new Player("player 2", p2_pos, p2Selected, assetManager, -Math.PI / 2.0f);

            Keys k1 = new Keys(KeyInput.KEY_LEFT, KeyInput.KEY_RIGHT, KeyInput.KEY_UP, KeyInput.KEY_RSHIFT);

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


            //sombras
            bsr = new BasicShadowRenderer(assetManager, 1024);
            bsr.setDirection(new Vector3f(-1, -10, -1).normalizeLocal()); // light direction
            viewPort.addProcessor(bsr);

            al = new AmbientLight();
            al.setColor(ColorRGBA.White.mult(0.8f));
            rootNode.addLight(al);

            platforms.setShadowMode(ShadowMode.Receive);
            setProgress(0.5f, "Loading landscapes and sky...");

            players[0] = player1;
            players[1] = player2;
            scenarios();

        } else if (counter == 3) {


            mat_terrain = new Material(assetManager,
                    "Common/MatDefs/Terrain/Terrain.j3md");


            mat_terrain.setTexture("Alpha", assetManager.loadTexture(
                    "Textures/Terrain/splat/alphamap.png"));


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



            setProgress(0.90f, "Loading sun, fog and screen info...");
        } else if (counter == 4) {
            guiNode.detachAllChildren();
            initKeys();
            initHPs();
            initManaBars();
            initPowerBar();

            initInfo();
            initHudImgs();
            inGame = true;
            nifty.exit();
            sun = new DirectionalLight();
            sun.setColor(ColorRGBA.White);
            sun.setDirection(new Vector3f(0.00555476f, -0.31822094f, -0.9480003f).normalizeLocal());
            rootNode.addLight(sun);

            fpp = new FilterPostProcessor(assetManager);
            //fpp.setNumSamples(4);
            FogFilter fog = new FogFilter();
            fog.setFogColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
            fog.setFogDistance(700.0f);
            fog.setFogDensity(2.5f);
            fpp.addFilter(fog);
            viewPort.addProcessor(fpp);
            backgroundMusic = new AudioNode(assetManager, "back.wav");
            backgroundMusic.setLooping(true);
            backgroundMusic.play();
        }

    }

    private void makeFlame(Vector3f c1) {
        ParticleEmitter flame2 = new ParticleEmitter("Flame", EMITTER_TYPE, 32 * COUNT_FACTOR);
        flame2.setSelectRandomImage(true);
        flame2.setStartColor(new ColorRGBA(1f, 0.4f, 0.05f, (float) (1f / COUNT_FACTOR_F)));
        flame2.setEndColor(new ColorRGBA(.4f, .22f, .12f, 0f));
        flame2.setStartSize(1.3f);
        flame2.setEndSize(2f);
        flame2.setShape(new EmitterSphereShape(Vector3f.ZERO, 1.0f));
        flame2.setParticlesPerSec(0);
        flame2.setGravity(0, -5, 0);
        flame2.setLowLife(.4f);
        flame2.setHighLife(.5f);
        flame2.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 7, 0));
        flame2.getParticleInfluencer().setVelocityVariation(1f);
        flame2.setImagesX(5);
        flame2.setImagesY(5);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        mat.setBoolean("PointSprite", POINT_SPRITE);
        flame2.setMaterial(mat);
        flame2.setLocalTranslation(c1.getX(), c1.getY(), c1.getZ() + 2.0f);
        rootNode.attachChild(flame2);
        flames.add(flame2);
    }

    private void makeCannonFlame(Vector3f c1) {
        ParticleEmitter flame2 = new ParticleEmitter("Flame", EMITTER_TYPE, 32 * COUNT_FACTOR);
        flame2.setSelectRandomImage(true);
        flame2.setStartColor(new ColorRGBA(1f, 0.4f, 0.05f, (float) (1f / COUNT_FACTOR_F)));
        flame2.setEndColor(new ColorRGBA(.4f, .22f, .12f, 0f));
        flame2.setStartSize(0.3f);
        flame2.setEndSize(0.6f);
        flame2.setShape(new EmitterSphereShape(Vector3f.ZERO, 1.0f));
        flame2.setParticlesPerSec(0);
        flame2.setGravity(0, -5, 0);
        flame2.setLowLife(.4f);
        flame2.setHighLife(.5f);
        flame2.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 7, 0));
        flame2.getParticleInfluencer().setVelocityVariation(1f);
        flame2.setImagesX(1);
        flame2.setImagesY(1);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        mat.setBoolean("PointSprite", POINT_SPRITE);
        flame2.setMaterial(mat);
        flame2.setLocalTranslation(c1.getX(), c1.getY(), c1.getZ());
        rootNode.attachChild(flame2);
        flame2.emitAllParticles();
    }

    private void scenarios() {
        //Cenarios
        Material mat3 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat3.setBoolean("m_UseMaterialColors", true);
        mat3.setColor("m_Ambient", ColorRGBA.White);
        mat3.setColor("m_Diffuse", ColorRGBA.White);
        mat3.setColor("m_Specular", ColorRGBA.White);
        mat3.setFloat("m_Shininess", 2);
        mat3.setTexture("DiffuseMap", assetManager.loadTexture("c1.jpg"));
        mat3.setTexture("NormalMap", assetManager.loadTexture("c1.jpg"));


        Material mat4 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat4.setBoolean("m_UseMaterialColors", true);
        mat4.setColor("m_Ambient", ColorRGBA.White);
        mat4.setColor("m_Diffuse", ColorRGBA.White);
        mat4.setColor("m_Specular", ColorRGBA.White);
        mat4.setFloat("m_Shininess", 2);
        mat4.setTexture("DiffuseMap", assetManager.loadTexture("c2.jpg"));
        mat4.setTexture("NormalMap", assetManager.loadTexture("c2.jpg"));

        Material mat5 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat5.setBoolean("m_UseMaterialColors", true);
        mat5.setColor("m_Ambient", ColorRGBA.White);
        mat5.setColor("m_Diffuse", ColorRGBA.White);
        mat5.setColor("m_Specular", ColorRGBA.White);
        mat5.setFloat("m_Shininess", 1);
        mat5.setTexture("DiffuseMap", assetManager.loadTexture("c3.jpg"));
        mat5.setTexture("NormalMap", assetManager.loadTexture("c3.jpg"));

        Material mat6 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat6.setBoolean("m_UseMaterialColors", true);
        mat6.setColor("m_Ambient", ColorRGBA.White);
        mat6.setColor("m_Diffuse", ColorRGBA.White);
        mat6.setColor("m_Specular", ColorRGBA.White);
        mat6.setFloat("m_Shininess", 1);
        mat6.setTexture("DiffuseMap", assetManager.loadTexture("c4.jpg"));
        mat6.setTexture("NormalMap", assetManager.loadTexture("c4.jpg"));

        Vector3f c1 = new Vector3f(-30f, -25f, -170f);
        float scale = 1.0f;
        float rotx = 0.0f;
        float roty = 0.0f;
        float rotz = 0.6f;
        makeWall(scale, c1, mat3, mat4, rotx, roty, rotz);

        c1 = new Vector3f(30f, -25f, -190f);
        makeWall(scale, c1, mat3, mat4, rotx, roty, -0.4f);

        c1 = new Vector3f(20f, -20f, -200f);
        scale = 0.5f;
        makeWall(scale, c1, mat5, mat4, rotx, roty, 0.5f);

        c1 = new Vector3f(-20f, -20f, -200f);
        makeWall(2.0f, c1, mat6, mat4, 0.0f, 0.0f, 0.2f);
    }
    ArrayList<ParticleEmitter> flames = new ArrayList<ParticleEmitter>();

    private void makeWall(float scale, Vector3f c1, Material mat3, Material mat4, float rotx, float roty, float rotz) {
        Box b3 = new Box(Vector3f.ZERO, 10.0f * scale, 10.f * scale, 0.1f * scale);
        Geometry c1_geo = new Geometry("C1", b3);

        c1_geo.setLocalTranslation(c1);
        c1_geo.setMaterial(mat3);
        c1_geo.rotate(rotx, roty, rotz);
        rootNode.attachChild(c1_geo);

        Box b4 = new Box(Vector3f.ZERO, 10.0f * scale, 10.f * scale, 2.0f * scale);
        Vector3f c2 = new Vector3f(c1.getX(), c1.getY(), c1.getZ() - 2.0f * scale);
        Geometry c2_geo = new Geometry("C1", b4);

        c2_geo.setLocalTranslation(c2);
        c2_geo.setMaterial(mat4);
        c2_geo.rotate(rotx, roty, rotz);
        rootNode.attachChild(c2_geo);
        makeFlame(c1);
    }
    private AnalogListener analogListener = new AnalogListener() {

        public void onAnalog(String name, float value, float tpf) {
            if (inGame) {
                Vector3f player1_pos = player1.getLocalTranslation();
                Vector3f player2_pos = player2.getLocalTranslation();
                if (player1.isAlive()) {
                    if (name.equals("P1_Left")) {
                        if (player1.getBall() != null) {
                            player1.getBall().getControl(RigidBodyControl.class).getObjectId().applyCentralImpulse(new javax.vecmath.Vector3f(-controlForce * player1.getSwapped(), 0.0f, 0.0f));
                        } else {
                            if ((player1.getSwapped() == 1 && player1.getPlayerControl().getPhysicsLocation().x > (plat2_pos.x - boxX + player1.getSizeX()))
                                    || (player1.getSwapped() == -1 && player1.getPlayerControl().getPhysicsLocation().x < (plat2_pos.x + boxX - player1.getSizeX()))) {
                                Vector3f temp = new Vector3f(player1_pos.x - player1.getMoveSpeed(), player1_pos.y, player1_pos.z);
                                player1.setLocalTranslation(temp);
                            }
                        }
                    } else if (name.equals("P1_Right")) {
                        if (player1.getBall() != null) {
                            player1.getBall().getControl(RigidBodyControl.class).getObjectId().applyCentralImpulse(new javax.vecmath.Vector3f(controlForce * player1.getSwapped(), 0.0f, 0.0f));
                        } else {
                            if ((player1.getSwapped() == 1 && player1.getPlayerControl().getPhysicsLocation().x < (plat2_pos.x + boxX - player1.getSizeX()))
                                    || (player1.getSwapped() == -1 && player1.getPlayerControl().getPhysicsLocation().x > (plat2_pos.x - boxX + player1.getSizeX()))) {
                                Vector3f temp = new Vector3f(player1_pos.x + player1.getMoveSpeed(), player1_pos.y, player1_pos.z);
                                player1.setLocalTranslation(temp);
                            }
                        }

                    }
                }

                if (player2.isAlive()) {
                    if (name.equals("P2_Left")) {
                        if (player2.getBall() != null) {
                            player2.getBall().getControl(RigidBodyControl.class).getObjectId().applyCentralImpulse(new javax.vecmath.Vector3f(-controlForce * player2.getSwapped(), 0.0f, 0.0f));
                        } else {
                            if ((player2.getSwapped() == 1 && player2.getPlayerControl().getPhysicsLocation().x > (plat1_pos.x - boxX + player2.getSizeX()))
                                    || (player2.getSwapped() == -1 && player2.getPlayerControl().getPhysicsLocation().x < (plat1_pos.x + boxX - player2.getSizeX()))) {
                                Vector3f temp = new Vector3f(player2_pos.x - player2.getMoveSpeed(), player2_pos.y, player2_pos.z);
                                player2.setLocalTranslation(temp);
                            }
                        }
                    } else if (name.equals("P2_Right")) {
                        if (player2.getBall() != null) {
                            player2.getBall().getControl(RigidBodyControl.class).getObjectId().applyCentralImpulse(new javax.vecmath.Vector3f(controlForce * player2.getSwapped(), 0.0f, 0.0f));
                        } else {
                            if ((player2.getSwapped() == 1 && player2.getPlayerControl().getPhysicsLocation().x < (plat1_pos.x + boxX - player2.getSizeX()))
                                    || (player2.getSwapped() == -1 && player2.getPlayerControl().getPhysicsLocation().x > (plat1_pos.x - boxX + player2.getSizeX()))) {
                                Vector3f temp = new Vector3f(player2_pos.x + player2.getMoveSpeed(), player2_pos.y, player2_pos.z);
                                player2.setLocalTranslation(temp);
                            }
                        }
                    }
                }
            }
        }
    };

    private void initKeys() {
        inputManager.clearMappings();
        inputManager.addMapping("P1_Left", new KeyTrigger(player1.getLeftKey()));
        inputManager.addMapping("P1_Right", new KeyTrigger(player1.getRightKey()));
        inputManager.addMapping("P2_Left", new KeyTrigger(player2.getLeftKey()));
        inputManager.addMapping("P2_Right", new KeyTrigger(player2.getRightKey()));
        inputManager.addMapping("P1_SP", new KeyTrigger(player1.getSuperPowerKey()));
        inputManager.addMapping("P2_SP", new KeyTrigger(player2.getSuperPowerKey()));
        inputManager.addMapping("P1_Shoot", new KeyTrigger(player1.getFireKey()));
        inputManager.addMapping("P2_Shoot", new KeyTrigger(player2.getFireKey()));
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_ESCAPE), new KeyTrigger(KeyInput.KEY_P), new KeyTrigger(KeyInput.KEY_PAUSE));

        inputManager.addMapping("Enter", new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addListener(analogListener, new String[]{"P1_Left", "P1_Right",
                    "P2_Left", "P2_Right"});
        inputManager.addListener(actionListener, new String[]{"P1_Shoot", "P2_Shoot", "P1_SP", "P2_SP", "Pause", "Enter"});

    }

    public void resumeGame() {
        nifty.exit();
        bulletAppState.setEnabled(true);
        initHPs();
        initManaBars();
        initPowerBar();
        initInfo();
        inGame = true;
        backgroundMusic.play();
    }
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean keyPressed, float tpf) {


            if (name.equals("Pause") && inGame) {
                guiNode.detachAllChildren();
                bulletAppState.setEnabled(false);
                nifty.fromXml("pauseScreen.xml", "pauseScreen", new PauseScreen(app));
                inGame = false;
                backgroundMusic.pause();
                return;
            } else if (name.equals("Enter")) {
                if (!players[0].isAlive() || !players[1].isAlive()) {
                    restartGame();
                }
            } else {

                if (players[currentPlayer].getBall() == null && players[currentPlayer].isAlive()) {
                    if (name.equals("P" + (currentPlayer + 1) + "_Shoot") && !keyPressed) {
                        if (bps[currentPlayer].getCurrentPower() > 1) {
                            int d = currentPlayer == 1 ? -1 : 1;
                            players[currentPlayer].setBall(makeBall(bps[currentPlayer].getCurrentPower() * powerScale, players[currentPlayer].getPlayerGeo(), d, players[currentPlayer].getBallMaterial()));
                            playerShoot = false;
                        }
                    } else if (name.equals("P" + (currentPlayer + 1) + "_Shoot")) {
                        playerShoot = true;
                    }
                }


                if (name.equals("P1_SP") && !keyPressed) {
                    player1.useSuperPower(0);
                }
                if (name.equals("P2_SP") && !keyPressed) {
                    player2.useSuperPower(1);
                }
            }
        }
    };

    public void restartGame() {
        guiNode.detachAllChildren();
        guiNode.removeLight(null);
        rootNode.detachAllChildren();
        if (winner != null) {
            winner.stop();
        }
        currentPlayer = 0;
        viewPort.removeProcessor(fpp);
        rootNode.removeLight(al);
        rootNode.removeLight(sun);

        nifty.fromXml("homeScreen.xml", "start", new MyStartScreen(app));
    }
    Geometry ball_geo = null;

    private Geometry makeBall(float power, Spatial geom, int d, Material mat) {

        Sphere sphere = new Sphere(32, 32, 0.4f, true, false);
        sphere.setTextureMode(TextureMode.Projected);
        ball_geo = new Geometry("cannon ball", sphere);


        ball_geo.setMaterial(mat);


        ball_geo.setShadowMode(ShadowMode.Cast);
        rootNode.attachChild(ball_geo);

        Vector3f v = geom.getLocalTranslation();
        if (d == 1) {
            ball_geo.setLocalTranslation(new Vector3f(v.x + 4.5f, v.y + 2.2f, v.z));
            makeCannonFlame(new Vector3f(v.x + 4.5f, v.y + 2.2f, v.z));
        } else {
            ball_geo.setLocalTranslation(new Vector3f(v.x - 4.5f, v.y + 2.2f, v.z));
            makeCannonFlame(new Vector3f(v.x - 4.5f, v.y + 2.2f, v.z));
        }

        RigidBodyControl ball_phy = new RigidBodyControl(1f);

        ball_geo.addControl(ball_phy);
        bulletAppState.getPhysicsSpace().add(ball_phy);
        ball_phy.setRestitution(0.7f);
        ball_phy.setLinearVelocity(new Vector3f(d * power / 20, power / 20, 0));

        AudioNode boom = new AudioNode(assetManager, "shoot.wav");
        boom.play();

        //ChaseCamera chaseCam = new ChaseCamera(cam, ball_geo, inputManager);

        return ball_geo;
    }
    ParticleEmitter flame = null, flash = null, spark = null, roundspark = null, smoketrail = null, debris = null,
            shockwave = null;

    private Material createMat(String m, String texture) {
        Material mat = new Material(assetManager, m);
        mat.setTexture("Texture", assetManager.loadTexture(texture));
        mat.setBoolean("PointSprite", POINT_SPRITE);
        return mat;
    }

    public void explosion(Vector3f pos, float explosionSize) {

        Node explosionEffect = new Node("explosionFX");

        Material flameMat = createMat("Common/MatDefs/Misc/Particle.j3md", "Effects/Explosion/flame.png");
        flame = new Flame(flameMat).getFlame();

        explosionEffect.attachChild(flame);

        Material flashMat = createMat("Common/MatDefs/Misc/Particle.j3md", "Effects/Explosion/flash.png");
        flash = new Flash(flashMat).getFlash();
        explosionEffect.attachChild(flash);

        Material sparkMat = createMat("Common/MatDefs/Misc/Particle.j3md", "Effects/Explosion/spark.png");
        spark = new Spark(sparkMat).getSpark();
        explosionEffect.attachChild(spark);


        Material roundSparkMat = createMat("Common/MatDefs/Misc/Particle.j3md", "Effects/Explosion/roundspark.png");
        roundspark = new RoundSpark(roundSparkMat).getRoundSpark();
        explosionEffect.attachChild(roundspark);

        Material smokeTrailMat = createMat("Common/MatDefs/Misc/Particle.j3md", "Effects/Explosion/smoketrail.png");
        smoketrail = new SmokeTrail(smokeTrailMat).getSmoketrail();
        explosionEffect.attachChild(smoketrail);


        Material debrisMat = createMat("Common/MatDefs/Misc/Particle.j3md", "Effects/Explosion/Debris.png");
        debris = new Debris(debrisMat).getDebris();
        explosionEffect.attachChild(debris);


        Material shockMat = createMat("Common/MatDefs/Misc/Particle.j3md", "Effects/Explosion/shockwave.png");

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
        service.shutdown();
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
        hps[0] = hp1;
        hps[1] = hp2;
    }

    public void initManaBars() {

        Material black = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        black.setColor("Color", ColorRGBA.Black);
        Material blue = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blue.setColor("Color", ColorRGBA.Red);
        Material blue1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blue.setColor("Color", ColorRGBA.Red);
        Vector3f leftPosition = new Vector3f(10, settings.getHeight() - 40, 0);
        Vector3f rightPosition = new Vector3f(settings.getWidth() - 200 - 20, settings.getHeight() - 40, 0);
        mana1 = new ManaBox("mana1b", leftPosition, blue, black);
        mana2 = new ManaBox("mana2b", rightPosition, blue1, black);

        guiNode.attachChild(mana1.getManaNode());
        guiNode.attachChild(mana2.getManaNode());
        manas[0] = mana1;
        manas[1] = mana2;
    }

    public void initHudImgs() {

        p1PicSp = new Picture("HUD Picture 1");
        p1PicSp.setImage(assetManager, iconsSp[player1.getSuperPower().getType().ordinal() - 1], true);
        p1PicSp.setWidth(0);
        p1PicSp.setHeight(0);
        p1PicSp.setPosition(10, settings.getHeight() - 150);
        guiNode.attachChild(p1PicSp);

        p1Pic = new Picture("HUD Picture 1");
        p1Pic.setImage(assetManager, icons[player1.getSuperPower().getType().ordinal() - 1], true);
        p1Pic.setWidth(80);
        p1Pic.setHeight(80);
        p1Pic.setPosition(10, settings.getHeight() - 150);
        guiNode.attachChild(p1Pic);

        p2Pic = new Picture("HUD Picture 2");
        p2Pic.setImage(assetManager, icons[player2.getSuperPower().getType().ordinal() - 1], true);
        p2Pic.setWidth(80);
        p2Pic.setHeight(80);
        p2Pic.setPosition(settings.getWidth() - 80 - 20, settings.getHeight() - 150);
        guiNode.attachChild(p2Pic);

        p2PicSp = new Picture("HUD Picture 2");
        p2PicSp.setImage(assetManager, iconsSp[player2.getSuperPower().getType().ordinal() - 1], true);
        p2PicSp.setWidth(0);
        p2PicSp.setHeight(0);
        p2PicSp.setPosition(settings.getWidth() - 80 - 20, settings.getHeight() - 150);
        guiNode.attachChild(p2PicSp);

        pics[0] = p1Pic;
        pics[1] = p2Pic;

        spPics[0] = p1PicSp;
        spPics[1] = p2PicSp;
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
        bps[0] = bp1;
        bps[1] = bp2;
    }

    @Override
    public void simpleUpdate(float tpf) {
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



        if (counter > 0 && counter < 5) {
            startGame(counter);
            counter++;
        }

        if (inGame) {
            if (randomFlames == 0) {
                for (ParticleEmitter f : flames) {
                    if (generator.nextInt(5) == 1) {
                        f.emitAllParticles();
                        scenarioExplosion.get(generator.nextInt(6)).play();
                    }
                }
            }
            randomFlames = generator.nextInt(50);


            if (playerShoot) {
                bps[currentPlayer].increasePower();
            }


            if (players[currentPlayer].getBall() != null && players[currentPlayer].getBall().getControl(RigidBodyControl.class).getPhysicsLocation().getY() <= -20.0f) {
                explosion(players[currentPlayer].getBall().getLocalTranslation(), 0.5f);
                rootNode.detachChild(ball_geo);
                bulletAppState.getPhysicsSpace().remove(players[currentPlayer].getBall());
                players[currentPlayer].setBall(null);
                bps[currentPlayer].resetPower();

                changePlayer();
            }

            if(mana1.getCurrentMana() >= player1.getSuperPower().getManaCost())
                mana1.getGreenManaGeometry().getMaterial().setColor("Color", ColorRGBA.Blue);
                
            
            else
                
                mana1.getGreenManaGeometry().getMaterial().setColor("Color", ColorRGBA.Red);
                
            
            if(mana2.getCurrentMana() >= player2.getSuperPower().getManaCost())
                mana2.getGreenManaGeometry().getMaterial().setColor("Color", ColorRGBA.Blue);
            else
                mana2.getGreenManaGeometry().getMaterial().setColor("Color", ColorRGBA.Red);
    
            manas[1 - currentPlayer].regainMana();

            for (int cp = 0; cp < 2; cp++) {
                if (players[cp].isNeedChange()) {
                    rootNode.detachChild(players[cp].getPlayerNode());
                    bulletAppState.getPhysicsSpace().remove(players[cp].getPlayerControl());
                    players[cp].changeModel(1, -1);
                    rootNode.attachChild(players[cp].getPlayerNode());
                    bulletAppState.getPhysicsSpace().add(players[cp].getPlayerControl());
                    players[cp].setNeedChange(false);
                }
            }


        }

    }

    @Override
    public void simpleRender(RenderManager rm) {
    }

    public void collision(PhysicsCollisionEvent pce) {
        Spatial cannon = null;
        Player p = null;

        if (pce.getNodeA().getName().equals("cannon ball")) {
            cannon = pce.getNodeA();

        } else if (pce.getNodeB().getName().equals("cannon ball")) {
            cannon = pce.getNodeB();

        }
        if (rootNode.hasChild(cannon)) {

            if (cannon != null) {

                if (pce.getNodeA().getName().equals(player1.getPlayerName()) || pce.getNodeB().getName().equals(player1.getPlayerName())) {
                    p = player1;
                } else if (pce.getNodeA().getName().equals(player2.getPlayerName()) || pce.getNodeB().getName().equals(player2.getPlayerName())) {
                    p = player2;
                } else { //caiu no chao

                    explosion(cannon.getLocalTranslation(), 0.01f);


                    players[currentPlayer].setBall(null);
                    bps[currentPlayer].resetPower();

                    changePlayer();
                }

                if (p != null && cannon != null) {


                    if (rootNode.hasChild(cannon)) {

                        players[currentPlayer].setBall(null);
                        bps[currentPlayer].resetPower();

                        changePlayer();
                        if (!p.isImmune()) {
                            if (p.equals(player1)) {
                                hp1.loseLife(player2.getDamage());

                                if (hp1.getCurrentLife() <= 0) {
                                    death(player1);
                                } else {

                                    rootNode.detachChild(player1.getPlayerNode());
                                    bulletAppState.getPhysicsSpace().remove(player1.getPlayerControl());
                                    player1.changeModel(player2.getDamage(), 1);
                                    rootNode.attachChild(player1.getPlayerNode());
                                    bulletAppState.getPhysicsSpace().add(player1.getPlayerControl());

                                }
                            } else {
                                hp2.loseLife(player1.getDamage());

                                if (hp2.getCurrentLife() <= 0) {
                                    death(player2);
                                } else {
                                    rootNode.detachChild(player2.getPlayerNode());
                                    bulletAppState.getPhysicsSpace().remove(player2.getPlayerControl());
                                    player2.changeModel(player1.getDamage(), 1);
                                    rootNode.attachChild(player2.getPlayerNode());
                                    bulletAppState.getPhysicsSpace().add(player2.getPlayerControl());

                                }
                            }



                        }

                    }
                } 
            }
        }
        explosion(cannon.getLocalTranslation(), 0.5f);
        rootNode.detachChild(cannon);
        bulletAppState.getPhysicsSpace().remove(cannon.getControl(0));
    }

    private void initInfo() {
        info = new BitmapText(guiFont, false);
        info.setSize(guiFont.getCharSet().getRenderedSize());
        info.setColor(ColorRGBA.Black);
        info.setText("Player 1");
        info.setLocalTranslation(settings.getWidth() / 2 - info.getLineWidth() / 2, settings.getHeight() - info.getLineHeight() - 10, 0.0f);
        guiNode.attachChild(info);
    }

    private void death(Player p) {
        explosion(p.getLocalTranslation(), 5.0f);
        rootNode.detachChild(p.getPlayerNode());
        bulletAppState.getPhysicsSpace().remove(p.getPlayerControl());
        p.setAlive(false);
        inGame = false;

        guiNode.detachAllChildren();
        info = new BitmapText(guiFont, false);
        info.setSize(guiFont.getCharSet().getRenderedSize() * 4);
        info.setColor(ColorRGBA.White);

        if (players[0].isAlive()) {
            info.setText("PLAYER 1 WON!\n PRESS SPACE TO CONTINUE");
        } else {
            info.setText("PLAYER 2 WON!\n PRESS SPACE TO CONTINUE");
        }

        info.setLocalTranslation(settings.getWidth() / 2 - info.getLineWidth() / 2, settings.getHeight() / 2, 0.0f);

        winner = new AudioNode(assetManager, "winner.wav");
        winner.setLooping(true);
        winner.play();
        guiNode.attachChild(info);

        backgroundMusic.stop();

    }

    private void changePlayer() {
        currentPlayer = 1 - currentPlayer;
        info.setText("Player " + (currentPlayer + 1));



    }
}
