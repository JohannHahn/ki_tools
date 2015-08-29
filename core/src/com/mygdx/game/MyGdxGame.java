package com.mygdx.game;

import java.util.ArrayList;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.Script.ScriptHolder;

public class MyGdxGame extends Game {
	
	public SpriteBatch batch;
	
	
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		ScriptHolder.loadDefault();
		setScreen(new SplashScreen(this));
	}
	
	

	@Override
	public void render () {
		super.render();
		
	}
}
