package com.mygdx.game;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.MainScreen.PositionComponent;

public class MovementSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

    public MovementSystem() {}

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(PositionComponent.class));
    }

    public void update(float deltaTime) {
        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            PositionComponent position = pm.get(entity);

            position.x += 2 * deltaTime;
            position.y += 2 * deltaTime;
        }
    }

}
