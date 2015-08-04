package com.mygdx.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.components.PositionComponent;

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
/*#include "../config/platform.h"

#if defined (OS_WINDOWS)
# include <windows.h>
#endif

#if defined(OS_MACOSX)
# include <OpenGL/gl.h>
#else
# include <GL/gl.h>
#endif

#include "../app/globaldefs.h"
#include "MoveComponent.h"

MoveComponent::MoveComponent(Character *c) {
	
	parent = NULL;
	
	velocity.set(0.0,0.0);
	accel.set(0.0,0.0);
	angularVelo = 0.0;
	angularAccel = 0.0;

	// init default maxima
	maxVelocity        = MAX_VELOCITY;
	maxAccel           = MAX_ACCEL;
	maxAngularVelocity = MAX_ANGULAR_VELOCITY;
	maxAngularAccel    = MAX_ANGULAR_ACCEL;

	// init steering
	posSteering     = NULL;
	angularSteering = NULL;
	
	attachToCharacter(c);
	active = true;
}

void MoveComponent::attachToCharacter(Character *p) {
	parent = p;
	p->addComponent(this);
	
}

void  MoveComponent::update(double deltaTime) {
	
	if (angularSteering != NULL) {
		angularSteering->apply(this);
	}

	if (posSteering != NULL) {
		posSteering->apply(this);
	}

	velocity += deltaTime * accel;
	if (velocity.getLength() > maxVelocity) {
		velocity.normalize();
		velocity *= maxVelocity;
	}

	CVector position = parent->getPosition();
	double rotation = parent->getRotation();

	position += PIXEL_PER_METER *deltaTime * velocity;

	angularVelo += deltaTime * angularAccel;
	if (std::abs(angularVelo) > maxAngularVelocity) {
		angularVelo = (angularVelo > 0.0)? maxAngularVelocity : - maxAngularVelocity;
	}
	rotation += deltaTime * angularVelo;

	if (rotation > 360.0) rotation  -= 360.0;
	if (rotation < -360.0) rotation += 360.0;

	if (position[0] > 1024) {
		position[0] -= 1024;
	}
	if (position[0] < 0) {
		position[0] += 1024;
	}
	if (position[1] > 800) {
		position[1] -= 800;
	}
	if (position[1] < 0) {
		position[1] += 800;
	}

	parent->setPosition(position);
	parent->setRotation(rotation);
} 

void MoveComponent::setPosition(const CVector &vec)  {parent->setPosition(vec);  }
void MoveComponent::setVelocity(const CVector &vec)  {velocity = vec;  CLAMP_VEC(velocity,maxVelocity);  }
void MoveComponent::setAccel(const CVector &vec)     {accel = vec;  CLAMP_VEC(accel,maxAccel);  }

void MoveComponent::setRotation(double v)            {parent->setRotation(v); }	
void MoveComponent::setAngularVelocity(double v)     {angularVelo = v; CLAMP(-maxAngularVelocity,angularVelo,maxAngularVelocity);}
void MoveComponent::setAngularAccel(double v)        {angularAccel = v; CLAMP(-maxAngularAccel,angularVelo,maxAngularAccel);}

void MoveComponent::draw() {

	glColor3f(1.0,1.0,1.0);

	CVector position = parent->getPosition();

	glMatrixMode(GL_MODELVIEW);
	glPushMatrix();
	glTranslated(position[0],position[1],0.0);
	glDisable(GL_TEXTURE_2D);

	if (debug) {
		glColor3f(1.0,0.0,0.0);
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_LINES);
		glVertex2d(0.0,0.0);
		glVertex2d(velocity[0],velocity[1]);
		glEnd();

		glColor3f(0.0,0.0,1.0);
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_LINES);
		glVertex2d(0.0,0.0);
		glVertex2d(accel[0],accel[1]);
		glEnd();
	}

	glPopMatrix();

	if (debug) {
		if (posSteering != NULL) posSteering->debugDraw(this);
		if (angularSteering != NULL) angularSteering->debugDraw(this);
	}

	glColor4d(1.0,1.0,1.0,1.0);
}
*/
