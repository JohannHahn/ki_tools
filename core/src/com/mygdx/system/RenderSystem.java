package com.mygdx.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.RenderComponent;
import com.mygdx.components.TextureComponent;

public class RenderSystem extends EntitySystem {

	private ImmutableArray<Entity> entities;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;

	// May be better deleted and use entity.getComponent
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<RenderComponent> rm = ComponentMapper.getFor(RenderComponent.class);

	public RenderSystem(SpriteBatch batch) {
		this.batch = batch;
		shapeRenderer = new ShapeRenderer();
	}

	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.getFor(PositionComponent.class, RenderComponent.class));
		System.out.println("Rendersystem added");

	}

	// Changed
	public void update(float deltaTime) {
		PositionComponent positionComp;
		RenderComponent renderComp;
		for (int i = 0; i < entities.size(); ++i) {
			Entity entity = entities.get(i);
			positionComp = pm.get(entity);
			renderComp = rm.get(entity);

			Vector2 position = positionComp.position;

			batch.begin();

			
			if (renderComp.getTexture() != null) {
				Texture tempTexture = renderComp.getTexture();
				batch.draw(tempTexture, position.x, position.y, renderComp.getWidth(), renderComp.getHeight());
			} else {
				shapeRenderer.setAutoShapeType(true);
				shapeRenderer.begin();

				shapeRenderer.triangle(position.x, position.y, position.x + 10, position.y, position.x + 5,
						position.y + 10);

				shapeRenderer.end();
			}

			batch.end();
		}
	}
}
