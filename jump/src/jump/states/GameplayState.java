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
	private Entity focusedEntity;
	private GameContainer gc;

	public GameplayState(Level level) {
		this.level = level;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
		player = level.getPlayer();
		focusedEntity = player;
		focusCameraOnTile(Math.round(player.x), Math.round(player.y));
		tileSize = 30f;
	}
	
	@Override
	public int getID() {
		return 1;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.gc = gc;
		setLevel(level);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
		for(int y=0; y<level.getHeight(); y++){
			for(int x=0; x<level.getWidth(); x++){
				level.getTile(x, y).draw(
						Math.round((x * Level.TILE_SIZE - camX + gc.getWidth() / 2f)), 
						Math.round((y * Level.TILE_SIZE - camY + gc.getHeight() / 2f)));
			}
		}
		for(Entity e : level){
			e.draw(Math.round((e.x * Level.TILE_SIZE - camX + gc.getWidth() / 2f)), 
					Math.round((e.y * Level.TILE_SIZE - camY + gc.getHeight() / 2f)));
		}
	}
	
	private void updateCamera(){
		float rx = (float)gc.getWidth() * 0.3f * 0.5f;
		float ry = (float)gc.getHeight() * 0.3f * 0.5f;
		float fx = (focusedEntity.x + 0.5f) * Level.TILE_SIZE;
		float fy = (focusedEntity.y + 0.5f) * Level.TILE_SIZE;
		if(fx < camX - rx) camX = fx + rx;
		if(fx > camX + rx) camX = fx - rx;
		if(fy < camY - ry) camY = fy + ry;
		if(fy > camY + ry) camY = fy - ry;
		applyLevelBorderToCamera();
	}
	
	private void focusCameraOnTile(int x, int y){
		camX = ((float)x + 0.5f) * (float)Level.TILE_SIZE;
		camY = ((float)y + 0.5f) * (float)Level.TILE_SIZE;
		applyLevelBorderToCamera();
	}
	
	private void applyLevelBorderToCamera(){
		float sx = gc.getWidth();
		float sy = gc.getHeight();
		float lx = level.getWidth() * Level.TILE_SIZE;
		float ly = level.getHeight() * Level.TILE_SIZE;
		if(lx < sx){
			camX = lx / 2f;
		} else {
			if(camX > lx - sx / 2f) camX = lx - sx / 2f;
			if(camX < sx / 2f) camX = sx / 2f;
		}
		if(ly < sy){
			camY = ly / 2f;
		} else {
			if(camY > ly - sy / 2f) camY = ly - sy / 2f;
			if(camY < sy / 2f) camY = sy / 2f;
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
		updateCamera();
	}
	
}
