/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jump.level;

import java.util.Random;
import jump.MyGame;
import org.newdawn.slick.Animation;

/**
 *
 * @author LostMekka
 */
public class ZerglingEntity extends Entity{

	private enum MindState { rest, patrolLeft, patrolRight }
	
	private MindState mindState = MindState.rest;
	private int stateTimer = 0;
	private Animation idleAniL = null, idleAniR = null, 
			walkAniL = null, walkAniR = null, 
			jumpAniL = null, jumpAniR = null, 
			fallAniL = null, fallAniR = null;
	
	public ZerglingEntity(boolean looksToTheLeft, float x, float y) {
		super(looksToTheLeft, x, y);
		idleAniL = ffs.createAnimation(new int[]{0,1}, new int[]{2,2}, true, false, 450);
		idleAniR = ffs.createAnimation(new int[]{0,1}, new int[]{2,2}, false, false, 450);
		walkAniL = ffs.createAnimation(new int[]{0,1,2,3}, new int[]{3,3,3,3}, true, false, 180);
		walkAniR = ffs.createAnimation(new int[]{0,1,2,3}, new int[]{3,3,3,3}, false, false, 180);
		setState(EntityState.idle);
	}

	@Override
	protected void tickInternal(Level level, int ms) {
		stateTimer -= ms;
		if(stateTimer <= 0){
			// do something!!
			if(mindState == MindState.rest){
				if(MyGame.random.nextBoolean()){
					mindState = MindState.patrolLeft;
				} else {
					mindState = MindState.patrolRight;
				}
				stateTimer = MyGame.random.nextInt(2000) + 500;
			} else {
				mindState = MindState.rest;
				stateTimer = MyGame.random.nextInt(1000) + 300;
			}
		}
		boolean l = (mindState == MindState.patrolLeft);
		boolean r = (mindState == MindState.patrolRight);
		setInputByAI(l, r, false, false, false, false, false, false);
	}

	@Override
	public void drawInternal(float x, float y) {}

	@Override
	public EntityInputMode getInputMode() {
		return EntityInputMode.computer;
	}

	@Override
	public boolean usesGravity() {
		return true;
	}

	@Override
	public boolean collidesOnForeground() {
		return true;
	}

	@Override
	public float getVxOnKeyPressed(boolean run) {
		return 3f;
	}

	@Override
	public float getAyOnUpPressed() {
		return MyGame.GRAVITY * 0.8f;
	}

	@Override
	public float getInitialJunpVel() {
		return 7f;
	}

	@Override
	public int getmaxJumpTime() {
		return 1000;
	}

	@Override
	public Animation getAnimation(EntityState state, boolean facesLeft) {
		if(facesLeft){
			switch(state){
				case idle: return idleAniL;
//				case jump: return jumpAniL;
//				case fall: return fallAniL;
				case walk: return walkAniL;
			}
		} else {
			switch(state){
				case idle: return idleAniR;
//				case jump: return jumpAniR;
//				case fall: return fallAniR;
				case walk: return walkAniR;
			}
		}
		throw new RuntimeException("No animation found for entity state!");
	}
	
}
