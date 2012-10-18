/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jump;

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

	public MyGame(String name) {
		super(name);
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		addState(new GameplayState(new Level("level001", true)));
	}
	
}
