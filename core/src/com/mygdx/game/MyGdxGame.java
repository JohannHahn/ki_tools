package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.Entities.BoidEntity;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public Texture img;
	public ArrayList<BoidEntity> boids= new ArrayList<BoidEntity>();
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		setScreen(new MainScreen(this));
	}
	
	public class PositionComponent extends Component{
	    
	}

	@Override
	public void render () {
		super.render();
		
	}
}
