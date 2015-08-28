package com.mygdx.States;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.Entities.BoidEntity.Team;
import com.mygdx.Entities.PointOfInterestEntity;
import com.mygdx.components.BoidCenterComponent;
import com.mygdx.components.RenderComponent;
import com.mygdx.components.RessourceComponent;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.SeekComponent;

//Default state system
public abstract class BoidState implements State<BoidEntity>, IState {		
	
	public static PointOfInterestEntity globalTarget;
	
	public static Vector2 mouseCoordinates(){
		return new Vector2(Gdx.input.getX(), Gdx.app.getGraphics().getHeight() - Gdx.input.getY());		
	}	
	
	//return: das aktuelle Ziel fГјr alle grГјnen Boids, null falls keins
	@SuppressWarnings("unchecked")
	public static PointOfInterestEntity getGlobalTarget(Engine engine){
		ImmutableArray<Entity> candidates = engine.getEntities();
		
		for(Entity e : candidates){
			if(e.getClass() == PointOfInterestEntity.class){
				PointOfInterestEntity pie = (PointOfInterestEntity)e;
				if(pie.toString() == "target"){
					return pie;
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static void setGlobalTarget(PointOfInterestEntity pie, Engine engine){
		
		ImmutableArray<Entity> boids = engine.getEntitiesFor(Family.all(BoidCenterComponent.class).get());
		
		for(Entity b : boids){
			BoidEntity boid = (BoidEntity)b;
			if(boid.team == Team.GREEN)
				boid.setPointOfInterest(pie);
		}
	}
	
	public static void updateTarget(BoidEntity boid){
		ComponentMapper<SeekComponent> sm = ComponentMapper.getFor(SeekComponent.class);
		SeekComponent sc = sm.get(boid);
		ComponentMapper<RenderComponent> rm = ComponentMapper.getFor(RenderComponent.class);
		ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);	
		
		if(sc == null)
			sc = new SeekComponent();
		if(sc.entityTarget == null){
			
			if(globalTarget == null){
				globalTarget = getGlobalTarget(boid.engine);
			}
			PointOfInterestEntity target = getGlobalTarget(boid.engine);
			
			if(globalTarget == null){
				globalTarget = PointOfInterestEntity.createTargetEntity(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				boid.engine.addEntity(globalTarget);
				setGlobalTarget(globalTarget, boid.engine);
			}		
		}
	}
	
	//immer false außer, falls grade vollgetankt
	public static boolean checkFuel(BoidEntity boid){
		ComponentMapper<SeekComponent> sm = ComponentMapper.getFor(SeekComponent.class);
		SeekComponent sc = sm.get(boid);
		ComponentMapper<RessourceComponent> rm = ComponentMapper.getFor(RessourceComponent.class);
		RessourceComponent rc = rm.get(boid);
		if(!rc.lowOnFuel){
			
			if(rc.fuel < rc.fuelThreshold) {
				rc.lowOnFuel = true;				
				//Suche nach Tankstelle in Entities
				PointOfInterestEntity tanke;
				
				for(Entity e : boid.engine.getEntities()){
					
					if(e.getClass() == PointOfInterestEntity.class){						
						tanke = (PointOfInterestEntity)e;
						
						if(tanke.toString().contains("Tankstelle")){
						    //Checke ob passende Tankstelle
						    if( (tanke.toString().contains("Green") && boid.team == Team.GREEN) ||
						        (tanke.toString().contains("Red") && boid.team == Team.RED) ) {
						        boid.setPointOfInterest(tanke);
						        break;
						    }							
							
						}
					}
				}
			}
			
		}
		
		else {
			if(rc.fuel == 100){
				rc.lowOnFuel = false;
				return true;
			}
		}
		
		return false;
	}
	

}
