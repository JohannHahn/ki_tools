package com.mygdx.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
        System.out.println(entities.size());
                
    }
//20€
    public void update(float deltaTime) {
        
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            PositionComponent position = pm.get(entity);
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
