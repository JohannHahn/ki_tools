package com.mygdx.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.Entities.BoidEntity.Team;
import com.mygdx.components.BoidCenterComponent;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.RessourceComponent;

public class RessourceSystem extends EntitySystem{
	
	private ImmutableArray<Entity> entities;
	private float elapsedTime = 0f;
	private float loseFuelTime = 1f;
	private int loseFuelAmount = 1;
	private int loseHealthAmount = 1;
	private boolean timeElapsed = false;
	private float collisionDistance = 15f;
	private ComponentMapper<RessourceComponent> rm = ComponentMapper.getFor(RessourceComponent.class);
	private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
	
	@SuppressWarnings("unchecked")
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(RessourceComponent.class, BoidCenterComponent.class, PositionComponent.class).get());
		System.out.println("RessourceSystem added");
	}
	
	public void update(float deltaTime) {
		elapsedTime += deltaTime;
		timeElapsed = elapsedTime >= loseFuelTime;
		
		for (int i = 0; i < entities.size(); ++i) {
			BoidEntity entity = (BoidEntity)entities.get(i);
			RessourceComponent rc = rm.get(entity);
			
			//Verliere Sprit jede Zeiteinheit
			if(timeElapsed){				
				rc.fuel -= loseFuelAmount;
			}
			
			//Verliere Leben, falls im grünen Team und Kontakt mit Gegner
			if(entity.team == Team.GREEN){
				Entity enemy = entity.searchTarget();
				
				if(enemy != null){
					PositionComponent pc = pm.get(entity);
					
					//Collision = Distanz < collisionDistance // dst2 für Performance
					if(pc.position.dst2(pm.get(enemy).position) < (collisionDistance * collisionDistance)){
						rc.health -= loseHealthAmount;
						if(rc.health <= 0){
							entity.engine.removeEntity(entity);
						}
					}
				}
			}
		}
		
		if(timeElapsed){
			elapsedTime %= loseFuelTime;
			timeElapsed = false;
		}
	}
}
