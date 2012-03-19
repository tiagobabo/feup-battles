/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ZePedro
 */
public class Keys {
    
    private Map<String,Integer> keys;
    
    public Keys(int lKey, int rKey, int fKey,int sp){
        keys = new HashMap<String,Integer>();
            keys.put("Left", lKey);
            keys.put("Right", rKey);
            keys.put("Fire", fKey);
            keys.put("SuperPower", sp);
       
    } 
    public int getLeftKey(){
        return getKeys().get("Left");
    }
    public int getRightKey(){
        return getKeys().get("Right");
    }
    public int getFireKey(){
        return getKeys().get("Fire");
    }
     public int getSuperPowerKey(){
        return getKeys().get("SuperPower");
    }

    /**
     * @return the keys
     */
    public Map<String,Integer> getKeys() {
        return keys;
    }

    /**
     * @param keys the keys to set
     */
    public void setKeys(Map<String,Integer> keys) {
        this.keys = keys;
    }
    
}
