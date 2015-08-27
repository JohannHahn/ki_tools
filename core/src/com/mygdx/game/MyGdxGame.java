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
import com.mygdx.Script.ScriptHolder;

public class MyGdxGame extends Game {
	private static final String SCRIPTPATH_EVADE     = "data/scripts/Evade.lua";
	private static final String SCRIPTPATH_PURSUIT   = "data/scripts/PursuitBehaviour.lua";
	private static final String SCRIPTPATH_NO_TARGET = "data/scripts/No_Target.lua";
	private static final String SCRIPTPATH_WANDER    = "data/scripts/Wander.lua";
	public SpriteBatch batch;
	public Texture img;
	public ArrayList<BoidEntity> boids= new ArrayList<BoidEntity>();
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		ScriptHolder.insertScript(SCRIPTPATH_EVADE);
		ScriptHolder.insertScript(SCRIPTPATH_PURSUIT);
		ScriptHolder.insertScript(SCRIPTPATH_NO_TARGET);
		ScriptHolder.insertScript(SCRIPTPATH_WANDER);
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
