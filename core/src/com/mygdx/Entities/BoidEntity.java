package com.mygdx.Entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.math.Vector2;


public class BoidEntity extends Entity {
	public enum Team {
	    RED, GREEN
	}
	public Team team;
	public StateMachine<BoidEntity> stateMachine;
	
	public BoidEntity(Team team)
	{
		this.team= team;
		stateMachine = new DefaultStateMachine<BoidEntity>(this, BoidState.SEEKING);
		
	}
	
	public void update (float delta){
		stateMachine.update();
	}
}
