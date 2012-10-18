/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jump.level;

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.LinkedList;
import java.util.logging.Logger;
import jump.AssetLoader;

/**
 *
 * @author LostMekka
 */
public class Level implements Serializable {
	
	private Tile[] tiles;
	private LinkedList<Entity> entities;
	private int width, height;
	private PlayerEntity playerEntity;
	
	public Level(String name){
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
	}
	
	public Level(BufferedImage image){
		// parse image
		Tile error1 = new StaticTile(AssetLoader.getImage("error1.png"), true);
		Tile back1 = new StaticTile(AssetLoader.getImage("back1.png"), true);
		Tile front1 = new StaticTile(AssetLoader.getImage("front1.png"), false);
		width = image.getWidth();
		height = image.getHeight();
		tiles = new Tile[width * height];
		entities = new LinkedList<>();
		for(int y=0; y<width; y++){
			for(int x=0; x<width; x++){
				int i = y * width + x;
				int col = image.getRGB(x, y);
				int fr = col & 0x00FF0000 >> 16;
				int bg = col & 0x0000FF00 >> 8;
				int en = col & 0x000000FF;
				tiles[i] = error1;
				if(fr == 1) tiles[i] = front1;
				if(bg == 1) tiles[i] = back1;
				if(en == 1) entities.add(new PlayerEntity(x, y));
			}
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
	
	public PlayerEntity getPlayer(){
		return playerEntity;
	}
	
}
