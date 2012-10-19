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

	public enum PlayerState{ idle, walk, jump, fall }
	
	private int currImageIndex = 0, msCounter = 0, maxMsCount, maxImageCount, jumpMsCounter = 0, maxJumpCounter = 1000;
	private Image currImage, jumpImage, fallImage;
	private Image[] walkImages, idleImages;
	private PlayerState state;
	private boolean looksToTheRight;
	
	public PlayerEntity(float x, float y) {
		super(x, y);
		looksToTheRight = true;
		jumpImage = AssetLoader.getImage("player_jump.png");
		fallImage = AssetLoader.getImage("player_fall.png");
		idleImages = new Image[4];
		idleImages[0] = AssetLoader.getImage("player_idle_01.png");
		idleImages[1] = AssetLoader.getImage("player_idle_02.png");
		idleImages[2] = AssetLoader.getImage("player_idle_03.png");
		idleImages[3] = AssetLoader.getImage("player_idle_04.png");
		walkImages = new Image[5];
		walkImages[0] = AssetLoader.getImage("player_walk_01.png");
		walkImages[1] = AssetLoader.getImage("player_walk_02.png");
		walkImages[2] = AssetLoader.getImage("player_walk_03.png");
		walkImages[3] = AssetLoader.getImage("player_walk_04.png");
		walkImages[4] = AssetLoader.getImage("player_walk_05.png");
		// TODO: init to idle
		setState(PlayerState.idle);
	}

	@Override
	public float getVxOnKeyPressed(int ms, boolean run) {
		float a = 0f;
		if((state == PlayerState.fall) || (state == PlayerState.jump)){
			a = 0.015f;
		} else {
			a = 0.3f;
		}
		if(run){
			a *= 1.5f;
		}
		return a * ms;
	}

	@Override
	public float getAyOnUpPressed() {
		switch(state){
			case idle: return 6f;
			case walk: return 6f;
			case jump: return 2.5f;
			default: return 0f;
		}
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

	public boolean looksToTheRight() {
		return looksToTheRight;
	}

	public void setDirection(boolean looksToTheRight) {
		this.looksToTheRight = looksToTheRight;
	}

	public PlayerState getState() {
		return state;
	}

	public void setState(PlayerState state) {
		this.state = state;
		currImageIndex = 0;
		msCounter = 0;
		switch(state){
			case idle:
				maxMsCount = 450;
				maxImageCount = 4;
				currImage = idleImages[0];
				break;
			case jump:
				maxMsCount = 100000;
				maxImageCount = 1;
				currImage = jumpImage;
				break;
			case fall:
				maxMsCount = 100000;
				maxImageCount = 1;
				currImage = fallImage;
				break;
			case walk:
				maxMsCount = 180;
				maxImageCount = 5;
				currImage = walkImages[0];
				break;
		}
	}

	@Override
	public void tick(int ms) {
		msCounter += ms;
		if(msCounter >= maxMsCount){
			int add = msCounter / maxMsCount;
			msCounter %= maxMsCount;
			currImageIndex = (currImageIndex + add) % maxImageCount;
			switch(state){
				case idle:
					currImage = idleImages[currImageIndex];
					break;
				case walk:
					currImage = walkImages[currImageIndex];
					break;
			}
		}
		if(state == PlayerState.jump){
			jumpMsCounter += ms;
			if(jumpMsCounter >= maxJumpCounter){
				jumpMsCounter = 0;
				state = PlayerState.fall;
			}
		}
	}

	@Override
	public void draw(float x, float y) {
		currImage.draw(x, y);
	}
	
}
