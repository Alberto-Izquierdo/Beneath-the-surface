package com.mygdx.ld29.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PlayerShoot {
	BodyDef bdef;
	Body body;
	float lifetime;
	private boolean right;
	
	public PlayerShoot(Vector2 pos, boolean right, World world){
		this.right = right;
		bdef = new BodyDef();
		bdef.position.set(pos);
		bdef.type = BodyType.DynamicBody;
		bdef.angle = 0;
		bdef.fixedRotation = false;
		body = world.createBody(bdef);
		body.setBullet(true);
		PolygonShape bodyShape = new PolygonShape();
		float w = 0.8f;
		float h = 0.8f;
		bodyShape.setAsBox(w, h);
		
		FixtureDef fdef = new FixtureDef();
		fdef.isSensor = true;
		fdef.shape = bodyShape;
		
		body.createFixture(fdef).setUserData("bullet");
		bodyShape.dispose();
		if(right){
			body.applyForceToCenter(new Vector2(5000, 0), true);
		}
		else{
			body.applyForceToCenter(new Vector2(-5000, 0), true);
		}
	}
	public boolean update(float delta){
		body.applyForceToCenter(new Vector2(0, 150),  true);
		if(body.getFixtureList().first().getUserData().equals("dead")){
			return false;
		}
		return true;
	}
	
	public void destroyBody(World world){
		body.destroyFixture(body.getFixtureList().first());
	}
	
	public boolean getRight(){
		return right;
	}
	
	public float getX(){
		return body.getPosition().x;
	}
	public float getY(){
		return body.getPosition().y;
	}

}
