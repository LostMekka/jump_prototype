/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jump.level;

import java.io.Serializable;
import java.util.Arrays;
import jump.AssetLoader;
import jump.FlippableSpriteSheet;
import jump.MyGame;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

/**
 *
 * @author LostMekka
 */
public abstract class Entity implements Serializable {
	
	// ATTENTION! manipulate state ONLY WITH setState(state) !!!!
	
	public static FlippableSpriteSheet ffs = null;

	public enum EntityState{ idle, walk, jump, fall }
	public enum EntityInputMode{ noneFixed, noneMovable, player, computer }
	
	public float x, y, vx, vy, ax, ay;
	public EntityState state;
	
	private boolean[] lastComputerInput = new boolean[8];
	private Animation currAnimation;
	private int jumpMsCounter = 0;
	private boolean looksToTheLeft;
	

	public Entity(boolean looksToTheLeft, float x, float y, float vx, float vy, float ax, float ay) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.ax = ax;
		this.ay = ay;
		this.looksToTheLeft = looksToTheLeft;
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

	public final void setState(EntityState state){
		if(this.state != state){
			currAnimation = getAnimation(state, looksToTheLeft);
			currAnimation.restart();
		}
		this.state = state;
	}

	public void setInputByAI(
			boolean leftPressed, boolean rightPressed, boolean upPressed, boolean downPressed, 
			boolean jumpPressed, boolean runPressed, boolean attack1Pressed, boolean attack2Pressed){
		lastComputerInput[0] = leftPressed;
		lastComputerInput[1] = rightPressed;
		lastComputerInput[2] = upPressed;
		lastComputerInput[3] = downPressed;
		lastComputerInput[4] = jumpPressed;
		lastComputerInput[5] = runPressed;
		lastComputerInput[6] = attack1Pressed;
		lastComputerInput[7] = attack2Pressed;
	}

	public void tick(Level level, int ms, 
			boolean leftPressed, boolean rightPressed, boolean upPressed, boolean downPressed, 
			boolean jumpPressed, boolean runPressed, boolean attack1Pressed, boolean attack2Pressed){
		currAnimation.update(ms);
		// movement
		if(getInputMode() == EntityInputMode.computer || getInputMode() == EntityInputMode.player){
			// inputs
			if(getInputMode() == EntityInputMode.computer){
				// set input to last specified by ai
				leftPressed = lastComputerInput[0];
				rightPressed = lastComputerInput[1];
				upPressed = lastComputerInput[2];
				downPressed = lastComputerInput[3];
				jumpPressed = lastComputerInput[4];
				runPressed = lastComputerInput[5];
				attack1Pressed = lastComputerInput[6];
				attack2Pressed = lastComputerInput[7];
				Arrays.fill(lastComputerInput, false);
			}
			if(leftPressed) vx = -getVxOnKeyPressed(runPressed);
			if(rightPressed) vx = getVxOnKeyPressed(runPressed);
			if(leftPressed == rightPressed) vx = 0;
			if(jumpPressed && ((state == EntityState.walk) || (state == EntityState.idle))){
				ay = getAyOnUpPressed();
				vy = getInitialJunpVel();
			}
			// calc pos
			vx += ax * (float)ms / 1000f;		
			if(usesGravity()){
				vy += (ay - MyGame.GRAVITY) * (float)ms / 1000f;
			} else {
				vy += ay * (float)ms / 1000f;
			}
			x += vx * (float)ms / 1000f;
			y -= vy * (float)ms / 1000f;
			// do collisions
			level.correctEntityPosition(this);
			// vertical handling
			if((vy == 0f) && (state == EntityState.fall)){
				setState(EntityState.idle);
				AssetLoader.getSound("land.wav").play();
			}
			if((vy > 0f) && ((state == EntityState.idle) || (state == EntityState.walk))){
				setState(EntityState.jump);
				AssetLoader.getSound("jump.wav").play();
			}
			if(state == EntityState.jump){
				jumpMsCounter += ms;
				if((jumpMsCounter >= getmaxJumpTime()) || (vy < 0f) || !jumpPressed){
					ay = 0f;
					jumpMsCounter = 0;
					setState(EntityState.fall);
				}
			} else if((vy < 0f) && (state != EntityState.fall)) {
				setState(EntityState.fall);
			}
			// horizontal handling
			if(vx > 0f) setDirectionToLeft(false);
			if(vx < 0f) setDirectionToLeft(true);
			if((vx != 0f) && (state == EntityState.idle)) setState(EntityState.walk);
			if((vx == 0f) && (state == EntityState.walk)) setState(EntityState.idle);		
		}
		// internal tick
		tickInternal(level, ms);
	}
	
	public void draw(float x, float y){
		currAnimation.renderInUse(Math.round(x), Math.round(y));
		//getCurrentFrame().draw(x, y, MyGame.PIXEL_SIZE);
		drawInternal(x, y);
	}
	
	public FlippableSpriteSheet getFlippableSpriteSheet(){ return null; } 
	
	protected abstract void tickInternal(Level level, int ms);
	public abstract void drawInternal(float x, float y);
	public abstract EntityInputMode getInputMode();
	public abstract boolean usesGravity();
	public abstract boolean collidesOnForeground();
	public abstract float getVxOnKeyPressed(boolean run);
	public abstract float getAyOnUpPressed();
	public abstract float getInitialJunpVel();
	public abstract int getmaxJumpTime();
	public abstract Animation getAnimation(EntityState state, boolean facesLeft);
	
}
