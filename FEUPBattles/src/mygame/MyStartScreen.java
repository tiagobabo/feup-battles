package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 */
public class MyStartScreen extends AbstractAppState implements ScreenController {

  private Nifty nifty;
  private Application app;
  private Screen screen;
  private Main main;

  /** custom methods */
  public MyStartScreen() {
    /** You custom constructor, can accept arguments */
  }

    MyStartScreen(Main app) {
        this.main = app;
    }

  public void startGame(String nextScreen) {
    nifty.exit();
    main.startGame();
  }

  public void quitGame() {
    app.stop();
  }

  public String getPlayerName() {
    return System.getProperty("user.name");
  }

  /** Nifty GUI ScreenControl methods */
  public void bind(Nifty nifty, Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
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
}
