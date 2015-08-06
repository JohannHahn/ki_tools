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
import com.badlogic.gdx.math.Vector2;
import com.mygdx.components.FleeComponent;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.RenderComponent;
import com.mygdx.components.SeekComponent;
import com.mygdx.components.VelocityComponent;

public class MovementSystem extends EntitySystem {

	private static final float OPTIMAL_BOID_DISTANCE = 20;

	private static final int GROUP_RANGE = 70;

	private ImmutableArray<Entity> entities;

	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
	private ComponentMapper<SeekComponent> sm = ComponentMapper.getFor(SeekComponent.class);
	private ComponentMapper<FleeComponent> fm = ComponentMapper.getFor(FleeComponent.class);

	public MovementSystem() {
	}

	public void addedToEngine(Engine engine) {
		// TODO: attache family rendercomponent
		entities = engine.getEntitiesFor(Family.getFor(PositionComponent.class, VelocityComponent.class));		
		System.out.println("MovementSystem added");
	}

	public void update(float deltaTime) {
		for (int i = 0; i < entities.size(); ++i) {
			Entity entity = entities.get(i);
			PositionComponent position = pm.get(entity);
			updateVectors(entity, position);
			setPositon();			
		}
	}
//SETs the Position after all Vectors are calculated
	private void setPositon() {
		// TODO: generate resulting Vector

	}
	
	private void updateVectors(Entity entity, PositionComponent position) {
		
		// Pseudocode Boid: http://www.kfish.org/boids/pseudocode.html
		Vector2 vectorBoidCenter = calculateVectorBoidCenter(entity, position);
		Vector2 vectorBoidDistance = calculateVectorBoidDistance(entity, position);
		Vector2 vectorBoidMatchVC = calculateVectorBoidMatchVc(entity, position);
		
		Vector2 vectorSeekorFlee = calculateVectorSeekFlee(entity, position);	
		
		//For debugging  press D
		if(Gdx.input.isKeyJustPressed(Keys.D))
		{	
			System.out.println("/////////////////// Vectors of a Boide //////////////////////////");
			System.out.println("VectorBoidCenter: " + vectorBoidCenter.x + " / " + vectorBoidCenter.y);
			System.out.println("VectorBoidDistance: " + vectorBoidDistance.x + " / " + vectorBoidDistance.y);
			System.out.println("VectorBoidMatchVc: " + vectorBoidMatchVC.x + " / " + vectorBoidMatchVC.y);
			System.out.println("VectorSeekOrFlee: " + vectorSeekorFlee.x + " / " + vectorSeekorFlee.y);
			System.out.println("////////////////////////////////////////////////////////////////");
		}
		
	}
	//DONE
	private Vector2 calculateVectorSeekFlee(Entity entity, PositionComponent positionComp) {
		Vector2 result = new Vector2();
		Vector2 position = positionComp.position;
		Vector2 target = new Vector2(Gdx.input.getX(), Gdx.app.getGraphics().getHeight() - Gdx.input.getY());
		float slowingRadius = OPTIMAL_BOID_DISTANCE;
		SeekComponent seekComp = sm.get(entity);
		FleeComponent fleeComp = fm.get(entity);
		VelocityComponent velComp = vm.get(entity);
		if(seekComp != null){
			//Der Maus folgen			
			Vector2 desired_velocity = target.sub(position);
			float distance = desired_velocity.len();
			//slow down if inside slowing area
			if (distance < slowingRadius) {
			    // Inside the slowing area
			    desired_velocity.nor().scl(velComp.maxVelocity * (distance / slowingRadius));
			} else {
			    // Outside the slowing area.
			    desired_velocity.nor().scl(velComp.maxVelocity);
			}
			Vector2 steering = desired_velocity.sub(velComp.vectorVelocity);			
		    steering = truncate(steering, velComp.maxForce);
			//steering = steering / mass
		    velComp.vectorVelocity = truncate(velComp.vectorVelocity.add(steering), velComp.maxSpeed);
		    result = velComp.vectorVelocity;
		    pm.get(entity).position.x += result.x;
			pm.get(entity).position.y += result.y;
		}
		else if(fleeComp != null){			
			Vector2 desired_velocity = target.sub(position).nor().scl(velComp.maxVelocity * -1);
			Vector2 steering = desired_velocity.sub(velComp.vectorVelocity);
		    steering = truncate(steering, velComp.maxForce);
			//steering = steering / mass
		    velComp.vectorVelocity = truncate(velComp.vectorVelocity.add(steering), velComp.maxSpeed);
		    result = velComp.vectorVelocity;
		    pm.get(entity).position.x += result.x;
			pm.get(entity).position.y += result.y;
		}
		
		return result;
	}
//TODO
	private Vector2 calculateVectorBoidMatchVc(Entity entity, PositionComponent positionComp) {
		
		Vector2 result = new Vector2();
		float SMALLING_VELOCITY_FACTOR=8;
		for (int i = 0; i < entities.size(); ++i) {
			if (!entity.equals(entities.get(i))) {
				//result =result.add(pm.get(entities.get(i)).position.x,pm.get(entities.get(i)).position.y);
				//TODO:	pvJ = pvJ + b.velocity
				
			}
			
		}
		
		result= new Vector2(result.x/SMALLING_VELOCITY_FACTOR,result.y/SMALLING_VELOCITY_FACTOR);
		
		
		return result;
	}
	
