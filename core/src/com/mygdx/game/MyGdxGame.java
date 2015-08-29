package com.mygdx.game;

import java.util.ArrayList;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.Script.ScriptHolder;

public class MyGdxGame extends Game {
	
	public SpriteBatch batch;
	
	public ArrayList<BoidEntity> boids= new ArrayList<BoidEntity>();
	
	
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
/*
 *TODO:
 *-normalize four max velocity has to be done after the "final" result vector is created
 *- vectoren abstimmen distance muss immer gröser sein als 
 *-distance vektor muss die breite seines und der anderen körper kennen
 *-need a velocityvector for each boid to match the velocity(verschieben unserer vektor berechnung in der methode updateVector in die components)
 *
 *SeekFlee mehrere objekte für teambeahviour
 *Velocity muss abnehmen überzeit trägheit simulation
 */
