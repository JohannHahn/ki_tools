package com.mygdx.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.TextureComponent;

public class RenderSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    

    private ComponentMapper<com.mygdx.components.TextureComponent> tm = ComponentMapper.getFor(TextureComponent.class);
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);    

    public RenderSystem(SpriteBatch batch) {
        this.batch = batch;
        shapeRenderer= new ShapeRenderer();
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(TextureComponent.class, PositionComponent.class));
        System.out.println("Rendersystem added");
                
    }
//Changed
    public void update(float deltaTime) {
        
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            PositionComponent positionComp = pm.get(entity);
            Vector2 position = positionComp.position;
            TextureComponent texture = tm.get(entity);
            batch.begin();
            shapeRenderer.setAutoShapeType(true);
            shapeRenderer.begin();
            
            shapeRenderer.triangle(position.x, position.y, position.x+10, position.y,position.x+5, position.y+10);
          //  batch.draw(texture.txt, position.x, position.y);
            shapeRenderer.end();
            batch.end();
        }
    }
}
