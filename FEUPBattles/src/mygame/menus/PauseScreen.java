package mygame.menus;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.Main;

/**
 *
 */
public class PauseScreen extends AbstractAppState implements ScreenController {

  private Nifty nifty;
  private Application app;
  private Screen screen;
  private Main main;

  public PauseScreen(Main app) {
        this.main = app;
    }

  /** Nifty GUI ScreenControl methods */
  public void bind(Nifty nifty, Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
  }

  public void resumeGame() {
    this.main.resumeGame();
  }
  
  public void onStartScreen() {
  }

  public void onEndScreen() {
  }
  
  public void quitGame() {
    main.stop();
    app.stop();
    System.exit(1);
  }
  
  public void endGame() {
   //   TODO restart do jogo
   //   main.endGame();
  }


  /** jME3 AppState methods */
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    this.app = app;
  }

  @Override
  public void update(float tpf) {

  }
}
