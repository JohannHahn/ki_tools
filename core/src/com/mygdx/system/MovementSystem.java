package com.mygdx.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.components.BoidCenterComponent;
import com.mygdx.components.BoidDistanceComponent;
import com.mygdx.components.BoidMatchVelocityComponent;
import com.mygdx.components.FleeComponent;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.RenderComponent;
import com.mygdx.components.SeekComponent;
import com.mygdx.components.VelocityComponent;

public class MovementSystem extends EntitySystem {

	private static final float OPTIMAL_BOID_DISTANCE = 20;

	private static final int GROUP_RANGE = 500;

	private ImmutableArray<Entity> entities;

	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	private ComponentMapper<SeekComponent> sm = ComponentMapper.getFor(SeekComponent.class);
	private ComponentMapper<FleeComponent> fm = ComponentMapper.getFor(FleeComponent.class);

	public MovementSystem() {
	}

	public void addedToEngine(Engine engine) {
		// TODO: attache family rendercomponent
		entities = engine.getEntitiesFor(Family.getFor(PositionComponent.class, VelocityComponent.class,
				BoidCenterComponent.class, BoidDistanceComponent.class, BoidMatchVelocityComponent.class));
		System.out.println("MovementSystem added");
	}

	public void update(float deltaTime) {
		for (int i = 0; i < entities.size(); ++i) {
			Entity entity = entities.get(i);
			PositionComponent position = pm.get(entity);
			updateVectors(entity, position);
			setPositon(entity, position);
		}
	}

	// SETs the Position after all Vectors are calculated
	private void setPositon(Entity entity, PositionComponent positionComp) {
		SeekComponent seekComp = sm.get(entity);
		FleeComponent fleeComp = fm.get(entity);
		VelocityComponent velComp = vm.get(entity);
		Vector2 bCenter = entity.getComponent(BoidCenterComponent.class).vectorCenter.cpy();
		Vector2 bMV = entity.getComponent(BoidMatchVelocityComponent.class).vectorMatchVelocity.cpy();
		Vector2 bDistance = entity.getComponent(BoidDistanceComponent.class).vectorDistance.cpy();

		//velComp.vectorVelocity.add(bCenter);
		//velComp.vectorVelocity.add(bMV);
		//velComp.vectorVelocity.add(bDistance);
	
		if (seekComp != null) {
			velComp.vectorVelocity = seekComp.vectorSeek;
			
		}
		if (fleeComp != null) {
			velComp.vectorVelocity = fleeComp.vectorFlee;
		}
		velComp.vectorVelocity = truncate(velComp.vectorVelocity, velComp.maxVelocity);
		positionComp.position.add(velComp.vectorVelocity);
		/*
		positionComp.position.add(bCenter);
		positionComp.position.add(bMV);
		positionComp.position.add(bDistance);
		*/
		
	}

	private void updateVectors(Entity entity, PositionComponent position) {
		// NODES:
		// Pseudocode Boid: http://www.kfish.org/boids/pseudocode.html
		// Perfect example: https://processing.org/examples/flocking.html

		// Vector in Component schreiben
		entity.getComponent(BoidCenterComponent.class).vectorCenter = calculateVectorBoidCenter(entity, position);
		entity.getComponent(BoidDistanceComponent.class).vectorDistance = calculateVectorBoidDistance(entity, position);
		entity.getComponent(BoidMatchVelocityComponent.class).vectorMatchVelocity = calculateVectorBoidMatchVc(entity,
				position);

		SeekComponent seekComp = sm.get(entity);
		FleeComponent fleeComp = fm.get(entity);

		if (seekComp != null)
			entity.getComponent(SeekComponent.class).vectorSeek = calculateVectorSeekFlee(entity, position);
		if (fleeComp != null)
			entity.getComponent(FleeComponent.class).vectorFlee = calculateVectorSeekFlee(entity, position);

		// For debugging press D
		if (Gdx.input.isKeyJustPressed(Keys.D)) {
			System.out.println("/////////////////// Vectors of a Boide //////////////////////////");
			System.out.println("VectorBoidCenter: " + calculateVectorBoidCenter(entity, position).x + " / "
					+ calculateVectorBoidCenter(entity, position).y);
			System.out.println("VectorBoidDistance: " + calculateVectorBoidDistance(entity, position).x + " / "
					+ calculateVectorBoidDistance(entity, position).y);
			System.out.println("VectorBoidMatchVc: " + calculateVectorBoidMatchVc(entity, position) + " / "
					+ calculateVectorBoidMatchVc(entity, position).y);
			System.out.println("VectorSeekOrFlee: " + calculateVectorSeekFlee(entity, position).x + " / "
					+ calculateVectorSeekFlee(entity, position).y);
			System.out.println("////////////////////////////////////////////////////////////////");
			System.out.println(entity.getComponent(VelocityComponent.class).vectorVelocity);
		}

	}

	// DONE
	private Vector2 calculateVectorSeekFlee(Entity entity, PositionComponent positionComp) {
		Vector2 result = new Vector2();
		Vector2 position = positionComp.position;
		Vector2 target = new Vector2(Gdx.input.getX(), Gdx.app.getGraphics().getHeight() - Gdx.input.getY());
		float slowingRadius = OPTIMAL_BOID_DISTANCE;
		SeekComponent seekComp = sm.get(entity);
		FleeComponent fleeComp = fm.get(entity);
		VelocityComponent velComp = vm.get(entity);
		Vector2 velocity = velComp.vectorVelocity.cpy();
		
		if (seekComp != null) {			
			Vector2 desired_velocity = target.sub(position);
			float distance = desired_velocity.len();
			
			// slow down if inside slowing area
			if (distance < slowingRadius) {
				// Inside the slowing area
				desired_velocity.nor().scl(velComp.maxVelocity * (distance / slowingRadius));
			} else {
				// Outside the slowing area.
				desired_velocity.nor().scl(velComp.maxVelocity);
			}
			Vector2 steering = desired_velocity.sub(velocity);
			steering = truncate(steering, velComp.maxForce);
			// steering = steering / mass
			velocity = truncate(velocity.add(steering), velComp.maxSpeed);
			result = velocity;
		}  
		if (fleeComp != null) {
			
			Vector2 desired_velocity = target.sub(position).nor().scl(velComp.maxVelocity * -1);
			Vector2 steering = desired_velocity.sub(velocity);
			steering = truncate(steering, velComp.maxForce);
			// steering = steering / mass
			velocity = truncate(velocity.add(steering), velComp.maxSpeed);
			result = velocity;
		}
		return result;
	}

