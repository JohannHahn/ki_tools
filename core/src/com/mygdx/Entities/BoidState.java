package com.mygdx.Entities;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.components.EvadeComponent;
import com.mygdx.components.FleeComponent;
import com.mygdx.components.PursuitComponent;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.SeekComponent;

public enum BoidState implements State<BoidEntity> {	
	
	PURSUIT(){
		private ComponentMapper<PursuitComponent> pm = ComponentMapper.getFor(PursuitComponent.class);
		private PursuitComponent pc;
		@Override
		public void enter(BoidEntity boid) {
			Entity target = searchTarget(boid);
			if(target != null){
				pc = new PursuitComponent();
				pc.target = target;
				boid.add(pc);
			}
			
			System.out.println("Entered State: PERSUIT");
		}			
		
		@Override		
        public void update(BoidEntity boid) {
			Entity target = searchTarget(boid);
			if(target != null){				
				pc = pm.get(boid);
				if(pc == null){
					pc = new PursuitComponent();
					boid.add(pc);
				}
				pc.target = target;				
			}			
        }
		
		public void exit(BoidEntity boid){
			boid.remove(PursuitComponent.class);
		}
	},
	
	EVADE(){
		private ComponentMapper<EvadeComponent> pm = ComponentMapper.getFor(EvadeComponent.class);
		private EvadeComponent ec;
		private ComponentMapper<SeekComponent> sm = ComponentMapper.getFor(SeekComponent.class);
		private SeekComponent sc;
		@Override
		public void enter(BoidEntity boid) {
			Entity target = searchTarget(boid);
			
			sc = new SeekComponent();	
			sc.target = mouseCoordinates();
			boid.add(sc);
			
			if(target != null){
				ec = new EvadeComponent();
				ec.target = target;
				boid.add(ec);
			}			
			System.out.println("Entered State: EVADE");
		}
		
		@Override		
        public void update(BoidEntity boid) {
			Entity target = searchTarget(boid);
			if(target != null){				
				ec = pm.get(boid);
				if(ec == null){
					ec = new EvadeComponent();
					boid.add(ec);
				}
				ec.target = target;		
				
				//Update Seek target
				sc = sm.get(boid);
				sc.target = mouseCoordinates();		
			}			
        }
		
		public void exit(BoidEntity boid){
			boid.remove(EvadeComponent.class);
		}
	},
	
	WANDER(){
		
	},
	
	NO_TARGET(){		
		@Override
		public void enter(BoidEntity boid) {
			System.out.println("Entered State: " + boid.stateMachine.getCurrentState());
		}
		
		@Override
        public void update(BoidEntity boid) {
			checkInput(boid);		
        }
	};
	
	
	public static int sightRadius = 150;
	
	@Override
	public void enter(BoidEntity boid) {
		System.out.println("enter");
	}	

	@Override
	public void exit(BoidEntity boid) {
		System.out.println("exit");
		
	}

	@Override
	public boolean onMessage(BoidEntity boid, Telegram telegram) {
		System.out.println("message");
		return false;
	}



	@Override
	public void update(BoidEntity boid) {
		System.out.println("update");
	}
	
	public void checkInput(BoidEntity boid){
		ComponentMapper<SeekComponent> sm = ComponentMapper.getFor(SeekComponent.class);
		ComponentMapper<FleeComponent> fm = ComponentMapper.getFor(FleeComponent.class);
		
		if(Gdx.input.isKeyPressed(Keys.N)){
			boid.stateMachine.changeState(BoidState.NO_TARGET);
		}
	}
	
	public Vector2 mouseCoordinates(){
		return new Vector2(Gdx.input.getX(), Gdx.app.getGraphics().getHeight() - Gdx.input.getY());		
	}
	
	//Gibt Gegner Position zurück, die am nähersten ist, falls keiner im Sichtfeld = null
	public static Entity searchTarget(BoidEntity boid){
		ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
		Entity result = null;
		
		ImmutableArray<Entity> entities = boid.engine.getEntities();			
		float smallestDistance = sightRadius * sightRadius;
		
		for(Entity entity : entities){			
			BoidEntity currentBoid = (BoidEntity)entity;
			float newDistance = pm.get(boid).position.dst2(pm.get(currentBoid).position);
			//Checke falls Gegner in Sicht => pursue Gegner
			if(boid.team != currentBoid.team && newDistance < smallestDistance){
				smallestDistance = newDistance;
				result = currentBoid;
			}
		}
		
		return result;
	}

}