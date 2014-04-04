/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jump;

import java.util.Random;
import jump.level.Level;
import jump.states.GameplayState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author LostMekka
 */
public class MyGame extends StateBasedGame {

	public static final int PIXEL_SIZE = 3;
	public static final int TILE_SIZE = 32;
	public static final float GRAVITY = 30f;
	public static final Random random = new Random();
		
	public MyGame(String name) {
		super(name);
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		addState(new GameplayState());
	}
	
}
