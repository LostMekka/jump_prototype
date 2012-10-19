/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jump.level;

import java.io.Serializable;
import org.newdawn.slick.Image;

/**
 *
 * @author LostMekka
 */
public abstract class Entity implements Serializable {
	
	// ATTENTION! manipulate state ONLY WITH setState(state) !!!!
	
	public enum EntityState{ idle, walk, jump, fall }
	
	public float x, y, vx, vy, ax, ay;
	private Image[] currImages;
	private int currImageIndex = 0, msCounter = 0, maxMsCount, jumpMsCounter = 0;
	public EntityState state;
	private boolean looksToTheLeft;

	public Entity(boolean looksToTheLeft, float x, float y, float vx, float vy, float ax, float ay) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.ax = ax;
		this.ay = ay;
		this.looksToTheLeft = looksToTheLeft;
		setState(EntityState.idle);
	}

	public Entity(boolean looksToTheLeft, float x, float y, float vx, float vy) {
		this(looksToTheLeft, x, y, vx, vy, 0f, 0f);
	}

	public Entity(boolean looksToTheLeft, float x, float y) {
		this(looksToTheLeft, x, y, 0f, 0f, 0f, 0f);
	}
	
	public boolean looksToTheLeft() {
		return looksToTheLeft;
	}

	public void setDirectionToLeft(boolean looksToTheLeft) {
		if(this.looksToTheLeft != looksToTheLeft){
			this.looksToTheLeft = looksToTheLeft;
			setState(state);
		} else {
			this.looksToTheLeft = looksToTheLeft;
		}
	}

	public EntityState getState() {
		return state;
	}

	public void setState(EntityState state){
		if(this.state != state) currImageIndex = 0;
		this.state = state;
		currImages = getImages(state, looksToTheLeft);
		maxMsCount = getImageDuration(state);
	}

	public void tick(Level level, int ms, boolean leftPressed, boolean rightPressed, boolean upPressed, boolean runPressed){
		// counters
		msCounter += ms;
		if(msCounter >= maxMsCount){
			int add = msCounter / maxMsCount;
			msCounter %= maxMsCount;
			currImageIndex = (currImageIndex + add) % currImages.length;
		}
		// movement
		if(isMovable()){
			if(leftPressed) vx = -getVxOnKeyPressed(runPressed);
			if(rightPressed) vx = getVxOnKeyPressed(runPressed);
			if(upPressed) ay = getAyOnUpPressed();
			// TODO: handle pressed == false
			vx += ax * (float)ms / 1000f;		
			if(usesGravity()){
				vy += (ay - Level.GRAVITY) * (float)ms / 1000f;
			} else {
				vy += ay * (float)ms / 1000f;
			}
			x += vx * (float)ms / 1000f;
			y -= vy * (float)ms / 1000f;
			level.correctEntityPosition(this);
		}
		// direction
		if(vx > 0f) setDirectionToLeft(false);
		if(vx < 0f) setDirectionToLeft(true);
		// states
		if(state == EntityState.jump){
			jumpMsCounter += ms;
			if((jumpMsCounter >= getmaxJumpTime()) || (vy < 0f)){
				jumpMsCounter = 0;
				setState(EntityState.fall);
			}
		}
		if((vy > 0f) && (state != EntityState.jump)){
			jumpMsCounter = 0;
			setState(EntityState.jump);
		}
		if((vy < 0f) && (state != EntityState.fall)) setState(EntityState.fall);
		if((vx != 0f) && (state == EntityState.idle)) setState(EntityState.walk);
		if((vx == 0f) && (state == EntityState.walk)) setState(EntityState.idle);		
		// internal tick
		tickInternal(level, ms, leftPressed, rightPressed, upPressed, runPressed);
	}
	
	public void draw(float x, float y){
		currImages[currImageIndex].draw(x, y);
		drawInternal(x, y);
	}
	
	protected abstract void tickInternal(Level level, int ms, boolean leftPressed, boolean rightPressed, boolean upPressed, boolean runPressed);
	public abstract void drawInternal(float x, float y);
	public abstract boolean isMovable();
	public abstract boolean usesGravity();
	public abstract boolean collidesOnForeground();
	public abstract float getVxOnKeyPressed(boolean run);
	public abstract float getAyOnUpPressed();
	public abstract int getmaxJumpTime();
	public abstract Image[] getImages(EntityState state, boolean facesLeft);
	public abstract int getImageDuration(EntityState state);
	
}