	// DONE
	private Vector2 calculateVectorBoidDistance(Entity entity, PositionComponent position) {
		
		Vector2 result = new Vector2();
		Vector2 positionVectorBoid = position.position;
		boolean xDistanceToSmall=false;
		boolean yDistanceToSmall=false;
		float percentNearing = 100;
		int entityWith=entity.getComponent(RenderComponent.class).getWidth();
		int entityHeight=entity.getComponent(RenderComponent.class).getHeight();
		
		for (int i = 0; i < entities.size(); ++i) {
			if (!entity.equals(entities.get(i))) {
				
				xDistanceToSmall=(OPTIMAL_BOID_DISTANCE*(-1))<(pm.get(entities.get(i)).position.x-positionVectorBoid.x) && (pm.get(entities.get(i)).position.x-positionVectorBoid.x)<OPTIMAL_BOID_DISTANCE;
				yDistanceToSmall=(OPTIMAL_BOID_DISTANCE*(-1))<(pm.get(entities.get(i)).position.y-positionVectorBoid.y) && (pm.get(entities.get(i)).position.y-positionVectorBoid.y)<OPTIMAL_BOID_DISTANCE;
				
				//near enought?
				if(xDistanceToSmall&&yDistanceToSmall){
					

					int entityIWith=entities.get(i).getComponent(RenderComponent.class).getWidth();
					int entityIHeight=entities.get(i).getComponent(RenderComponent.class).getHeight();
					
					result.sub(pm.get(entities.get(i)).position.x-positionVectorBoid.x-entityIWith/2-entityWith/2,pm.get(entities.get(i)).position.y-positionVectorBoid.y-entityHeight-entityIHeight);
					
				}
				//is not working tried to bigger the vector for smaller distances
				//percentNearing=MathUtils.clamp(Math.min(pm.get(entities.get(i)).position.x-positionVectorBoid.x,pm.get(entities.get(i)).position.y-positionVectorBoid.y),70,100);

			}
			result.x = result.x / percentNearing;
			result.y = result.y / percentNearing;
			//if activeted in maipulats the position without using a vector
			
			pm.get(entity).position.x += result.x;
			pm.get(entity).position.y += result.y;
			
		}
		return result;
	}

	// DONE
	private Vector2 calculateVectorBoidCenter(Entity entity, PositionComponent position) {

		Vector2 positionVectorBoid = position.position;
		Vector2 result = new Vector2();
		float percentNearing = 100;
		boolean xRange=false,yRange=false;
		
		for (int i = 0; i < entities.size(); ++i) {
			if (!entity.equals(entities.get(i))) {
				xRange=(GROUP_RANGE*(-1))<(pm.get(entities.get(i)).position.x-positionVectorBoid.x) && (pm.get(entities.get(i)).position.x-positionVectorBoid.x)<GROUP_RANGE;
				yRange=(GROUP_RANGE*(-1))<(pm.get(entities.get(i)).position.y-positionVectorBoid.y) && (pm.get(entities.get(i)).position.y-positionVectorBoid.y)<GROUP_RANGE;
				
				//near enought?
				if(xRange&&yRange){
					
				result.add(pm.get(entities.get(i)).position.x, pm.get(entities.get(i)).position.y);
				}

			}

		}
		if (entities.size() > 0) {
			result.x = result.x / (entities.size() - 1);
			result.y = result.y / (entities.size() - 1);
		}
		result.sub(positionVectorBoid);
		
		result.x = result.x / percentNearing;
		result.y = result.y / percentNearing;

		////if activeted in maipulats the position without using a vector
		
		pm.get(entity).position.x += result.x;
		pm.get(entity).position.y += result.y;
		
		//System.out.println("x: " + pm.get(entity).x + "	y:" + pm.get(entity).y);
		return result;
	}
	
