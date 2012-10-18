/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jump;

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.BigImage;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *
 * @author LostMekka
 */
public class AssetLoader {
	
	private AssetLoader(){}

	private static Image errorImage = null;
	private static HashMap<String, Sound> sounds = new HashMap<>();
	private static HashMap<String, Image> images = new HashMap<>();
	
	public static Image getImage(String name){
		if(images.containsKey(name)){
			return images.get(name);
		} else {
			Image i = null;
			try {
				i = new Image(name);
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
