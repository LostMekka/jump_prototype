/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jump.states;

import jump.FlippableSpriteSheet;
import jump.MyGame;
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
	private float camX, camY;
	private PlayerEntity player;
	private Entity focusedEntity;
	private GameContainer gc;

	public GameplayState() {
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
		player = level.getPlayer();
		focusedEntity = player;
		focusCameraOnTile(Math.round(player.x), Math.round(player.y));
	}
	
	@Override
	public int getID() {
		return 1;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		this.gc = gc;
		// load textures
		PlayerEntity.ffs = new FlippableSpriteSheet("player.png", 32, 32, true, false);
		// init state stuff
		setLevel(new Level("level001", true));
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.scale(MyGame.PIXEL_SIZE, MyGame.PIXEL_SIZE);
		// render tiles
		level.getSpriteSheet().startUse();
		for(int y=0; y<level.getHeight(); y++){
			for(int x=0; x<level.getWidth(); x++){
				level.getTile(x, y).draw(
						Math.round((x - camX) * MyGame.TILE_SIZE + gc.getWidth() / 2f / MyGame.PIXEL_SIZE), 
						Math.round((y - camY) * MyGame.TILE_SIZE + gc.getHeight() / 2f / MyGame.PIXEL_SIZE));
			}
		}
		level.getSpriteSheet().endUse();
		//render entities
		PlayerEntity.ffs.startUse();
		for(Entity e : level){
			e.draw(Math.round((e.x - camX) * MyGame.TILE_SIZE + gc.getWidth() / 2f / MyGame.PIXEL_SIZE), 
					Math.round((e.y - camY) * MyGame.TILE_SIZE + gc.getHeight() / 2f / MyGame.PIXEL_SIZE));
		}
		PlayerEntity.ffs.endUse();
	}
	
	private void updateCamera(){
		float rx = (float)gc.getWidth() / MyGame.TILE_SIZE / MyGame.PIXEL_SIZE / 2f * 0.3f;
		float ry = (float)gc.getHeight() / MyGame.TILE_SIZE / MyGame.PIXEL_SIZE / 2f * 0.3f;
		float fx = focusedEntity.x + 0.5f;
		float fy = focusedEntity.y + 0.5f;
		if(fx < camX - rx) camX = fx + rx;
		if(fx > camX + rx) camX = fx - rx;
		if(fy < camY - ry) camY = fy + ry;
		if(fy > camY + ry) camY = fy - ry;
		applyLevelBorderToCamera();
	}
	
	private void focusCameraOnTile(int x, int y){
		camX = (float)x + 0.5f;
		camY = (float)y + 0.5f;
		applyLevelBorderToCamera();
	}
	
	private void applyLevelBorderToCamera(){
		float sx = (float)gc.getWidth() / (float)MyGame.TILE_SIZE / (float)MyGame.PIXEL_SIZE;
		float sy = (float)gc.getHeight() / (float)MyGame.TILE_SIZE / (float)MyGame.PIXEL_SIZE;
		float lx = level.getWidth();
		float ly = level.getHeight();
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
		if(in.isKeyDown(Input.KEY_ESCAPE)) gc.exit();
		for(Entity e : level){
			if(e == player){
				e.tick(level, i,
						in.isKeyDown(Input.KEY_LEFT),
						in.isKeyDown(Input.KEY_RIGHT),
						in.isKeyDown(Input.KEY_UP),
						in.isKeyDown(Input.KEY_DOWN),
						in.isKeyDown(Input.KEY_C),
						in.isKeyDown(Input.KEY_X),
						in.isKeyDown(Input.KEY_V),
						in.isKeyDown(Input.KEY_B));
			} else {
				e.tick(level, i, false, false, false, false, false, false, false, false);
			}
		}
		updateCamera();
	}
	
}
