package com.mygdx.ld29.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.ld29.LD29;

public class mainScreen implements Screen {

	Texture texture_screen;
	Music song;
	OrthographicCamera cam;
	SpriteBatch batch;
	LD29 game;

	
	public mainScreen(LD29 game){
		this.game = game;
		game.deaths = 0;
		texture_screen = new Texture("stuff/titlescreen.png");
		song = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
		song.setLooping(true);
		song.play();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, 480, 720);
		cam.update();
		batch = new SpriteBatch();
		batch.getProjectionMatrix().set(cam.combined);
	}
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(texture_screen, 0, 0);
		batch.end();
		if(Gdx.input.isKeyPressed(Keys.SPACE)){
			game.setScreen(new SelectLevel(game));
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
