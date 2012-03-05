package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;

public class Main extends SimpleApplication {

    private BulletAppState bulletAppState;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    float velocity = 0.01f;
    Geometry player1;
    Geometry player2;

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
        player1 = new Geometry("Player1", b);
        player1.setLocalTranslation(p1_pos);
        player1.setMaterial(mat);
        rootNode.attachChild(player1);

        //Player 2
        Vector3f p2_pos = new Vector3f(30.0f, 1f, 0f);
        player2 = new Geometry("Player2", b);
        player2.setLocalTranslation(p2_pos);
        player2.setMaterial(mat);
        rootNode.attachChild(player2);

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
        RigidBodyControl player1_rb = new RigidBodyControl(2.0f);
        RigidBodyControl player2_rb = new RigidBodyControl(0.0f);
        RigidBodyControl plat1_rb = new RigidBodyControl(2.0f);
        RigidBodyControl plat2_rb = new RigidBodyControl(0.0f);

        //Associacao da fisica
        player1.addControl(player1_rb);
        player2.addControl(plat1_rb);
        plat1.addControl(player2_rb);
        plat2.addControl(plat2_rb);


        player1_rb.setKinematic(true);
        player2_rb.setKinematic(true);
        plat1_rb.setKinematic(true);
        plat2_rb.setKinematic(true);

        bulletAppState.getPhysicsSpace().add(player1_rb);
        bulletAppState.getPhysicsSpace().add(player2_rb);
        bulletAppState.getPhysicsSpace().add(plat1_rb);
        bulletAppState.getPhysicsSpace().add(plat2_rb);

        initKeys();
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
    long player1_power = 0;
    long player2_power = 0;
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("P1_Shoot") && !keyPressed) {
                player1_power = System.currentTimeMillis() - player1_power;
                makeBall(player1_power, player1, 1);
            } else if (name.equals("P1_Shoot")) {
                player1_power = System.currentTimeMillis();
            } else if (name.equals("P2_Shoot") && !keyPressed) {
                player2_power = System.currentTimeMillis() - player2_power;
                makeBall(player2_power, player2, -1);
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

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }
}
