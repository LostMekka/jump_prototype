/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jump.level;

import jump.MyGame;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author LostMekka
 */
public class StaticTile extends Tile{

	private Image image;
	private boolean isBackground;
	private SpriteSheet sheet;
	private int imageX, imageY;

	public StaticTile(SpriteSheet sheet, int imageX, int imageY, boolean isBackground) {
		this.isBackground = isBackground;
		this.sheet = sheet;
		this.imageX = imageX;
		this.imageY = imageY;
	}

	public boolean isIsBackground() {
		return isBackground;
	}
	
	@Override
	public void draw(float x, float y) {
		sheet.renderInUse(Math.round(x), Math.round(y), imageX, imageY);
		//image.draw(x, y, MyGame.PIXEL_SIZE);
	}

	@Override
	public boolean isCollidable() {
		return !isBackground;
	}
	
}
