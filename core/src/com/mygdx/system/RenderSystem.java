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
		entities = engine.getEntitiesFor(Family.all(PositionComponent.class, RenderComponent.class).get());
		System.out.println("Rendersystem added");

	}

	// Changed
	public void update(float deltaTime) {
		PositionComponent positionComp;
		RenderComponent renderComp;
		VelocityComponent velComp;
		Color fillColor = Color.CYAN;
		for (int i = 0; i < entities.size(); ++i) {
			Entity entity = entities.get(i);
			positionComp = pm.get(entity);
			renderComp = rm.get(entity);

			Vector2 position = positionComp.position;

			batch.begin();
			if (renderComp.getTexture() != null) {
				Texture tempTexture = renderComp.getTexture();
				batch.draw(tempTexture, position.x - renderComp.getWidth() / 2, position.y - renderComp.getHeight() / 2,
						renderComp.getWidth(), renderComp.getHeight());
				batch.flush();
			} else {
				if (entity.getComponent(VelocityComponent.class) != null) {
					BoidEntity boid = (BoidEntity) entity;
					velComp = vm.get(entity);
					rotation = up.angle(velComp.direction);
					fillColor = boid.team == Team.GREEN ? Color.GREEN : Color.RED;

					shapeRenderer.setAutoShapeType(true);
					shapeRenderer.begin();

					shapeRenderer.set(ShapeType.Filled);
					shapeRenderer.identity();
					shapeRenderer.translate(position.x, position.y, 0);
					shapeRenderer.rotate(0f, 0f, 1f, rotation);
					shapeRenderer.triangle(-boid.width / 2f, 0, boid.width / 2, 0, 0, boid.height, fillColor, fillColor,
							fillColor);

					shapeRenderer.end();
				}
			}

			// Shows the Areas
			if (Gdx.input.isKeyPressed(Keys.D)) {
				shapeRenderer.setAutoShapeType(true);
				shapeRenderer.begin();

				try {

					shapeRenderer.setColor(Color.BLUE);
					shapeRenderer.circle(0, 0, ((BoidEntity) entity).sightRadius);
					shapeRenderer.setColor(Color.RED);
					shapeRenderer.circle(0, 0, MovementSystem.OPTIMAL_BOID_DISTANCE);
				} catch (Exception e) {
					if (e.getClass() != ClassCastException.class)
						System.out.println(e);
				} finally {
					shapeRenderer.end();
				}

			}
			//Shows the Vectors
			if (Gdx.input.isKeyPressed(Keys.V)) {
				float Scale = 20;
			
				
				Vector2 v1 = null, v2, v3, v4;
				if (entity.getComponent(BoidCenterComponent.class) != null) {
					BoidEntity boid = (BoidEntity) entity;
					try {
						v1 = new Vector2(entity.getComponent(BoidCenterComponent.class).vectorCenter.x,
								entity.getComponent(BoidCenterComponent.class).vectorCenter.y);
						v2 = new Vector2(entity.getComponent(BoidDistanceComponent.class).vectorDistance.x,
								entity.getComponent(BoidDistanceComponent.class).vectorDistance.y);
						v3 = new Vector2(entity.getComponent(VelocityComponent.class).vectorVelocity.x,
								entity.getComponent(VelocityComponent.class).vectorVelocity.y);
					
						shapeRenderer.setAutoShapeType(true);
						shapeRenderer.begin();
						//Center
						shapeRenderer.setColor(Color.OLIVE);
						//.clamp(MovementSystem.OPTIMAL_BOID_DISTANCE,MovementSystem.OPTIMAL_BOID_DISTANCE))
						shapeRenderer.line(new Vector2(0,boid.height/2), v1.scl(-Scale));
						//Distance
						shapeRenderer.setColor(Color.RED);
						shapeRenderer.line(new Vector2(0,boid.height/2), v2.scl(Scale));
						//Velocity
						shapeRenderer.setColor(Color.BLUE);
						shapeRenderer.line(new Vector2(0,boid.height/2), v3.scl(Scale));
						

					} catch (Exception e) {
						//if (e.getClass() != ClassCastException.class)
							//System.out.println(e);
					} finally {
						shapeRenderer.end();
					}
				}

			}
			
			batch.end();
		}
	}
}
