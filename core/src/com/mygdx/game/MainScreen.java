package com.mygdx.game;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class MainScreen implements Screen {
    Engine engine;
    Entity bild;
    Texture text = new Texture("badlogic.jpg");
    Stage stage = new Stage();
    Image img = new Image(text);
    MyGdxGame game;
    
    public class PositionComponent extends Component{
        public float x = 0.0f;
        public float y = 0.0f;         
    }
    
    public class TextureComponent extends Component{
        public Texture txt;
        public TextureComponent(Texture txt){
            this.txt = txt;
        }
    }
    
    
    public MainScreen(MyGdxGame game) {
        engine = new Engine();
        bild = new Entity();
        bild.add(new PositionComponent());
        bild.add(new TextureComponent(text));
        this.game = game;   
        engine.addSystem(new MovementSystem());
    }

    @Override
    public void show() {
        
        
    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        game.batch.draw(bild.getComponent(TextureComponent.class).txt, 0, 0);
        game.batch.end();
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
