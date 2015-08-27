package com.mygdx.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.Entities.BoidEntity.Team;
import com.mygdx.components.BoidCenterComponent;
import com.mygdx.components.BoidDistanceComponent;
import com.mygdx.components.BoidMatchVelocityComponent;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.RenderComponent;
import com.mygdx.components.VelocityComponent;

public class RenderSystem extends EntitySystem {

	private ImmutableArray<Entity> entities;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;

	// May be better deleted and use entity.getComponent
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<RenderComponent> rm = ComponentMapper.getFor(RenderComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	private Vector2 up = new Vector2(0, 1);
	private float rotation = 0f;
	float alpha = 0f;

	public RenderSystem(SpriteBatch batch) {
		this.batch = batch;
		shapeRenderer = new ShapeRenderer();
	}

	@SuppressWarnings("unchecked")
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(
				Family.all(PositionComponent.class, RenderComponent.class).get());
		System.out.println("Rendersystem added");

	}

	// Changed
	public void update(float deltaTime) {
		PositionComponent positionComp;
		RenderComponent renderComp;
		VelocityComponent velComp;
		Color fillColor=Color.CYAN;
		for (int i = 0; i < entities.size(); ++i) {
			Entity entity = entities.get(i);
			positionComp = pm.get(entity);
			renderComp = rm.get(entity);			
			
			Vector2 position = positionComp.position;

			batch.begin();
			if (renderComp.getTexture() != null) {
				Texture tempTexture = renderComp.getTexture();
				batch.draw(tempTexture, position.x - renderComp.getWidth() / 2, position.y - renderComp.getHeight() / 2, renderComp.getWidth(), renderComp.getHeight());
				batch.flush();
			} else {
				if (entity.getComponent(VelocityComponent.class) != null) {
					BoidEntity boid = (BoidEntity)entity;
					velComp = vm.get(entity);
					rotation = up.angle(velComp.direction);
					fillColor = boid.team == Team.GREEN ? Color.GREEN : Color.RED;
					
					shapeRenderer.setAutoShapeType(true);
					shapeRenderer.begin();
	
					shapeRenderer.set(ShapeType.Filled);
					shapeRenderer.identity();
					shapeRenderer.translate(position.x, position.y, 0);
					shapeRenderer.rotate(0f, 0f, 1f, rotation);
					shapeRenderer.triangle(-boid.width / 2f, 0, boid.width / 2, 0, 0, boid.height, fillColor, fillColor, fillColor);
	
					shapeRenderer.end();
				}
			}

			// SHows the vectors of each boid //is not working
			if (Gdx.input.isKeyPressed(Keys.V)) {
				shapeRenderer.setAutoShapeType(true);
				shapeRenderer.begin();
				shapeRenderer.setColor(Color.GREEN);
				float Scale = 10;
				Vector2 v1, v2, v3, v4;
				v1 = new Vector2(position.x + entity.getComponent(BoidCenterComponent.class).vectorCenter.x,
						positionComp.position.y + entity.getComponent(BoidCenterComponent.class).vectorCenter.y);
				v2 = new Vector2(position.x + entity.getComponent(BoidDistanceComponent.class).vectorDistance.x,
						positionComp.position.y + entity.getComponent(BoidDistanceComponent.class).vectorDistance.y);
				v3 = new Vector2(
						position.x + entity.getComponent(BoidMatchVelocityComponent.class).vectorMatchVelocity.x,
						positionComp.position.y
								+ entity.getComponent(BoidMatchVelocityComponent.class).vectorMatchVelocity.y);
				v4 = new Vector2(position.x + entity.getComponent(VelocityComponent.class).vectorVelocity.x,
						positionComp.position.y + entity.getComponent(VelocityComponent.class).vectorVelocity.y);

				shapeRenderer.line(positionComp.position, v1);
				shapeRenderer.setColor(Color.PURPLE);
				shapeRenderer.line(positionComp.position, v2.scl(Scale));
				shapeRenderer.line(positionComp.position, v3);
				shapeRenderer.setColor(Color.RED);
				shapeRenderer.line(positionComp.position, v4);
				shapeRenderer.end();
			}
			batch.end();
		}
	}
}
