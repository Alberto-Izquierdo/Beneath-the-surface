package com.mygdx.ld29.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.ld29.LD29;
import com.mygdx.ld29.handlers.MyInput;

public class SelectLevel implements Screen {

	OrthographicCamera cam;
	SpriteBatch batch;
	LD29 game;
	Texture texture_screen;
	Texture texture_select;
	
	boolean pressed;
	int levels = 0;
	
	public SelectLevel(LD29 game){
		this.game = game;
		pressed = false;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, 480, 720);
		cam.update();
		batch = new SpriteBatch();
		batch.getProjectionMatrix().set(cam.combined);
		
		texture_screen = new Texture("stuff/level-select-screen.png");
		texture_select = new Texture("stuff/highlight-level.png");
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(texture_screen, 0, 0);
		batch.end();
		update();
	}
	
	public void update(){
		if(MyInput.isDown(MyInput.BUTTON1)&&!pressed){
			levels = levels-1;
			if(levels<0){
				levels = 15;
			}
			System.out.println(levels);
			pressed = true;
		}
		else if(MyInput.isDown(MyInput.BUTTON2)&&!pressed){
			levels +=1;
			if(levels>15)
				levels = 0;
			System.out.println(levels);
			pressed = true;
		}
		else if(MyInput.isDown(MyInput.BUTTON3)&&!pressed){
			levels -= 4;
			if(levels<0)
				levels += 16;
			System.out.println(levels);
			pressed = true;
		}
		else if(MyInput.isDown(MyInput.BUTTON5)&&!pressed){
			levels += 4;
			if(levels>15){
				levels-=16;
			}
			System.out.println(levels);
			pressed = true;
		}
		else if(!MyInput.isDown(MyInput.BUTTON1)&&!MyInput.isDown(MyInput.BUTTON2)&&!MyInput.isDown(MyInput.BUTTON3)&&!MyInput.isDown(MyInput.BUTTON5)){
			pressed = false;
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
