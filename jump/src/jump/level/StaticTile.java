/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jump.level;

import org.newdawn.slick.Image;

/**
 *
 * @author LostMekka
 */
public class StaticTile extends Tile{

	private Image image;
	private boolean isBackground;

	public StaticTile(Image image, boolean isBackground) {
		this.image = image;
		this.isBackground = isBackground;
	}

	public Image getImage() {
		return image;
	}

	public boolean isIsBackground() {
		return isBackground;
	}
	
	@Override
	public void draw(float x, float y) {
		image.draw(x, y);
	}
	
}
