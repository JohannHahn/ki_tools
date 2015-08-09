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

public class MainScreen implements Screen {
    Engine engine;
    Entity bild,bild2,bild3,bild4,bild5,bild6,bild7;
    Texture text = new Texture("smiley.png");
    Stage stage = new Stage();
    Image img = new Image(text);
    MyGdxGame game;
    
    
    public MainScreen(MyGdxGame game) {
        engine = new Engine();
        bild = new Entity();

        bild.add(new PositionComponent(MathUtils.random(0,500 ),MathUtils.random(0,500 )));
       
        bild.add(new VelocityComponent());
        bild.add(new SeekComponent());  
        bild.add(new RenderComponent(text,40,40));
        bild.add(new BoidCenterComponent());
        bild.add(new BoidDistanceComponent());
        bild.add(new BoidMatchVelocityComponent());
        
        bild2= new Entity();
        bild2.add(new PositionComponent(MathUtils.random(0,500 ),MathUtils.random(0,500 )));
        
        bild2.add(new SeekComponent());
        bild2.add(new VelocityComponent());
        bild2.add(new RenderComponent(text,40,40));
        bild2.add(new BoidCenterComponent());
        bild2.add(new BoidDistanceComponent());
        bild2.add(new BoidMatchVelocityComponent());
 
        bild3 = new Entity();
        bild3.add(new PositionComponent(MathUtils.random(0,500 ),MathUtils.random(0,500 )));
        
        bild3.add(new VelocityComponent());
        bild3.add(new SeekComponent());
       // bild3.add(new RenderComponent(text,40,40));
        bild3.add(new BoidCenterComponent());
        bild3.add(new BoidDistanceComponent());
        bild3.add(new BoidMatchVelocityComponent());
 
        bild4= new Entity();
        bild4.add(new PositionComponent(MathUtils.random(0,600 ),MathUtils.random(0,600 )));
        
        bild4.add(new VelocityComponent());
        bild4.add(new SeekComponent());
      
     //   bild4.add(new RenderComponent(text,40,40));
        bild4.add(new BoidCenterComponent());
        bild4.add(new BoidDistanceComponent());
        bild4.add(new BoidMatchVelocityComponent());
        
        bild5= new Entity();
        bild5.add(new PositionComponent(MathUtils.random(0,600 ),MathUtils.random(0,600 )));        
        bild5.add(new VelocityComponent());
        bild5.add(new SeekComponent());
   //     bild5.add(new RenderComponent());
        
        bild5.add(new BoidCenterComponent());
        bild5.add(new BoidDistanceComponent());
        bild5.add(new BoidMatchVelocityComponent());
        
        bild6 = new Entity();
        bild6.add(new PositionComponent(MathUtils.random(0,600 ),MathUtils.random(0,600 )));
        
        bild6.add(new VelocityComponent());
        bild6.add(new SeekComponent());
   //     bild6.add(new RenderComponent());
        bild6.add(new BoidCenterComponent());
        bild6.add(new BoidDistanceComponent());
        bild6.add(new BoidMatchVelocityComponent());
        
        bild7= new Entity();
        bild7.add(new PositionComponent(MathUtils.random(0,600 ),MathUtils.random(0,600 )));
        
        bild7.add(new VelocityComponent());
        bild7.add(new SeekComponent());
      //  bild7.add(new RenderComponent());
        bild7.add(new BoidCenterComponent());
        bild7.add(new BoidDistanceComponent());
        bild7.add(new BoidMatchVelocityComponent());
        
        this.game = game;   
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RenderSystem(game.batch));
        
        engine.addEntity(bild);
        engine.addEntity(bild2);
        engine.addEntity(bild3);
        engine.addEntity(bild4);
        engine.addEntity(bild5);
        engine.addEntity(bild6);
        engine.addEntity(bild7);
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
