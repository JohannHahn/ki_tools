package com.mygdx.Entities;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.mygdx.components.PositionComponent;


public class BoidEntity extends Entity {
	public enum Team {
	    RED, GREEN
	}
	private static final int sightRadius = 150;
	public Team team;
	public StateMachine<BoidEntity> stateMachine;
	public Engine engine;
	public boolean enemyInSight = false;
	
	public BoidEntity(Team team, Engine engine, State state)
	{
		this.team= team;
		this.engine = engine;
		stateMachine = new DefaultStateMachine<BoidEntity>(this, state);	
		
	}
	
	public void update (float delta){
		stateMachine.update();
	}
	public void setState(String state)
	{ System.out.println(state);
	
	}
	public Entity searchTarget(){
		ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	
		Entity result = null;
		
		ImmutableArray<Entity> entities = this.engine.getEntities();			
		float smallestDistance = sightRadius * sightRadius;
		
		for(Entity entity : entities){			
			BoidEntity currentBoid = (BoidEntity)entity;
			float newDistance = pm.get(this).position.dst2(pm.get(currentBoid).position);
			//Checke falls Gegner in Sicht => pursue Gegner
			if(this.team != currentBoid.team && newDistance < smallestDistance){
				smallestDistance = newDistance;
				result = currentBoid;
			}
		}
		
		return result;
		}
}
