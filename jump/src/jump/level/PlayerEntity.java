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

	public enum PlayerState{ idle, walk, duck, jump, fall }
	
	private int currImageIndex = 0, msCounter = 0, maxMsCount, maxImageCount;
	private Image currImage, duckImage, jumpImage, fallImage;
	private Image[] walkImages, idleImages;
	private PlayerState state;
	
	public PlayerEntity(float x, float y) {
		super(x, y);
		// TODO: init the other images
		duckImage = AssetLoader.getImage("player_duck.png");
		jumpImage = AssetLoader.getImage("player_jump.png");
		fallImage = AssetLoader.getImage("player_fall.png");
		idleImages = new Image[3];
		idleImages[0] = AssetLoader.getImage("player_idle_1.png");
		idleImages[1] = AssetLoader.getImage("player_idle_2.png");
		idleImages[2] = AssetLoader.getImage("player_idle_3.png");
		walkImages = new Image[5];
		walkImages[0] = AssetLoader.getImage("player_walk_1.png");
		walkImages[1] = AssetLoader.getImage("player_walk_2.png");
		walkImages[2] = AssetLoader.getImage("player_walk_3.png");
		walkImages[3] = AssetLoader.getImage("player_walk_4.png");
		walkImages[4] = AssetLoader.getImage("player_walk_5.png");
		// TODO: init to idle
		setState(PlayerState.walk);
	}

	public PlayerState getState() {
		return state;
	}

	public void setState(PlayerState state) {
		this.state = state;
		currImageIndex = 0;
		msCounter = 0;
		switch(state){
			case duck:
				maxMsCount = 100000;
				maxImageCount = 1;
				currImage = duckImage;
				break;
			case idle:
				maxMsCount = 180;
				maxImageCount = 1;
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
	}

	@Override
	public void draw(float x, float y, float tileSize) {
		currImage.draw(x, y, tileSize, tileSize);
	}
	
}
