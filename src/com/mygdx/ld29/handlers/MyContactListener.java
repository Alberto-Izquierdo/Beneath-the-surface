package com.mygdx.ld29.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.mygdx.ld29.LD29;

public class MyContactListener implements ContactListener {
	
	private boolean player1OnGround;
	private boolean player2OnGround;
	
	private boolean dead = false;
	
	private boolean finish1 = false;
	private boolean finish2 = false;
	
	private boolean blockOff = true;
	
	LD29 game;
	
	private Array<Body> blocks;
	// called when two fixtures start to collide
	public void beginContact(Contact c) {
		
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		//the player can jump
		if(fa.getUserData() != null && fa.getUserData().equals("1") && (fb.getUserData() != null && fb.getUserData().equals("Floor") || (fb.getUserData() != null && fb.getUserData().equals("Block") && !fb.isSensor()))) {
			player1OnGround = true;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("1") && (fa.getUserData() != null && fa.getUserData().equals("Floor") || (fa.getUserData() != null && fa.getUserData().equals("Block") && !fa.isSensor()))) {
			player1OnGround = true;
		}
		if(fa.getUserData() != null && fa.getUserData().equals("2") && (fb.getUserData() != null && fb.getUserData().equals("Floor") || (fb.getUserData() != null && fb.getUserData().equals("Block") && !fb.isSensor()))) {
			player2OnGround = true;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("2") && (fa.getUserData() != null && fa.getUserData().equals("Floor") || (fa.getUserData() != null && fa.getUserData().equals("Block") && !fa.isSensor()))) {
			player2OnGround = true;
		}
		
		
		//the player contacts with spikes
		if(fa.getUserData() != null && fa.getUserData().equals("player") && fb.getUserData() != null && fb.getUserData().equals("spike")) {
			dead = true;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("player") && fa.getUserData() != null && fa.getUserData().equals("spike")) {
			dead = true;
		}
		
		
		//delete the bullet
		if(fa.getUserData() != null && fa.getUserData().equals("bullet") && (fb.getUserData() != null && fb.getUserData().equals("Floor") || fb.getUserData() != null && fb.getUserData().equals("Block") && !fb.isSensor())) {
			fa.setUserData("dead");
		}
		if(fb.getUserData() != null && fb.getUserData().equals("bullet") && fa.getUserData() != null && (fa.getUserData().equals("Floor") || fb.getUserData() != null && fa.getUserData().equals("Block") && !fa.isSensor())) {
			fb.setUserData("dead");
		}
		
		//bullet touches the switch
		if(fa.getUserData() != null && fa.getUserData().equals("bullet") && fb.getUserData() != null && fb.getUserData().equals("Switch")) {
			fa.setUserData("dead");
			if(blockOff){
				for(Body b: blocks){
					b.getFixtureList().first().setSensor(false);
				}
			}
			else{
				for(Body b: blocks){
					b.getFixtureList().first().setSensor(true);
				}
			}
			blockOff = !blockOff;
			game.switchblock.play();
		}
		if(fb.getUserData() != null && fb.getUserData().equals("bullet") && fa.getUserData() != null && fa.getUserData().equals("Switch")) {
			fb.setUserData("dead");
			if(blockOff){
				for(Body b: blocks){
					b.getFixtureList().first().setSensor(false);
				}
			}
			else{
				for(Body b: blocks){
					b.getFixtureList().first().setSensor(true);
				}
			}
			System.out.println("switch");
			blockOff = !blockOff;
		}
		
		
		//finish the level
		if(fa.getUserData() != null && fa.getUserData().equals("1") && fb.getUserData() != null && fb.getUserData().equals("Finish")) {
			finish1 = true;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("1") && fa.getUserData() != null && fa.getUserData().equals("Finish")) {
			finish1 = true;
		}
		if(fa.getUserData() != null && fa.getUserData().equals("2") && fb.getUserData() != null && fb.getUserData().equals("Finish")) {
			finish2 = true;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("2") && fa.getUserData() != null && fa.getUserData().equals("Finish")) {
			finish2 = true;
		}
		
	}
	
	// called when two fixtures no longer collide
	public void endContact(Contact c) {
		
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		
		//checks if the players can jump
		if(fa.getUserData() != null && fa.getUserData().equals("1") && (fb.getUserData() != null && fb.getUserData().equals("Floor") || fb.getUserData() != null && fb.getUserData().equals("Block"))) {
			player1OnGround = false;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("1") && (fa.getUserData() != null && fa.getUserData().equals("Floor") || fa.getUserData() != null && fa.getUserData().equals("Block"))) {
			player1OnGround = false;
		}
		if(fa.getUserData() != null && fa.getUserData().equals("2") && (fb.getUserData() != null && fb.getUserData().equals("Floor") || fb.getUserData() != null && fb.getUserData().equals("Block"))) {
			player2OnGround = false;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("2") && (fa.getUserData() != null && fa.getUserData().equals("Floor") || fa.getUserData() != null && fa.getUserData().equals("Block"))) {
			player2OnGround = false;
		}
		
		//the player contacts with spikes
				if(fa.getUserData() != null && fa.getUserData().equals("player") && fb.getUserData() != null && !fb.getUserData().equals("spike")) {
					dead = false;
				}
				if(fb.getUserData() != null && fb.getUserData().equals("player") && fa.getUserData() != null && !fa.getUserData().equals("spike")) {
					dead = false;
				}
				
				if(fa.getUserData() != null && fa.getUserData().equals("1") && fb.getUserData() != null && fb.getUserData().equals("Finish")) {
					finish1 = false;
				}
				if(fb.getUserData() != null && fb.getUserData().equals("1") && fa.getUserData() != null && fa.getUserData().equals("Finish")) {
					finish1 = false;
				}
				if(fa.getUserData() != null && fa.getUserData().equals("2") && fb.getUserData() != null && fb.getUserData().equals("Finish")) {
					finish2 = false;
				}
				if(fb.getUserData() != null && fb.getUserData().equals("2") && fa.getUserData() != null && fa.getUserData().equals("Finish")) {
					finish2 = false;
				}
		
	}
	
	public boolean isPlayer1OnGround() { return player1OnGround; }
	public boolean isPlayer2OnGround() { return player2OnGround; }
	
	public boolean isPlayerDead() { return dead; }
	
	
	
	public void preSolve(Contact c, Manifold m) {}
	public void postSolve(Contact c, ContactImpulse ci) {}
	
	public void setFinishFalse(){
		finish1 = false;
		finish2 = false;
	}
	
	public boolean getFinish1(){
		return finish1;
	}
	
	public boolean getFinish2(){
		return finish2;
	}
	
	public boolean blockOff(){
		return blockOff;
	}
	
	public void setBlocks(Array<Body> blocks){
		this.blocks = blocks;
	}
	
	public void setGame(LD29 game){
		this.game = game;
	}
	
	public void blockOn(){
		blockOff = true;
	}
	
}

