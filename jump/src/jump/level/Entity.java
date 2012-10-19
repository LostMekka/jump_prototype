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
	
	protected float x, y, vx, vy, ax, ay;

	public Entity(float x, float y, float vx, float vy, float ax, float ay) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.ax = ax;
		this.ay = ay;
	}

	public Entity(float x, float y, float vx, float vy) {
		this(x, y, vx, vy, 0f, 0f);
	}

	public Entity(float x, float y) {
		this(x, y, 0f, 0f, 0f, 0f);
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

	public float getAx() {
		return ax;
	}

	public void setAx(float ax) {
		this.ax = ax;
	}

	public float getAy() {
		return ay;
	}

	public void setAy(float ay) {
		this.ay = ay;
	}

	public boolean movementTick(int ms){
		if(isMovable()){
			vx += ax * ms;		
			if(usesGravity()){
				vy += (ay - Level.GRAVITY) * ms;
			} else {
				vy += ay * ms;
			}
			x += vx * ms;
			y -= vy * ms;
			return true;
		} else {
			return false;
		}
	}
	
	public void leftPressed(int ms, boolean run){
		vx = -getVxOnKeyPressed(ms, run);
	}
	
	public void rigthPressed(int ms, boolean run){
		vx = getVxOnKeyPressed(ms, run);
	}
	
	public void upPressed(){
		ay = getAyOnUpPressed();
	}
	
	public abstract void tick(int ms);
	public abstract void draw(float x, float y);
	public abstract boolean isMovable();
	public abstract boolean usesGravity();
	public abstract boolean collidesOnForeground();
	public abstract float getVxOnKeyPressed(int ms, boolean run);
	public abstract float getAyOnUpPressed();
	
}
