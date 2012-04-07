package mygame.menus;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.ImageSelect;
import de.lessvoid.nifty.controls.ImageSelectSelectionChangedEvent;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.Main;
import mygame.superPower.ESuperPower;

/**
 *
 */
public class MyStartScreen extends AbstractAppState implements ScreenController {

  private Nifty nifty;
  private Application app;
  private Screen screen;
  private Main main;
  private String[] descs = {"inf_desc.png","civil_desc.png","chem_desc.png","electro_desc.png","bio_desc.png","mec_desc.png","metal_desc.png"};
  private NiftyImage[] descImgs ;
  public MyStartScreen(Main app) {
        this.main = app;
       
        descImgs  = new NiftyImage[descs.length];
                
        
    }

  public void startGame() {
     
    int index = nifty.getScreen("start_game").findNiftyControl("#player1", ImageSelect.class).getSelectedImageIndex();
    main.p1Selected= ESuperPower.values()[index+1];
    index = nifty.getScreen("start_game").findNiftyControl("#player2", ImageSelect.class).getSelectedImageIndex();
    main.p2Selected=ESuperPower.values()[index+1];
    main.counter = 1;
   
    
  }
  
  public void go(String screen) { 
    nifty.gotoScreen(screen);
  }

  public void quitGame() {
    main.stop();
    app.stop();
  }

  public String getPlayerName() {
    return System.getProperty("user.name");
  }

  /** Nifty GUI ScreenControl methods */
  public void bind(Nifty nifty, Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
    int i =0;
    for(String s: descs){
           descImgs[i++] =  nifty.getRenderEngine().createImage(s, false);
        }
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  /** jME3 AppState methods */
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    this.app = app;
  }

  @Override
  public void update(float tpf) {
    if (screen.getScreenId().equals("hud")) {
      Element niftyElement = nifty.getCurrentScreen().findElementByName("score");
      niftyElement.getRenderer(TextRenderer.class).setText(tpf*1000/10 + ""); // fake score
    }
  }
  @NiftyEventSubscriber(pattern="#player.?")
  public void stuffChanged(String id, ImageSelectSelectionChangedEvent event ){

      Element element = nifty.getScreen("start_game").findElementByName(id+"_img");
      element.getRenderer(ImageRenderer.class).setImage(descImgs[event.getSelectedIndex()]);
  }
}
