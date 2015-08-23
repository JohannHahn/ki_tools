package com.mygdx.Entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;


public class BoidEntity extends Entity {
	public enum Team {
	    RED, GREEN
	}
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
}
