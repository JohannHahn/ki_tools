package com.mygdx.Entities;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.mygdx.components.BoidCenterComponent;
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
	public static int width = 8; 
	public static int height = 16;

	public BoidEntity(Team team, Engine engine, State state) {
		this.team = team;
		this.engine = engine;
		stateMachine = new DefaultStateMachine<BoidEntity>(this, state);

	}

	public void update(float delta) {
		stateMachine.update();
	}

	public void switchTeams() {
		if (team == Team.GREEN) {
			team = Team.RED;
		} else {
			team = Team.GREEN;
		}
	}

	// Gibt Gegner Position zur�ueck, die am naechersten ist, falls keiner im
	// Sichtfeld = null
	public Entity searchTarget() {
		ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
		Entity result = null;
		ImmutableArray<Entity> entities = engine.getEntities();
		float smallestDistance = sightRadius * sightRadius;

		for (Entity entity : entities) {
			if (entity.getComponent(BoidCenterComponent.class) == null)
				continue;

			BoidEntity currentBoid = (BoidEntity) entity;

			float newDistance = pm.get(this).position.dst2(pm.get(currentBoid).position);
			// Checke falls Gegner in Sicht => pursue Gegner
			if (this.team != currentBoid.team && newDistance < smallestDistance) {
				smallestDistance = newDistance;
				result = currentBoid;
			}
		}

		return result;
	}

	public ComponentMapper<PositionComponent> getPostionComponentMapper() {

		return ComponentMapper.getFor(PositionComponent.class);
	}

}
