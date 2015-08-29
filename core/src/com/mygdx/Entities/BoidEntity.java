package com.mygdx.Entities;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.Script.ScriptHolder;
import com.mygdx.States.BoidState;
import com.mygdx.components.BoidCenterComponent;
import com.mygdx.components.EvadeComponent;
import com.mygdx.components.FleeComponent;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.PursuitComponent;
import com.mygdx.components.RenderComponent;
import com.mygdx.components.RessourceComponent;
import com.mygdx.components.SeekComponent;

import com.mygdx.components.VelocityComponent;
import com.mygdx.components.WanderComponent;

public class BoidEntity extends Entity {
	public enum Team {
		RED, GREEN
	}

	public static final int sightRadius = 200;
	public Team team;
	public StateMachine<BoidEntity> stateMachine;
	public Engine engine;
	public boolean enemyInSight = false;
	public static int width = 8; 
	public static int height = 16;
	private boolean gotHit = false;
	

	public BoidEntity(Team team, Engine engine, BoidState state) {
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
		float collisionDistance = 30f;

		for (Entity entity : entities) {
			//If reached target -> set a new target
			if(entity.getClass() == PointOfInterestEntity.class){
				PointOfInterestEntity pie = (PointOfInterestEntity)entity;
				if(team == Team.GREEN && pie.toString() == "target" && pm.get(pie).position.dst2(pm.get(this).position) <= (collisionDistance * collisionDistance)){
					pm.get(pie).position.set(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));
				}
			}
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

	public ComponentMapper<PositionComponent> getPositionComponentMapper() {

		return ComponentMapper.getFor(PositionComponent.class);
	}
	
	public void setPointOfInterest(PointOfInterestEntity e){
		SeekComponent sc = ComponentMapper.getFor(SeekComponent.class).get(this);
		
		if(sc == null){			
			sc = new SeekComponent();
			this.add(sc);
		}
		
		sc.entityTarget = e;
		sc.target = getPositionComponentMapper().get(e).position;
	}
	

	
	//LUA Methodes:
	
	
	public void changeStateByName(String state)

	{
		//example state==EVADE

	try {this.stateMachine.changeState(ScriptHolder.getLuaStateByName(state));
		
	} catch (Exception e) {
		System.out.println("Kein State gefunden.");
	}
			
		
	}
	public void setTexture(String path)
	{
		getComponent(RenderComponent.class).setTexture(new Texture(Gdx.files.external(path)));
	}
	public void setRealtivPostion(float x,float y)
	{
		System.out.println(x +"/" + y);
		PositionComponent pC=getComponent(PositionComponent.class);
		
		pC.position.set(x, y);
		
		System.out.println(pC.position);
	}



	
	
	public boolean gotHit(){
		
		if(this.gotHit){
			this.gotHit = false;
			return true;
		}
		return false;	
	}
	
	public void setHit(boolean b) {
		this.gotHit = b;
	}
	
	public void addComponent(String name){
		if(name.contains("Evade")) {
			this.add(new EvadeComponent());
		}
		
		if(name.contains("Wander")) {
			this.add(new WanderComponent());
		}
		
		if(name.contains("Pursuit")) {
			this.add(new PursuitComponent());
		}
	}
	
	public void removeComponent(String name){
		if(name.contains("Evade")) {
			this.remove(EvadeComponent.class);
		}
		
		if(name.contains("Wander")) {
			this.remove(WanderComponent.class);
		}
		
		if(name.contains("Pursuit")) {
			this.remove(PursuitComponent.class);
		}
		
		if(name.contains("Seek")) {
			this.remove(SeekComponent.class);
		}
		
		if(name.contains("Flee")) {
			this.remove(FleeComponent.class);
		}
		
		
	}
	
	public boolean checkFuel(){
		return BoidState.checkFuel(this);
	}
	
	public PointOfInterestEntity getGlobalTarget(Engine engine){
		return BoidState.getGlobalTarget(engine);
	}
	
	//Sets target for pursuit or evade behaviors
	public boolean setTarget(Entity target, String action){
		if(target == null){
			return false;
			
		} else{
			
			if(action.contains("Evade")){
				ComponentMapper<EvadeComponent> em = ComponentMapper.getFor(EvadeComponent.class);
				EvadeComponent ec = em.get(this);			
				if(ec == null){
					ec = new EvadeComponent();
				}
				ec.target = target;
				this.add(ec);
				return true;				
			}
			
			else if(action.contains("Pursuit")){
				ComponentMapper<PursuitComponent> pm = ComponentMapper.getFor(PursuitComponent.class);
				PursuitComponent pc = pm.get(this);
				
				if(pc == null){
					pc = new PursuitComponent();
				}
				pc.target = target;
				this.add(pc);
				return true;
			}
			return false;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean gotComponent(String name){
		
		Class[] classes = {WanderComponent.class, EvadeComponent.class, PursuitComponent.class, SeekComponent.class, FleeComponent.class};
		for(Class c : classes){
			if(c.getName().contains(name)){
				return this.getComponent(c) != null;
			}
		}
		return false;
	}
	
	public void resetRessources(){
		ComponentMapper<RessourceComponent> rm = ComponentMapper.getFor(RessourceComponent.class);
		RessourceComponent rc = rm.get(this);
		
		rc.fuel = 100;
		rc.health = 100;
	}

}
