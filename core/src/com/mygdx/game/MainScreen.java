package com.mygdx.game;

import java.awt.TextComponent;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.TextureComponent;
import com.mygdx.system.MovementSystem;
import com.mygdx.system.RenderSystem;

public class MainScreen implements Screen {
    Engine engine;
    Entity bild;
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
        this.game = game;   
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RenderSystem(game.batch));
        engine.addEntity(bild);
    }

    @Override
    public void show() {
        
        
    }

    @Override
    public void render(float delta) {
        
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
