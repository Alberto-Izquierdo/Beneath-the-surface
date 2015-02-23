package com.mygdx.ld29;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.mygdx.ld29.handlers.MyInputProcessor;
import com.mygdx.ld29.screens.mainScreen;

public class LD29 extends Game {

	public Array<String> screens;
	public Sound jumping, laser, switchblock, death;
	public int deaths = 0;
	public float timer = 0f;

	@Override
	public void create () {
		jumping = Gdx.audio.newSound(Gdx.files.internal("sounds/jump-final.mp3"));
		laser = Gdx.audio.newSound(Gdx.files.internal("sounds/laser-final.mp3"));
		switchblock = Gdx.audio.newSound(Gdx.files.internal("sounds/switchblock-final.mp3"));
		death = Gdx.audio.newSound(Gdx.files.internal("sounds/death-final.mp3"));
		Gdx.input.setInputProcessor(new MyInputProcessor());
		screens = new Array<String>();
		String s;
		s = "level-one.tmx";
		screens.add(s);
		s = "level-two.tmx";
		screens.add(s);
		s = "level-three.tmx";
		screens.add(s);
		s = "level-four.tmx";
		screens.add(s);
		s = "level-five.tmx";
		screens.add(s);
		s = "level-six.tmx";
		screens.add(s);
		s = "level-seven.tmx";
		screens.add(s);
		s = "level-eight.tmx";
		screens.add(s);
		s = "level-nine.tmx";
		screens.add(s);
		s = "level-ten.tmx";
		screens.add(s);
		s = "level-eleven.tmx";
		screens.add(s);
		s = "level-twelve.tmx";
		screens.add(s);
		s = "level-thirteen.tmx";
		screens.add(s);
		s = "level-fourteen.tmx";
		screens.add(s);
		s = "level-fifteen.tmx";
		screens.add(s);
		s = "level-sixteen.tmx";
		screens.add(s);
		this.setScreen(new mainScreen(this));
	}
}
