/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jump.states;

import jump.level.Entity;
import jump.level.Level;
import jump.level.PlayerEntity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author LostMekka
 */
public final class GameplayState extends BasicGameState {

	private Level level;
	private float camX, camY, tileSize;
	private PlayerEntity player;

	public GameplayState(Level level) {
		setLevel(level);
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
		player = level.getPlayer();
		camX = player.x;
		camY = player.y;
		tileSize = 30f;
	}
	
	@Override
	public int getID() {
		return 1;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
		for(int y=0; y<level.getHeight(); y++){
			for(int x=0; x<level.getWidth(); x++){
				level.getTile(x, y).draw(
						(x - camX) * Level.TILE_SIZE + gc.getWidth() / 2f, 
						(y - camY) * Level.TILE_SIZE + gc.getHeight() / 2f);
			}
		}
		for(Entity e : level){
			e.draw((e.x - camX) * Level.TILE_SIZE + gc.getWidth() / 2f, 
					(e.y - camY) * Level.TILE_SIZE + gc.getHeight() / 2f);
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
		Input in = gc.getInput();
		for(Entity e : level){
			if(e == player){
				e.tick(level, i,
						in.isKeyDown(Input.KEY_LEFT),
						in.isKeyDown(Input.KEY_RIGHT),
						in.isKeyDown(Input.KEY_SPACE),
						in.isKeyDown(Input.KEY_LCONTROL));
			} else {
				e.tick(level, i, false, false, false, false);
			}
		}
	}
	
}
