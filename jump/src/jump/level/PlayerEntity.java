/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jump.level;

import jump.AssetLoader;
import jump.FlippableSpriteSheet;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author LostMekka
 */
public final class PlayerEntity extends Entity {

	private Animation idleAniL = null, idleAniR = null, 
			walkAniL = null, walkAniR = null, 
			jumpAniL = null, jumpAniR = null, 
			fallAniL = null, fallAniR = null;
	
	public PlayerEntity(float x, float y) {
		super(false, x, y);
		idleAniL = ffs.createAnimation(new int[]{0,1,2,3}, new int[]{0,0,0,0}, true, false, 450);
		idleAniR = ffs.createAnimation(new int[]{0,1,2,3}, new int[]{0,0,0,0}, false, false, 450);
		walkAniL = ffs.createAnimation(new int[]{0,1,2,3,4}, new int[]{1,1,1,1,1}, true, false, 180);
		walkAniR = ffs.createAnimation(new int[]{0,1,2,3,4}, new int[]{1,1,1,1,1}, false, false, 180);
		jumpAniL = ffs.createAnimation(new int[]{7}, new int[]{0}, true, false, 100000);
		jumpAniR = ffs.createAnimation(new int[]{7}, new int[]{0}, false, false, 100000);
		fallAniL = ffs.createAnimation(new int[]{7}, new int[]{1}, true, false, 100000);
		fallAniR = ffs.createAnimation(new int[]{7}, new int[]{1}, false, false, 100000);
		setState(EntityState.idle);
	}

	@Override
	public FlippableSpriteSheet getFlippableSpriteSheet() {
		return ffs;
	}

	@Override
	public float getVxOnKeyPressed(boolean run) {
		float v;
		if((state == EntityState.fall) || (state == EntityState.jump)){
			v = 3f;
		} else {
			v = 4f;
		}
		if(run){
			v *= 1.5f;
		}
		return v;
	}

	@Override
	public float getAyOnUpPressed() {
		return Level.GRAVITY * 0.7f;
	}

	@Override
	public float getInitialJunpVel() {
		return 6.5f;
	}

	@Override
	public int getmaxJumpTime() {
		return 500;
	}
	
	@Override
	public boolean isMovable() {
		return true;
	}

	@Override
	public boolean usesGravity() {
		return true;
	}

	@Override
	public boolean collidesOnForeground() {
		return true;
	}

	@Override
	public Animation getAnimation(EntityState state, boolean facesLeft) {
		if(facesLeft){
			switch(state){
				case idle: return idleAniL;
				case jump: return jumpAniL;
				case fall: return fallAniL;
				case walk: return walkAniL;
			}
		} else {
			switch(state){
				case idle: return idleAniR;
				case jump: return jumpAniR;
				case fall: return fallAniR;
				case walk: return walkAniR;
			}
		}
		throw new RuntimeException("No animation found for entity state!");
	}

	@Override
	public void tickInternal(Level level, int ms, boolean leftPressed, boolean rightPressed, boolean upPressed, boolean runPressed) {
	}

	@Override
	public void drawInternal(float x, float y) {}

}
