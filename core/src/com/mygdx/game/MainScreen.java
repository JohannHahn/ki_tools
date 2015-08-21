package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.Entities.BoidState;
import com.mygdx.components.BoidCenterComponent;
import com.mygdx.components.BoidDistanceComponent;
import com.mygdx.components.BoidMatchVelocityComponent;
import com.mygdx.components.FleeComponent;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.RenderComponent;
import com.mygdx.components.SeekComponent;
import com.mygdx.components.VelocityComponent;
import com.mygdx.system.MovementSystem;
import com.mygdx.system.RenderSystem;
import com.sun.corba.se.spi.orbutil.fsm.State;

public class MainScreen implements Screen {
    Engine engine;
    BoidEntity boid,boid2,boid3,boid4,boid5,boid6, boid7;
    Texture text = new Texture("smiley.png");
    Stage stage = new Stage();
    Image img = new Image(text);
    MyGdxGame game;
    int boidTeamSize = 1;
    private int windowWidth = Gdx.graphics.getWidth();
	private int windowHeight = Gdx.graphics.getHeight();    
    
    public MainScreen(MyGdxGame game) {
    	
        engine = new Engine();        
        //Create Team Red
        for(int i = 0; i < boidTeamSize; i++){
        	BoidEntity boidR= new BoidEntity(BoidEntity.Team.RED, engine, BoidState.PURSUIT);
	        boidR.add(new PositionComponent(MathUtils.random(0,  windowWidth / 4f), MathUtils.random(0, windowHeight / 4f)));	        
	        boidR.add(new VelocityComponent());
	      //  boidR.add(new SeekComponent());
	     //   boidR.add(new FleeComponent());
	        boidR.add(new RenderComponent());
	        boidR.add(new BoidCenterComponent());
	        boidR.add(new BoidDistanceComponent());
	        boidR.add(new BoidMatchVelocityComponent());
	        engine.addEntity(boidR);
        }
        
      //Create Team Green
        for(int i = 0; i < boidTeamSize; i++){
        	BoidEntity boidR= new BoidEntity(BoidEntity.Team.GREEN, engine, BoidState.SEEKING);
	        boidR.add(new PositionComponent(MathUtils.random(windowWidth,  windowWidth - windowWidth / 4f), MathUtils.random(windowHeight, windowHeight - windowHeight / 4f)));	        
	        boidR.add(new VelocityComponent());
	        boidR.add(new SeekComponent());
	       // boidR.add(new FleeComponent());
	        boidR.add(new RenderComponent());
	        boidR.add(new BoidCenterComponent());
	        boidR.add(new BoidDistanceComponent());
	        boidR.add(new BoidMatchVelocityComponent());
	        engine.addEntity(boidR);
        }
        
        this.game = game;   
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RenderSystem(game.batch));
    }

    @Override
    public void show() {
        
        
    }    
    

    @Override
    public void render(float delta) {
    	Gdx.gl.glClearColor(1, 1, 1.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        engine.update(delta);
        
    }
    
    
    

    @Override
    public void resize(int width, int height) {
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
    public void hide() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }

}
