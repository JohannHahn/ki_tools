package com.mygdx.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.components.FleeComponent;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.SeekComponent;
import com.mygdx.components.VelocityComponent;

public class MovementSystem extends EntitySystem {

	private static final float OPTIMAL_BOID_DISTANCE = 20;

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
		Vector2 vectorBoidMatchVC = calculateVectorBoidMatchVc();
		Vector2 vectorSeekorFlee = calculateVectorSeekFlee(entity, position);		
	}

	private Vector2 calculateVectorSeekFlee(Entity entity, PositionComponent positionComp) {
		Vector2 result = new Vector2();
		Vector2 position = positionComp.position;
		Vector2 target = new Vector2(Gdx.input.getX(), Gdx.app.getGraphics().getHeight() - Gdx.input.getY());
		SeekComponent seekComp = sm.get(entity);
		FleeComponent fleeComp = fm.get(entity);
		VelocityComponent velComp = vm.get(entity);
		if(seekComp != null){
			//Der Maus folgen			
			Vector2 desired_velocity = target.sub(position).nor().scl(velComp.maxVelocity);
			Vector2 steering = desired_velocity.sub(velComp.vectorVelocity);
		    steering.clamp(0, velComp.maxForce);
			//steering = steering / mass
		    result = steering;
		    pm.get(entity).position.x += result.x;
			pm.get(entity).position.y += result.y;
		}
		else if(fleeComp != null){						
			Vector2 desired_velocity = target.sub(position).nor().scl(velComp.maxVelocity * -1);
			Vector2 steering = desired_velocity.sub(velComp.vectorVelocity);
		    steering.clamp(0, velComp.maxForce);
			//steering = steering / mass
		    result = steering;
		    pm.get(entity).position.x += result.x;
			pm.get(entity).position.y += result.y;
		}
		//TODO:ARRIVAL
		return result;
	}

	private Vector2 calculateVectorBoidMatchVc() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// DONE
	private Vector2 calculateVectorBoidDistance(Entity entity, PositionComponent position) {
		
		Vector2 result = new Vector2();
		Vector2 positionVectorBoid = position.position;
		boolean xDistanceToSmall=false;
		boolean yDistanceToSmall=false;
		float percentNearing = 70;
		
		
		for (int i = 0; i < entities.size(); ++i) {
			if (!entity.equals(entities.get(i))) {
				
				xDistanceToSmall=(OPTIMAL_BOID_DISTANCE*(-1))<(pm.get(entities.get(i)).position.x-positionVectorBoid.x) && (pm.get(entities.get(i)).position.x-positionVectorBoid.x)<OPTIMAL_BOID_DISTANCE;
				yDistanceToSmall=(OPTIMAL_BOID_DISTANCE*(-1))<(pm.get(entities.get(i)).position.y-positionVectorBoid.y) && (pm.get(entities.get(i)).position.y-positionVectorBoid.y)<OPTIMAL_BOID_DISTANCE;
				
				if(xDistanceToSmall){
					result.sub(pm.get(entities.get(i)).position.x-positionVectorBoid.x,0);
				}
				if(yDistanceToSmall)
				{
					result.sub(0, pm.get(entities.get(i)).position.y-positionVectorBoid.y);
				}

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
		
		
		for (int i = 0; i < entities.size(); ++i) {
			if (!entity.equals(entities.get(i))) {

				result.add(pm.get(entities.get(i)).position.x, pm.get(entities.get(i)).position.y);


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
