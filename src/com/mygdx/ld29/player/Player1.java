package com.mygdx.ld29.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Player1 {

	private final static float movement = 300;
	private BodyDef bdef;
	private Body body;
	private boolean facingRight = true;
	private float lastShot = 0;
	public enum State{
		going_up,
		going_down,
		stop,
		moving
	}
	
	State state = State.stop;

	public Player1 (Vector2 pos, World world, String number) {
		bdef = new BodyDef();
		bdef.position.set(pos);
		bdef.type = BodyType.DynamicBody;
		bdef.angle = 0;
		bdef.fixedRotation = true;
		bdef.allowSleep = false;
		body = world.createBody(bdef);
		PolygonShape bodyShape = new PolygonShape();
		float w = 24/20;
		float h = 36/20;
		bodyShape.setAsBox(w, h);
		
		FixtureDef fdef = new FixtureDef();
		fdef.density = 0.4f;
		fdef.restitution = 0.0f;
		fdef.shape = bodyShape;
		
		body.createFixture(fdef).setUserData("player");
		
		//sensor
		bodyShape.setAsBox(w/1.1f, h/4, new Vector2(0, -100/100), 0);
		fdef.shape = bodyShape;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData(number);
		bodyShape.dispose();
	}
	
	public boolean jump(){
		boolean a = false;
//		body.applyLinearImpulse(new Vector2(0, 30), body.getPosition(), true);

		if(body.getLinearVelocity().y<=0){
			a = true;
		}
		body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 40));
		return a;
	}
	
	public void update(){
		body.setLinearVelocity(new Vector2((float)(body.getLinearVelocity().x*0.96), (float)(body.getLinearVelocity().y)));
		Vector2 vel = body.getLinearVelocity();
		if(vel.y!=0){
			if(vel.y>0){
				state = State.going_up;
			}
			else{
				state = State.going_down;
			}
		}
		else{
			if(Math.abs(vel.x)>0.4){
				state = State.moving;
			}
			else{
				state = State.stop;
			}
		}
	}
	
	public void setPosition(float x, float y){
		body.setTransform(new Vector2(x, y), 0);
	}
	
	public void moveRight(){
		if(body.getLinearVelocity().x<20){
			body.applyForceToCenter(new Vector2(movement, 0), true);
			facingRight = true;
		}
	}
	public void moveLeft(){
		if(body.getLinearVelocity().x>-20){
			body.applyForceToCenter(new Vector2(-movement, 0), true);
			facingRight = false;
		}
	}
	
	public void shoot(Array<PlayerShoot> shoots, World world, float stateTime){
		if(stateTime-lastShot>1){
			PlayerShoot p = new PlayerShoot(body.getPosition(), facingRight, world);
			shoots.add(p);
			lastShot = stateTime;
		}
	}
	
	public boolean isFacingRight() {
		return facingRight;
	}
	
	public float getX(){
		return 	body.getPosition().x;
	}
	public float getY(){
		return body.getPosition().y;
	}
	public State getState(){
		return state;
	}
}
