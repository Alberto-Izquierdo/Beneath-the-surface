package com.mygdx.ld29.enemies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Rock {
	private BodyDef bdef;
	private Body body;
	
	public Rock(Vector2 pos, World world){
		bdef = new BodyDef();
		bdef.position.set(pos);
		bdef.type = BodyType.DynamicBody;
		bdef.angle = 0;
		bdef.fixedRotation = true;
		body = world.createBody(bdef);
		PolygonShape bodyShape = new PolygonShape();
		float w = 100/100;
		float h = 100/100;
		bodyShape.setAsBox(w, h);
		
		FixtureDef fdef = new FixtureDef();
		fdef.density = 0.4f;
		fdef.restitution = 0;
		fdef.shape = bodyShape;
		
		body.createFixture(fdef);
		bodyShape.dispose();
		body.setAwake(false);
	}
}
