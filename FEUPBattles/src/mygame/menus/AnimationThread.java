/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.menus;

/**
 *
 * @author felipeschmitt
 */
public class AnimationThread extends Thread{
    MyStartScreen mss;
    
    public AnimationThread(MyStartScreen m){
        mss = m;
    }
    
    
    
    @Override
    public void run(){
        try {
                Thread.sleep(5000);
                mss.go("screen1");
                Thread.sleep(2000);
                mss.go("screen2");
                Thread.sleep(2000);
                mss.go("screen3");
                Thread.sleep(2000);
                mss.go("screen4");
                Thread.sleep(2000);
                mss.go("screen5");
                Thread.sleep(2000);
                mss.go("startScreen");
                } catch (InterruptedException ex) {
            }
    }
    
}
