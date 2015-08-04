package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.TextureComponent;
import com.mygdx.system.MovementSystem;
import com.mygdx.system.RenderSystem;

public class MainScreen implements Screen {
    Engine engine;
    Entity bild,bild2,bild3,bild4;
    Texture text = new Texture("badlogic.jpg");
    Stage stage = new Stage();
    Image img = new Image(text);
    MyGdxGame game;
    
    
    
    //comment
    public MainScreen(MyGdxGame game) {
        engine = new Engine();
        bild = new Entity();
        bild.add(new PositionComponent());
        bild.add(new TextureComponent(text));
        bild2= new Entity();
        bild2.add(new PositionComponent(450,450));
        bild2.add(new TextureComponent(text));
        bild3 = new Entity();
        bild3.add(new PositionComponent(200,200));
        bild3.add(new TextureComponent(text));
        bild4= new Entity();
        bild4.add(new PositionComponent(700,0));
        bild4.add(new TextureComponent(text));
        this.game = game;   
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RenderSystem(game.batch));
        engine.addEntity(bild);
        engine.addEntity(bild2);
        engine.addEntity(bild3);
        engine.addEntity(bild4);
    }

    @Override
    public void show() {
        
        
    }

    @Override
    public void render(float delta) {
    	Gdx.gl.glClearColor(0, 0, 0.2f, 1);
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
