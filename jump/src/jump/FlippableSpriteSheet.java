package jump;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * A big sprite sheet, that contains flipped versions of the original.
 * That way you can use startUse(), endUse() and renderInUse(...)
 * without changing to a flipped sprite sheet when necessary.
 * Downside: Uses more memory.
 * 
 * @author LostMekka
 */
public class FlippableSpriteSheet {
	
	private SpriteSheet sheet;
	private int tw, th, w, h;
	private final boolean flippableHorizontal, flippableVertical;

	public FlippableSpriteSheet(String src, int tileWidth, int tileHeight, boolean flippableHorizontal, boolean flippableVertical) {
		this.flippableHorizontal = flippableHorizontal;
		this.flippableVertical = flippableVertical;
		this.tw = tileWidth;
		this.th = tileHeight;
		Image i = AssetLoader.getImage(src, false);
		if(i.getWidth() % tileWidth != 0 || i.getHeight() % tileHeight != 0){
			throw new RuntimeException("FlippableSpriteSheet create: image size(" + 
					i.getWidth() + "," + i.getHeight() + 
					") not compatible with tile size(" + 
					tileWidth + "," + tileHeight + ")");
		}
		if(i == null) throw new RuntimeException("Image \"" + src + "\" could not be loaded!");
		w = i.getWidth() / tw;
		h = i.getHeight() / th;
		if(!flippableHorizontal && !flippableVertical){
			// if no flipping is required, dont start drawing stuff
			sheet = new SpriteSheet(i, tw, th);
			return;
		}
		int wmod = flippableHorizontal ? 2 : 1;
		int hmod = flippableVertical ? 2 : 1;
		Image result;
		try {
			// new Image(wmod * i.getWidth(), hmod * i.getHeight()) does not set the filter and weirdly does not 
			// allow to set the filter afterwards, so i use this workaround
			result = new Image(new ImageBuffer(wmod * i.getWidth(), hmod * i.getHeight()), Image.FILTER_NEAREST);
			Graphics g = result.getGraphics();
			g.drawImage(i, 0, 0);
			if(flippableHorizontal){
				Image tmp = i.getFlippedCopy(true, false);
				g.drawImage(tmp, i.getWidth(), 0);
			}
			if(flippableVertical){
				Image tmp = i.getFlippedCopy(false, true);
				g.drawImage(tmp, 0, i.getHeight());
			}
			if(flippableHorizontal && flippableVertical){
				Image tmp = i.getFlippedCopy(true, true);
				g.drawImage(tmp, i.getWidth(), i.getHeight());
			}
			g.flush();
			g.destroy();
			sheet = new SpriteSheet(result, tw, th);
		} catch (SlickException ex) {
			throw new RuntimeException("failed to load texture!");
		}
	}
	
	public Image getSprite(int x, int y, boolean flipHorizontal, boolean flipVertical){
		if(x < 0 || y < 0 || x >= w || y >= h){
			throw new RuntimeException("Sprite sheet index out of bounds: (" +
					x + "," + y + "), size:(" + 
					w + "," + h + ")");
		}
		if(flipHorizontal){
			if(!flippableHorizontal) throw new RuntimeException("Sprite sheet is not flippable horizontally!");
			x = 2 * w - 1 - x;
		}
		if(flipVertical){
			if(!flippableVertical) throw new RuntimeException("Sprite sheet is not flippable vertically!");
			y = 2 * h - 1 - y;
		}
		return sheet.getSprite(x, y);
	}
	
	public Animation createAnimation(int[] x, int[] y, boolean flipHorizontal, boolean flipVertical, int durations){
		boolean[] h = new boolean[x.length];
		Arrays.fill(h, flipHorizontal);
		boolean[] v = new boolean[x.length];
		Arrays.fill(v, flipVertical);
		int[] d = new int[x.length];
		Arrays.fill(d, durations);
		return createAnimation(x, y, h, v, d);
	}
	
	public Animation createAnimation(int[] x, int[] y, boolean[] flipHorizontal, boolean[] flipVertical, int durations){
		int[] d = new int[x.length];
		Arrays.fill(d, durations);
		return createAnimation(x, y, flipHorizontal, flipVertical, d);
	}
	
	public Animation createAnimation(int[] x, int[] y, boolean flipHorizontal, boolean flipVertical, int[] durations){
		boolean[] h = new boolean[x.length];
		Arrays.fill(h, flipHorizontal);
		boolean[] v = new boolean[x.length];
		Arrays.fill(v, flipVertical);
		return createAnimation(x, y, h, v, durations);
	}
	
	public Animation createAnimation(int[] x, int[] y, boolean[] flipHorizontal, boolean[] flipVertical, int[] durations){
		int len = x.length;
		if(y.length != len || flipHorizontal.length != len || flipVertical.length != len){
			throw new RuntimeException("input arrays differ in length!");
		}
		int ssw = flippableHorizontal ? 2 * w : w;
		int[] indices = new int[2 * len];
		for(int i=0; i<len; i++){
			if(flipHorizontal[i] && !flippableHorizontal) throw new RuntimeException("Animation needs horizontally flipped sprites, but there are none!");
			if(flipVertical[i] && !flippableVertical) throw new RuntimeException("Animation needs vertically flipped sprites, but there are none!");
			if(x[i] < 0 || y[i] < 0 || x[i] >= w || y[i] >= h){
				throw new RuntimeException("Sprite sheet index out of bounds:(" + 
						x[i] + "," + y[i] + ") size:(" + 
						w + "," + h + ")");
			}
			int ssx = flipHorizontal[i] ? 2 * w - 1 - x[i] : x[i];
			int ssy = flipVertical[i] ? 2 * h - 1 - y[i] : y[i];
			indices[2 * i] = ssx;
			indices[2 * i + 1] = ssy;
		}
		Animation ani = new Animation(sheet, indices, durations);
		ani.setAutoUpdate(false);
		return ani;
	}

	public void renderInUse(int x, int y, float scale) {
		sheet.renderInUse(x, y, Math.round(scale * tw), Math.round(scale * th));
	}

	public void endUse() {
		sheet.endUse();
	}

	public void startUse() {
		sheet.startUse();
	}
	
}
