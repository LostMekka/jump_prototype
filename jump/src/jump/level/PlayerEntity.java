/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jump.level;

import jump.AssetLoader;
import org.newdawn.slick.Image;

/**
 *
 * @author LostMekka
 */
public final class PlayerEntity extends Entity {

	public PlayerEntity(float x, float y) {
		super(false, x, y);
	}

	@Override
	public float getVxOnKeyPressed(boolean run) {
		float v;
		if((state == EntityState.fall) || (state == EntityState.jump)){
			v = 1f;
		} else {
			v = 2f;
		}
		if(run){
			v *= 1.5f;
		}
		return v;
	}

	@Override
	public float getAyOnUpPressed() {
		switch(state){
			case idle: return -3f;
			case walk: return -3f;
			case jump: return -Level.GRAVITY * 0.9f;
			default: return 0f;
		}
	}

	@Override
	public int getmaxJumpTime() {
		return 3000;
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
	public Image[] getImages(EntityState state, boolean facesLeft) {
		Image[] ans;
		switch(state){
			case idle:
				ans = new Image[4];
				ans[0] = AssetLoader.getImage("player_idle_01.png", facesLeft);
				ans[1] = AssetLoader.getImage("player_idle_02.png", facesLeft);
				ans[2] = AssetLoader.getImage("player_idle_03.png", facesLeft);
				ans[3] = AssetLoader.getImage("player_idle_04.png", facesLeft);
				break;
			case jump:
				ans = new Image[1];
				ans[0] = AssetLoader.getImage("player_jump.png", facesLeft);
				break;
			case fall:
				ans = new Image[1];
				ans[0] = AssetLoader.getImage("player_fall.png", facesLeft);
				break;
			case walk:
				ans = new Image[5];
				ans[0] = AssetLoader.getImage("player_walk_01.png", facesLeft);
				ans[1] = AssetLoader.getImage("player_walk_02.png", facesLeft);
				ans[2] = AssetLoader.getImage("player_walk_03.png", facesLeft);
				ans[3] = AssetLoader.getImage("player_walk_04.png", facesLeft);
				ans[4] = AssetLoader.getImage("player_walk_05.png", facesLeft);
				break;
			default:
				ans = new Image[1];
				ans[0] = AssetLoader.getImage("error.png", false);
				break;
		}
		return ans;
	}

	@Override
	public int getImageDuration(EntityState state) {
		switch(state){
			case idle: return 450;
			case walk: return 180;
			default: return 10000;
		}
	}

	@Override
	public void tickInternal(Level level, int ms, boolean leftPressed, boolean rightPressed, boolean upPressed, boolean runPressed) {
	}

	@Override
	public void drawInternal(float x, float y) {}

}
