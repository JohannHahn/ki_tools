package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.Entities.BoidEntity;
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
    
    
    public MainScreen(MyGdxGame game) {
        engine = new Engine();
        
        boid = new BoidEntity(BoidEntity.Team.RED);
        boid.add(new PositionComponent(MathUtils.random(0,500 ),MathUtils.random(0,500)));       
        boid.add(new VelocityComponent());
        boid.add(new SeekComponent());  
        boid.add(new FleeComponent());
        boid.add(new RenderComponent());
        boid.add(new BoidCenterComponent());
        boid.add(new BoidDistanceComponent());
        boid.add(new BoidMatchVelocityComponent());
        
        boid2= new BoidEntity(BoidEntity.Team.RED);
        boid2.add(new PositionComponent(MathUtils.random(0,500 ),MathUtils.random(0,500)));        
        boid2.add(new SeekComponent());  
        boid2.add(new FleeComponent());
        boid2.add(new VelocityComponent());
        boid2.add(new RenderComponent());
        boid2.add(new BoidCenterComponent());
        boid2.add(new BoidDistanceComponent());
        boid2.add(new BoidMatchVelocityComponent());
 
        boid3 = new BoidEntity(BoidEntity.Team.RED);
        boid3.add(new PositionComponent(MathUtils.random(0,500 ),MathUtils.random(0,500)));        
        boid3.add(new VelocityComponent());
        boid3.add(new SeekComponent());  
        boid3.add(new FleeComponent());
        boid3.add(new RenderComponent());
        boid3.add(new BoidCenterComponent());
        boid3.add(new BoidDistanceComponent());
        boid3.add(new BoidMatchVelocityComponent());
 
        boid4= new BoidEntity(BoidEntity.Team.RED);
        boid4.add(new PositionComponent(MathUtils.random(0,600 ),MathUtils.random(0,600 )));        
        boid4.add(new VelocityComponent());
        boid4.add(new SeekComponent());      
        boid4.add(new RenderComponent(text,40,40));
        boid4.add(new BoidCenterComponent());
        boid4.add(new BoidDistanceComponent());
        boid4.add(new BoidMatchVelocityComponent());
        
        boid5= new BoidEntity(BoidEntity.Team.RED);
        boid5.add(new PositionComponent(MathUtils.random(0,600 ),MathUtils.random(0,600 )));        
        boid5.add(new VelocityComponent());
        boid5.add(new SeekComponent());
        boid5.add(new RenderComponent());
        
        boid5.add(new BoidCenterComponent());
        boid5.add(new BoidDistanceComponent());
        boid5.add(new BoidMatchVelocityComponent());
        
        boid6 =new BoidEntity(BoidEntity.Team.RED);
        boid6.add(new PositionComponent(MathUtils.random(0,600 ),MathUtils.random(0,600 )));
        
        boid6.add(new VelocityComponent());
        boid6.add(new SeekComponent());
        boid6.add(new RenderComponent());
        boid6.add(new BoidCenterComponent());
        boid6.add(new BoidDistanceComponent());
        boid6.add(new BoidMatchVelocityComponent());
        
        boid7= new BoidEntity(BoidEntity.Team.RED);
        boid7.add(new PositionComponent(MathUtils.random(0,600 ),MathUtils.random(0,600 )));
        
        boid7.add(new VelocityComponent());
        boid7.add(new SeekComponent());
        boid7.add(new RenderComponent());
        boid7.add(new BoidCenterComponent());
        boid7.add(new BoidDistanceComponent());
        boid7.add(new BoidMatchVelocityComponent());
        
        this.game = game;   
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RenderSystem(game.batch));
        
        engine.addEntity(boid);
        engine.addEntity(boid2);
        engine.addEntity(boid3);
        engine.addEntity(boid4);
        engine.addEntity(boid5);
        engine.addEntity(boid6);
        engine.addEntity(boid7);
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