	// TODO
	private Vector2 calculateVectorBoidMatchVc(Entity entity, PositionComponent positionComp) {

		Vector2 result = new Vector2();

		float SMALLING_VELOCITY_FACTOR = 8;
		Vector2 positionVectorBoid = positionComp.position.cpy();
		boolean xRange = false;
		boolean yRange = false;
		int boidCounter = 0;

		for (int i = 0; i < entities.size(); ++i) {
			if (!entity.equals(entities.get(i))) {

				// near enought?
				if (GROUP_RANGE > distance(positionVectorBoid, pm.get(entities.get(i)).position)) {
					// pvJ = pvJ + b.velocity
					float d = distance(positionVectorBoid, pm.get(entities.get(i)).position);

					Vector2 entitiesVelocity = entities.get(i).getComponent(VelocityComponent.class).vectorVelocity;

					result = result.add(entitiesVelocity);

					boidCounter++;
				}

			}

		}

		// durch n-1
		if (boidCounter > 0) // precrement
		// result.scl(1 / boidCounter);
		{
			result.x = result.x / boidCounter;
			result.y = result.y / boidCounter;
		}

		// result= new
		// Vector2(result.x/SMALLING_VELOCITY_FACTOR,result.y/SMALLING_VELOCITY_FACTOR);

		// return result;
		result.sub(entity.getComponent(VelocityComponent.class).vectorVelocity);
		result.scl(1 / SMALLING_VELOCITY_FACTOR);
		return result;
	}

	// DONE
	private Vector2 calculateVectorBoidDistance(Entity entity, PositionComponent position) {

		Vector2 result = new Vector2();

		Vector2 positionVectorBoid = position.position.cpy();

		float percentNearing = 100;
		int entityWith = entity.getComponent(RenderComponent.class).getWidth();
		int entityHeight = entity.getComponent(RenderComponent.class).getHeight();
		int boidCounter = 0;

		for (int i = 0; i < entities.size(); ++i) {
			if (!entity.equals(entities.get(i))) {

				// near enought?
				if (OPTIMAL_BOID_DISTANCE > distance(positionVectorBoid, pm.get(entities.get(i)).position)) {

					int entityIWith = entities.get(i).getComponent(RenderComponent.class).getWidth();
					int entityIHeight = entities.get(i).getComponent(RenderComponent.class).getHeight();

					// float d=
					// distance(position.position,pm.get(entities.get(i)).position);
					result.sub(
							 pm.get(entities.get(i)).position.x -positionVectorBoid.x - entityIWith / 2	- entityWith / 2,
							pm.get(entities.get(i)).position.y - positionVectorBoid.y  -entityHeight / 2- entityIHeight / 2) ;
					
					/*entityIWith / 2	- entityWith / 2
					entityHeight / 2- entityIHeight / 2)*/
					// result.nor();
					// result.scl(1/d);
					boidCounter++;
				}
				// is not working tried to bigger the vector for smaller
				// distances
				// percentNearing=MathUtils.clamp(Math.min(pm.get(entities.get(i)).position.x-positionVectorBoid.x,pm.get(entities.get(i)).position.y-positionVectorBoid.y),70,100);

			}

		}
		// durch n-1
		if (boidCounter > 0) // precrement
		// result.scl(1 / boidCounter);
		{
			result.x = result.x / boidCounter;
			result.y = result.y / boidCounter;
		} // result.x = result.x / percentNearing;
			// result.y = result.y / percentNearing;
		return result;
	}

	// DONE
	private Vector2 calculateVectorBoidCenter(Entity entity, PositionComponent position) {

		Vector2 positionVectorBoid = position.position.cpy();

		Vector2 result = new Vector2();

		float percentNearing = 100;
		boolean xRange = false, yRange = false;
		int boidCounter = 0;

		for (int i = 0; i < entities.size(); ++i) {

			if (!entity.equals(entities.get(i))) {

				// near enought?
				if (GROUP_RANGE >= distance(positionVectorBoid, pm.get(entities.get(i)).position)) {

					result.add(pm.get(entities.get(i)).position);
					boidCounter++;
				}

			}

		}

		// durch n-1
		if (boidCounter > 0) // precrement
		{
			// result.scl(1 / boidCounter);
			result.x = result.x / boidCounter;
			result.y = result.y / boidCounter;

			result.sub(positionVectorBoid);

		}
		// result.scl(1.0f / percentNearing);
		result.x = result.x / percentNearing;
		result.y = result.y / percentNearing;
		return result;
	}

	// like Clamp
	private Vector2 truncate(Vector2 vector, float max) {
		float i;
		i = max / vector.len();
		i = i < 1.0f ? i : 1.0f;
		vector.scl(i);
		return vector;
	}

	private float distance(Vector2 v1, Vector2 v2) {
		Vector2 temp = v1.cpy();
		temp = temp.sub(v2);

		return temp.len();
	}
}
