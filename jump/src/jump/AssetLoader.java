/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jump;

import java.util.HashMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author LostMekka
 */
public class AssetLoader {
	
	private AssetLoader(){}

	private static Image errorImage = null;
	private static SpriteSheet errorSpriteSheet = null;
	private static HashMap<String, Sound> sounds = new HashMap<>();
	private static HashMap<String, Image> images = new HashMap<>();
	private static HashMap<String, SpriteSheet> spriteSheets = new HashMap<>();
	
	public static Image getImage(String name, boolean flipped){
		if(flipped){
			String name2 = "__FLIPPED__" + name;
			if(images.containsKey(name2)){
				return images.get(name2);
			} else {
				Image i = getImage(name, false);
				i = i.getFlippedCopy(true, false);
				images.put(name2, i);
				return i;
			}
		} else {
			if(images.containsKey(name)){
				return images.get(name);
			} else {
				Image i;
				try {
					i = new Image(name);
					i.setFilter(Image.FILTER_NEAREST);
				} catch (Exception ex) {
					if(errorImage == null){
						try {
							errorImage = new Image("error.png");
						} catch (SlickException ex1) {}
					}
					return errorImage;
				}
				if(i != null){
					images.put(name, i);
				}
				return i;
			}
		}
	}
	
	public static SpriteSheet getSpriteSheet(String name, int tileWidth, int tileHeight){
		if(spriteSheets.containsKey(name)){
			return spriteSheets.get(name);
		} else {
			SpriteSheet s;
			try {
				s = new SpriteSheet(name, tileWidth, tileHeight);
			} catch (SlickException ex) {
				if(errorSpriteSheet == null){
					try {
						errorSpriteSheet = new SpriteSheet("error.png", 32, 32);
					} catch (SlickException ex1) {}
				}
				return errorSpriteSheet;
			}
			if(s != null){
				spriteSheets.put(name, s);
			}
			return s;
		}
	}
	
	public static Sound getSound(String name){
		if(sounds.containsKey(name)){
			return sounds.get(name);
		} else {
			Sound s = null;
			try {
				s = new Sound(name);
			} catch (SlickException ex) {}
			if(s != null){
				sounds.put(name, s);
			}
			return s;
		}
	}
	
}
