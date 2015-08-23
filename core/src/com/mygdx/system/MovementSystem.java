package com.mygdx.system;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.components.BoidCenterComponent;
import com.mygdx.components.BoidDistanceComponent;
import com.mygdx.components.BoidMatchVelocityComponent;
import com.mygdx.components.EvadeComponent;
import com.mygdx.components.FleeComponent;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.PursuitComponent;
import com.mygdx.components.RenderComponent;
import com.mygdx.components.SeekComponent;
import com.mygdx.components.VelocityComponent;
public class MovementSystem extends EntitySystem {

	private static final float OPTIMAL_BOID_DISTANCE = 40;

	private static final int GROUP_RANGE = 100;

	private ImmutableArray<Entity> entities;

	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	private ComponentMapper<SeekComponent> sm = ComponentMapper.getFor(SeekComponent.class);
	private ComponentMapper<FleeComponent> fm = ComponentMapper.getFor(FleeComponent.class);
	private ComponentMapper<PursuitComponent> purMapper = ComponentMapper.getFor(PursuitComponent.class);
	private ComponentMapper<EvadeComponent> em = ComponentMapper.getFor(EvadeComponent.class);
	
	private int windowWidth;
	private int windowHeight;

	public MovementSystem() {
		windowWidth = Gdx.graphics.getWidth();
		windowHeight = Gdx.graphics.getHeight();
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
		PursuitComponent purComp = purMapper.get(entity);
		EvadeComponent evadeComp = em.get(entity);
		VelocityComponent velComp = vm.get(entity);
		
		Vector2 bCenter = entity.getComponent(BoidCenterComponent.class).vectorCenter.cpy();
		Vector2 bMV = entity.getComponent(BoidMatchVelocityComponent.class).vectorMatchVelocity.cpy();
		Vector2 bDistance = entity.getComponent(BoidDistanceComponent.class).vectorDistance.cpy();		
		
		//velComp.vectorVelocity.setZero();
		
		if (seekComp != null) {
			velComp.vectorVelocity.add(seekComp.vectorSeek);
		}
		if (fleeComp != null) {
			velComp.vectorVelocity.add(fleeComp.vectorFlee);
		}		
		if (purComp != null) {
			velComp.vectorVelocity.add(purComp.vectorPersuit);
		}
		if (evadeComp != null) {
			velComp.vectorVelocity.add(evadeComp.vectorEvade);
		}

		Vector2 boidVector = new Vector2();
		boidVector.add(bCenter);
		boidVector.add(bDistance);
		boidVector.add(bMV);		
		velComp.vectorVelocity.add(boidVector);		
				
		velComp.vectorVelocity.clamp(0, velComp.maxSpeed);
		
		arrival(entity);	
		
		positionComp.position.add(velComp.vectorVelocity);	
		
		//Clamp in screen
		boundCoordinates(positionComp.position);		
		
		//rotate to velocity direction
		float angle = velComp.direction.angle(velComp.vectorVelocity);
		velComp.direction.rotate(angle);
		//velComp.direction.nor();
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
		PursuitComponent purComp = purMapper.get(entity);
		EvadeComponent evadeComp = em.get(entity);

		if (seekComp != null)
			seekComp.vectorSeek = calculateVectorSeekFlee(entity, position);
		if (fleeComp != null)
			fleeComp.vectorFlee = calculateVectorSeekFlee(entity, position);
		if (purComp != null){
			purComp.vectorPersuit = calculatePursuit(entity);
			purComp.vectorPersuit.scl(3f);
		}
		if(evadeComp != null){
			evadeComp.vectorEvade = calculateEvade(entity);
		}

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
	
	/*private Vector2 calculateWander(BoidEntity boid) {
		   // Calculate the circle center
		   Vector2 circleCenter;
		   circleCenter = velocity.clone();
		   circleCenter.normalize();
		   circleCenter.scaleBy(CIRCLE_DISTANCE);
		   //
		   // Calculate the displacement force
		   var displacement :Vector3D;
		   displacement = new Vector3D(0, -1);
		   displacement.scaleBy(CIRCLE_RADIUS);
		   //
		   // Randomly change the vector direction
		   // by making it change its current angle
		   setAngle(displacement, wanderAngle);
		   //
		   // Change wanderAngle just a bit, so it
		   // won't have the same value in the
		   // next game frame.
		   wanderAngle += Math.random() * ANGLE_CHANGE - ANGLE_CHANGE * .5;
		   //
		   // Finally calculate and return the wander force
		   var wanderForce :Vector3D;
		   wanderForce = circleCenter.add(displacement);
		   return wanderForce;
		}*/
		 
		


	private Vector2 calculatePursuit(BoidEntity boid){
		PursuitComponent pc = purMapper.get(boid);
		PositionComponent targetPos = pm.get(pc.target);
		VelocityComponent targetVel = vm.get(pc.target);
		
		float T = targetPos.position.dst(pm.get(boid).position) / targetVel.maxVelocity;		
		Vector2 futurePosition  = targetPos.position.cpy();
		Vector2 futureVelocity = targetVel.vectorVelocity.cpy();
		futureVelocity.scl(T);
		futurePosition.add(futureVelocity);
		
		return vectorSeek(boid, futurePosition);
	}
	
	private Vector2 calculateEvade(BoidEntity boid){
		EvadeComponent ec = em.get(boid);
		PositionComponent targetPos = pm.get(ec.target);
		VelocityComponent targetVel = vm.get(ec.target);
		
		float T = targetPos.position.dst(pm.get(boid).position) / targetVel.maxVelocity;		
		Vector2 futurePosition  = targetPos.position.cpy();
		Vector2 futureVelocity = targetVel.vectorVelocity.cpy();
		futureVelocity.scl(T);
		futurePosition.add(futureVelocity);
		Vector2 result = vectorSeek(boid, futurePosition);
		result.scl(-1);
		return result;
	}
	
	private Vector2 calculateVectorSeekFlee(BoidEntity entity, PositionComponent positionComp) {
		Vector2 result = new Vector2();
		Vector2 position = positionComp.position;
		SeekComponent seekComp = sm.get(entity);
		FleeComponent fleeComp = fm.get(entity);
		VelocityComponent velComp = vm.get(entity);
		Vector2 velocity = velComp.vectorVelocity.cpy();
		Vector2 target;
		
		//Seek
		if (seekComp != null) {
			target = seekComp.target.cpy();
			result = vectorSeek(entity, target);
		}
		
		//Flee
		if (fleeComp != null) {
			target = fleeComp.target.cpy();	
			//Flee = -1 * Seek
			result = vectorSeek(entity, target);
			result.scl(-1);
		}
		
		return result;
	}
	private Vector2 calculateVectorBoidMatchVc(BoidEntity entity, PositionComponent positionComp) {

		Vector2 result = new Vector2();

		float SMALLING_VELOCITY_FACTOR = 8;
		Vector2 positionVectorBoid = positionComp.position.cpy();
		int boidCounter = 0;

		for (int i = 0; i < entities.size(); ++i) {
			BoidEntity currentEntity = (BoidEntity)entities.get(i);
			
			//Betrachte nur andere Boids und nur aus dem selben Team
			if (!entity.equals(currentEntity) && entity.team == currentEntity.team) {
				//Im Sichtfeld?
				Vector2 connectionVector = new Vector2(positionComp.position);
				connectionVector.sub(pm.get(entities.get(i)).position).scl(-1);
				float angle	= connectionVector.angle(vm.get(entity).direction);
				boolean inSight = angle < 150 && angle > -150;
				
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
	private Vector2 calculateVectorBoidDistance(BoidEntity entity, PositionComponent position) {

		Vector2 result = new Vector2();

		Vector2 positionVectorBoid = position.position.cpy();
		int entityWith = entity.getComponent(RenderComponent.class).getWidth();
		int entityHeight = entity.getComponent(RenderComponent.class).getHeight();
		int boidCounter = 0;
		float d=1;
		//for each boid
		for (int i = 0; i < entities.size(); ++i) {			
			BoidEntity currentEntity = (BoidEntity)entities.get(i);
			
			//Betrachte nur andere Boids und nur aus dem selben Team			
			if (!entity.equals(currentEntity) && entity.team == currentEntity.team){				
				//Im Sichtfeld?
				Vector2 connectionVector = new Vector2(position.position);
				connectionVector.sub(pm.get(entities.get(i)).position).scl(-1);
				float angle	= connectionVector.angle(vm.get(entity).direction);
				boolean inSight = angle < 150 && angle > -150;
				
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
	private Vector2 calculateVectorBoidCenter(BoidEntity entity, PositionComponent position) {

		Vector2 positionVectorBoid = position.position.cpy();
		Vector2 result = new Vector2();
		int entityWith = entity.getComponent(RenderComponent.class).getWidth();
		int entityHeight = entity.getComponent(RenderComponent.class).getHeight();
		int boidCounter = 0;

		float d = 1;
		// for each
		for (int i = 0; i < entities.size(); ++i) {
			BoidEntity currentEntity = (BoidEntity)entities.get(i);
			
			//Betrachte nur andere Boids und nur aus dem selben Team
			if (!entity.equals(currentEntity) && entity.team == currentEntity.team) {

				int entityIWith = entities.get(i).getComponent(RenderComponent.class).getWidth();
				int entityIHeight = entities.get(i).getComponent(RenderComponent.class).getHeight();
				
				//Im Sichtfeld?
				Vector2 connectionVector = new Vector2(position.position);
				connectionVector.sub(pm.get(entities.get(i)).position).scl(-1);
				float angle	= connectionVector.angle(vm.get(entity).direction);
				boolean inSight = angle < 150 && angle > -150;
				
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
	
	private void boundCoordinates(Vector2 pos){
		if(pos.x > windowWidth)
		{
			pos.x -= windowWidth;
			pos.y = windowHeight - pos.y;
		}
		
		if(pos.x < 0)
		{
			pos.x += windowWidth;
			pos.y = windowHeight - pos.y;
		}
		
		if(pos.y > windowHeight)
		{
			pos.y -= windowHeight;
			pos.x = windowWidth - pos.x;
		}
		
		if(pos.y < 0)
		{
			pos.y += windowHeight;
			pos.x = windowWidth - pos.x;
		}
	}
	
	private Vector2 vectorSeek(Entity entity, Vector2 target){		
		Vector2 position = entity.getComponent(PositionComponent.class).position;		
				
		VelocityComponent velComp = vm.get(entity);
		Vector2 velocity = velComp.vectorVelocity.cpy();		
		
		Vector2 desired_velocity = target.sub(position);		
		desired_velocity.nor().scl(velComp.maxVelocity);
		
		Vector2 steering = desired_velocity.sub(velocity);
		steering = truncate(steering, velComp.maxForce);
		
		// steering = steering / mass
		velocity = truncate(velocity.add(steering), velComp.maxSpeed);
		return velocity;
	}
	
	private void arrival(Entity entity){
		SeekComponent seekComp = sm.get(entity);
		PursuitComponent purComp = purMapper.get(entity);
		PositionComponent positionComp = pm.get(entity);
		VelocityComponent velComp = vm.get(entity);
		
		Vector2 target = seekComp != null ? seekComp.target : purComp != null ? pm.get(purComp.target).position : null;
		
		if(target != null){
			float distance = target.dst(positionComp.position); 
			float slowingRadius = 50f; 
			if(distance < slowingRadius){
				velComp.vectorVelocity.clamp(0, velComp.maxSpeed * (distance / slowingRadius)); 
			}
		}
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
