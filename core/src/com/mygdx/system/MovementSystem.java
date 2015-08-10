package com.mygdx.system;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.Entities.BoidState;
import com.mygdx.components.BoidCenterComponent;
import com.mygdx.components.BoidDistanceComponent;
import com.mygdx.components.BoidMatchVelocityComponent;
import com.mygdx.components.FleeComponent;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.RenderComponent;
import com.mygdx.components.SeekComponent;
import com.mygdx.components.VelocityComponent;
public class MovementSystem extends EntitySystem {

	private static final float OPTIMAL_BOID_DISTANCE = 80;

	private static final int GROUP_RANGE = 100;

	private ImmutableArray<Entity> entities;

	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	private ComponentMapper<SeekComponent> sm = ComponentMapper.getFor(SeekComponent.class);
	private ComponentMapper<FleeComponent> fm = ComponentMapper.getFor(FleeComponent.class);

	public MovementSystem() {
	}

	public void addedToEngine(Engine engine) {
		// TODO: attache family rendercomponent
		entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class, BoidCenterComponent.class,
						BoidDistanceComponent.class, BoidMatchVelocityComponent.class, RenderComponent.class).get());
		System.out.println("MovementSystem added");
	}

	public void update(float deltaTime) {
		for (int i = 0; i < entities.size(); ++i) {
			BoidEntity entity = (BoidEntity)entities.get(i);
			PositionComponent position = pm.get(entity);
			updateVectors(entity, position);
			setPositon(entity, position);
			entity.update(deltaTime);
		}
	}

	// SETs the Position after all Vectors are calculated
	private void setPositon(BoidEntity entity, PositionComponent positionComp) {
		
		SeekComponent seekComp = sm.get(entity);
		FleeComponent fleeComp = fm.get(entity);
		VelocityComponent velComp = vm.get(entity);
		Vector2 bCenter = entity.getComponent(BoidCenterComponent.class).vectorCenter.cpy();
		Vector2 bMV = entity.getComponent(BoidMatchVelocityComponent.class).vectorMatchVelocity.cpy();
		Vector2 bDistance = entity.getComponent(BoidDistanceComponent.class).vectorDistance.cpy().scl(1f / 3f);
		
		velComp.vectorVelocity.setZero();
		
		if (seekComp != null && entity.stateMachine.getCurrentState() == BoidState.SEEKING) {
			velComp.vectorVelocity = seekComp.vectorSeek.scl(1f / 2f);

		}
		if (fleeComp != null && entity.stateMachine.getCurrentState() == BoidState.FLEEING) {
			velComp.vectorVelocity = fleeComp.vectorFlee;
		}

		Vector2 boidVector = new Vector2();
		boidVector.add(bCenter);
		boidVector.add(bDistance);
		boidVector.add(bMV);

		/*
		 * //Arrival float distance = distance(new Vector2(bCenter).scl(100),
		 * positionComp.position); float slowingRadius = OPTIMAL_BOID_DISTANCE *
		 * 2; if(distance < slowingRadius){
		 * boidVector.nor().scl(velComp.maxVelocity * (distance /
		 * slowingRadius)); }
		 */

		velComp.vectorVelocity.add(boidVector);
		velComp.vectorVelocity = truncate(velComp.vectorVelocity, velComp.maxSpeed);
		positionComp.position.add(velComp.vectorVelocity);		

	}

	private void updateVectors(BoidEntity entity, PositionComponent position) {
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

		if (seekComp != null && entity.stateMachine.getCurrentState() == BoidState.SEEKING)
			entity.getComponent(SeekComponent.class).vectorSeek = calculateVectorSeekFlee(entity, position);
		if (fleeComp != null && entity.stateMachine.getCurrentState() == BoidState.FLEEING)
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
	private Vector2 calculateVectorSeekFlee(BoidEntity entity, PositionComponent positionComp) {
		Vector2 result = new Vector2();
		Vector2 position = positionComp.position;
		float slowingRadius = OPTIMAL_BOID_DISTANCE;
		SeekComponent seekComp = sm.get(entity);
		FleeComponent fleeComp = fm.get(entity);
		VelocityComponent velComp = vm.get(entity);
		Vector2 velocity = velComp.vectorVelocity.cpy();
		Vector2 target;
		
		//Seek
		if (seekComp != null && entity.stateMachine.getCurrentState() == BoidState.SEEKING) {
			target = seekComp.target;
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
		
		//Flee
		if (fleeComp != null && entity.stateMachine.getCurrentState() == BoidState.FLEEING) {
			target = fleeComp.target;
			Vector2 desired_velocity = target.sub(position).nor().scl(velComp.maxVelocity * -1);
			Vector2 steering = desired_velocity.sub(velocity);
			steering = truncate(steering, velComp.maxForce);
			// steering = steering / mass
			velocity = truncate(velocity.add(steering), velComp.maxSpeed);
			result = velocity;
		}
		
		return result;
	}
	private Vector2 calculateVectorBoidMatchVc(Entity entity, PositionComponent positionComp) {

		Vector2 result = new Vector2();

		float SMALLING_VELOCITY_FACTOR = 8;
		Vector2 positionVectorBoid = positionComp.position.cpy();
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
		result.x = result.x / SMALLING_VELOCITY_FACTOR;
		result.y = result.y / SMALLING_VELOCITY_FACTOR;
		return result;
	}

	// scaled bei distance
	private Vector2 calculateVectorBoidDistance(Entity entity, PositionComponent position) {

		Vector2 result = new Vector2();

		Vector2 positionVectorBoid = position.position.cpy();
		int entityWith = entity.getComponent(RenderComponent.class).getWidth();
		int entityHeight = entity.getComponent(RenderComponent.class).getHeight();
		int boidCounter = 0;
		float d=1;
		//for each boid
		for (int i = 0; i < entities.size(); ++i) {
			if (!entity.equals(entities.get(i))) {

				// near enought?
				if (OPTIMAL_BOID_DISTANCE > (d=distance(positionVectorBoid, pm.get(entities.get(i)).position))) {

					int entityIWith = entities.get(i).getComponent(RenderComponent.class).getWidth();
					int entityIHeight = entities.get(i).getComponent(RenderComponent.class).getHeight();

					// Calculate vector pointing away from neighbor
			        Vector2 diff = sub( positionVectorBoid,pm.get(entities.get(i)).position);
			        //abziehen der umfang der einzelnen entities
			        diff.sub(entityIWith/2 - entityWith/2, entityHeight/2-entityIHeight/2);
			        
			        diff.nor();
			        
			        diff.scl(OPTIMAL_BOID_DISTANCE/d);        // Weight by distance//wrong
			        result.add(diff);
					
					
					//hand the object borders has to be added
					/*entityIWith / 2	- entityWith / 2
					entityHeight / 2- entityIHeight / 2)*/
					
					boidCounter++;
				}

			}

		}
		// durch n-1
		if (boidCounter > 0) 
			scaleVector(result,(1.0f/boidCounter));
		
		return result;
	}

	// scaled bei distance //darf nur bis zu einer gewissen distance scaliert
	// werden
	private Vector2 calculateVectorBoidCenter(Entity entity, PositionComponent position) {

		Vector2 positionVectorBoid = position.position.cpy();
		Vector2 result = new Vector2();
		int entityWith = entity.getComponent(RenderComponent.class).getWidth();
		int entityHeight = entity.getComponent(RenderComponent.class).getHeight();
		int boidCounter = 0;

		float d = 1;
		// for each
		for (int i = 0; i < entities.size(); ++i) {
			// not equals
			if (!entity.equals(entities.get(i))) {

				int entityIWith = entities.get(i).getComponent(RenderComponent.class).getWidth();
				int entityIHeight = entities.get(i).getComponent(RenderComponent.class).getHeight();
				// near enought?
				if (GROUP_RANGE >= (d = distance(positionVectorBoid, pm.get(entities.get(i)).position))) {

					Vector2 diff= new Vector2();
					diff=sub( pm.get(entities.get(i)).position , positionVectorBoid);
					diff.sub(entityIWith/2 - entityWith/2, entityHeight/2-entityIHeight/2);
					diff.nor();
					diff.scl(d/OPTIMAL_BOID_DISTANCE);        // Weight by distance
					result.add(diff);
					boidCounter++;
				}

			}

		}

		// durch n-1
		if (boidCounter > 0) // precrement
		{
			scaleVector(result, 1f / boidCounter);
		}
		
		//returning a relativ vector
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

	private void scaleVector(Vector2 result, float f) {
		{
			result.x = result.x * f;
			result.y = result.y * f;
		}

	}

	private Vector2 sub(Vector2 positionVectorBoid, Vector2 position) {
		Vector2 temp;
		temp = new Vector2(positionVectorBoid.x - position.x, positionVectorBoid.y - position.y);
		return temp;
	}

}

// preversion
/*
 * 
 * // scaled bei distance private Vector2 calculateVectorBoidDistance(Entity
 * entity, PositionComponent position) {
 * 
 * Vector2 result = new Vector2();
 * 
 * Vector2 positionVectorBoid = position.position.cpy();
 * 
 * float percentNearing = 25f; int entityWith =
 * entity.getComponent(RenderComponent.class).getWidth(); int entityHeight =
 * entity.getComponent(RenderComponent.class).getHeight(); int boidCounter = 0;
 * float d=1; for (int i = 0; i < entities.size(); ++i) { if
 * (!entity.equals(entities.get(i))) {
 * 
 * // near enought? if (OPTIMAL_BOID_DISTANCE > (d=distance(positionVectorBoid,
 * pm.get(entities.get(i)).position))) {
 * 
 * int entityIWith =
 * entities.get(i).getComponent(RenderComponent.class).getWidth(); int
 * entityIHeight =
 * entities.get(i).getComponent(RenderComponent.class).getHeight();
 * 
 * 
 * // distance(position.position,pm.get(entities.get(i)).position); result.sub(
 * pm.get(entities.get(i)).position.x -positionVectorBoid.x,
 * pm.get(entities.get(i)).position.y - positionVectorBoid.y ) ;
 * 
 * /* // Calculate vector pointing away from neighbor Vector2 diff =
 * sub(positionVectorBoid, pm.get(entities.get(i)).position); diff.nor();
 * diff.scl(d); // Weight by distance result.add(diff);
 */

/*
 * entityIWith / 2 - entityWith / 2 entityHeight / 2- entityIHeight / 2) //
 * result.nor(); // result.scl(1/d); boidCounter++; } // is not working tried to
 * bigger the vector for smaller // distances //
 * percentNearing=MathUtils.clamp(Math.min(pm.get(entities.get(i)).position.x-
 * positionVectorBoid.x,pm.get(entities.get(i)).position.y-positionVectorBoid.y)
 * ,70,100);
 * 
 * }
 * 
 * } // durch n-1 if (boidCounter > 0) // precrement // result.scl(1 /
 * boidCounter); { result.x = result.x / boidCounter; result.y = result.y /
 * boidCounter; } result.x = result.x / percentNearing; result.y = result.y /
 * percentNearing;
 * 
 * return result; }
 */
