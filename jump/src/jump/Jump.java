/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jump;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

/**
 *
 * @author LostMekka
 */
public class Jump {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {
			MyGame game = new MyGame("game");
			AppGameContainer c = new AppGameContainer(game, 800, 600, false);
			c.start();
		} catch (SlickException ex) {
			Logger.getLogger(Jump.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
