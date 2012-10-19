/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jump.level;

import java.io.Serializable;
import jump.MessageType;

/**
 *
 * @author LostMekka
 */
public abstract class Tile implements Serializable {
	
	public void receiveMessage(MessageType type){}
	public void tick(int ms){}
	public abstract void draw(float x, float y);
	public abstract boolean isCollidable();
	
}
