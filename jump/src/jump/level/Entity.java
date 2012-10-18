/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jump.level;

import java.io.Serializable;

/**
 *
 * @author LostMekka
 */
public abstract class Entity implements Serializable {
	
	protected float x, y, vx, vy;

	public Entity(float x, float y) {
		this.x = x;
		this.y = y;
		vx = 0f;
		vy = 0f;
	}
	
	public float getVx() {
		return vx;
	}

	public float getVy() {
		return vy;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public abstract void tick(int ms);
	public abstract void draw(float x, float y, float tileSize);
	
}
