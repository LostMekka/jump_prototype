/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jump.level;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import jump.AssetLoader;

/**
 *
 * @author LostMekka
 */
public class Level implements Serializable, Iterable<Entity> {
	
	public static final float GRAVITY = 30f;
	public static final int TILE_SIZE = 30;
	
	private Tile[] tiles;
	private LinkedList<Entity> entities;
	private int width, height;
	private PlayerEntity playerEntity;
	
	public Level(String name, boolean isImage){
		if(isImage){
			BufferedImage image;
			try {
				image = ImageIO.read(new File(name + ".png"));
			} catch (IOException ex) {
				throw new RuntimeException("cannot find image \"" + name + ".png\"!", ex);
			}
			Tile error1 = new StaticTile(AssetLoader.getImage("error.png", false), true);
			Tile back1 = new StaticTile(AssetLoader.getImage("back254.png", false), true);
			Tile front1 = new StaticTile(AssetLoader.getImage("front254.png", false), false);
			width = image.getWidth();
			height = image.getHeight();
			tiles = new Tile[width * height];
			entities = new LinkedList<>();
			for(int y=0; y<width; y++){
				for(int x=0; x<width; x++){
					int i = y * width + x;
					int col = image.getRGB(x, y);
					int fg = (col & 0x00FF0000) >> 16;
					int bg = (col & 0x0000FF00) >> 8;
					int en = col & 0x000000FF;
					tiles[i] = error1;
					if(fg == 255) tiles[i] = front1;
					if(bg == 255) tiles[i] = back1;
					if(en == 255){
						playerEntity = new PlayerEntity(x, y);
						entities.add(playerEntity);
					}
				}
			}
		} else {
			Level l = null;
			File f = new File(name + ".lvl");
			try {
				ObjectInput in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));
				l = (Level)in.readObject();
			} catch (Exception ex) {
				Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			}
			if(l == null){
				throw new RuntimeException("failed to load level!");
			}
			tiles = l.tiles;
			entities = l.entities;
			width = l.width;
			height = l.width;
			playerEntity = l.playerEntity;
		}
	}
	
	public void save(String name){
		File f = new File(name + ".lvl");
		try {
			ObjectOutput out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
			out.writeObject(this);
		} catch (Exception ex) {
			Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public PlayerEntity getPlayer(){
		return playerEntity;
	}

	@Override
	public Iterator<Entity> iterator() {
		return entities.iterator();
	}
	
	public Tile getTile(int x, int y){
		return tiles[y * width + x];
	}
	
	private boolean isCollidable(Entity e, int x, int y){
		if((x < 0) || (x >= width) || (y < 0) || (y >= height)) return true;
		return (getTile(x, y).isCollidable() && e.collidesOnForeground());
	}
	
	public void correctEntityPosition(Entity e){
		int ix = Math.round(e.x);
		int iy = Math.round(e.y);
		boolean hc, vc, dc;
		if(e.x < ix){
			hc = isCollidable(e, ix - 1, iy);
			if(e.y < iy){
				// top left case
			hc = isCollidable(e, ix - 1, iy);
				vc = isCollidable(e, ix, iy - 1);
				dc = isCollidable(e, ix - 1, iy - 1);
			} else {
				// bottom left case
				vc = isCollidable(e, ix, iy + 1);
				dc = isCollidable(e, ix - 1, iy + 1);
			}
		} else {
				hc = isCollidable(e, ix + 1, iy);
			if(e.y < iy){
				// top right case
				vc = isCollidable(e, ix, iy - 1);
				dc = isCollidable(e, ix + 1, iy - 1);
			} else {
				// bottom right case
				vc = isCollidable(e, ix, iy + 1);
				dc = isCollidable(e, ix + 1, iy + 1);
			}
		}
		if(!hc && !vc && dc){
			if(Math.abs(e.x - ix) > Math.abs(e.y - iy)){
				if(((e.y < iy) && (e.vy > 0f)) || ((e.y > iy) && (e.vy < 0f))){
					// jumped on an edge from below or fell on an edge from above
					// reset motion. if this is not the case, the motion must continue
					e.vy = 0f;
					e.ay = 0f;
				}
				e.y = iy;
			} else {
				e.x = ix;
			}
		}
		if(hc){
			e.x = ix;
			e.vx = 0f;
			e.ax = 0f;
		}
		if(vc){
			e.y = iy;
			e.vy = 0f;
			e.ay = 0f;
		}
	}
	
}