	private Vector2 truncate(Vector2 vector, float max){
	    float i;
	    i = max / vector.len();
	    i = i < 1.0f ? i : 1.0f;
	    vector.scl(i);
	    return vector;
	}

}
/*
 * #include "../config/platform.h"
 * 
 * #if defined (OS_WINDOWS) # include <windows.h> #endif
 * 
 * #if defined(OS_MACOSX) # include <OpenGL/gl.h> #else # include <GL/gl.h>
 * #endif
 * 
 * #include "../app/globaldefs.h" #include "MoveComponent.h"
 * 
 * MoveComponent::MoveComponent(Character *c) {
 * 
 * parent = NULL;
 * 
 * velocity.set(0.0,0.0); accel.set(0.0,0.0); angularVelo = 0.0; angularAccel =
 * 0.0;
 * 
 * // init default maxima maxVelocity = MAX_VELOCITY; maxAccel = MAX_ACCEL;
 * maxAngularVelocity = MAX_ANGULAR_VELOCITY; maxAngularAccel =
 * MAX_ANGULAR_ACCEL;
 * 
 * // init steering posSteering = NULL; angularSteering = NULL;
 * 
 * attachToCharacter(c); active = true; }
 * 
 * void MoveComponent::attachToCharacter(Character *p) { parent = p;
 * p->addComponent(this);
 * 
 * }
 * 
 * void MoveComponent::update(double deltaTime) {
 * 
 * if (angularSteering != NULL) { angularSteering->apply(this); }
 * 
 * if (posSteering != NULL) { posSteering->apply(this); }
 * 
 * velocity += deltaTime * accel; if (velocity.getLength() > maxVelocity) {
 * velocity.normalize(); velocity *= maxVelocity; }
 * 
 * CVector position = parent->getPosition(); double rotation =
 * parent->getRotation();
 * 
 * position += PIXEL_PER_METER *deltaTime * velocity;
 * 
 * angularVelo += deltaTime * angularAccel; if (std::abs(angularVelo) >
 * maxAngularVelocity) { angularVelo = (angularVelo > 0.0)? maxAngularVelocity :
 * - maxAngularVelocity; } rotation += deltaTime * angularVelo;
 * 
 * if (rotation > 360.0) rotation -= 360.0; if (rotation < -360.0) rotation +=
 * 360.0;
 * 
 * if (position[0] > 1024) { position[0] -= 1024; } if (position[0] < 0) {
 * position[0] += 1024; } if (position[1] > 800) { position[1] -= 800; } if
 * (position[1] < 0) { position[1] += 800; }
 * 
 * parent->setPosition(position); parent->setRotation(rotation); }
 * 
 * void MoveComponent::setPosition(const CVector &vec)
 * {parent->setPosition(vec); } void MoveComponent::setVelocity(const CVector
 * &vec) {velocity = vec; CLAMP_VEC(velocity,maxVelocity); } void
 * MoveComponent::setAccel(const CVector &vec) {accel = vec;
 * CLAMP_VEC(accel,maxAccel); }
 * 
 * void MoveComponent::setRotation(double v) {parent->setRotation(v); } void
 * MoveComponent::setAngularVelocity(double v) {angularVelo = v;
 * CLAMP(-maxAngularVelocity,angularVelo,maxAngularVelocity);} void
 * MoveComponent::setAngularAccel(double v) {angularAccel = v;
 * CLAMP(-maxAngularAccel,angularVelo,maxAngularAccel);}
 * 
 * void MoveComponent::draw() {
 * 
 * glColor3f(1.0,1.0,1.0);
 * 
 * CVector position = parent->getPosition();
 * 
 * glMatrixMode(GL_MODELVIEW); glPushMatrix();
 * glTranslated(position[0],position[1],0.0); glDisable(GL_TEXTURE_2D);
 * 
 * if (debug) { glColor3f(1.0,0.0,0.0); glDisable(GL_TEXTURE_2D);
 * glBegin(GL_LINES); glVertex2d(0.0,0.0); glVertex2d(velocity[0],velocity[1]);
 * glEnd();
 * 
 * glColor3f(0.0,0.0,1.0); glDisable(GL_TEXTURE_2D); glBegin(GL_LINES);
 * glVertex2d(0.0,0.0); glVertex2d(accel[0],accel[1]); glEnd(); }
 * 
 * glPopMatrix();
 * 
 * if (debug) { if (posSteering != NULL) posSteering->debugDraw(this); if
 * (angularSteering != NULL) angularSteering->debugDraw(this); }
 * 
 * glColor4d(1.0,1.0,1.0,1.0); }
 */
