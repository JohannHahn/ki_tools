package com.mygdx.Entities;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.Entities.BoidEntity.Team;
import com.mygdx.components.BoidCenterComponent;
import com.mygdx.components.EvadeComponent;
import com.mygdx.components.FleeComponent;
import com.mygdx.components.PursuitComponent;
import com.mygdx.components.RenderComponent;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.SeekComponent;
import com.mygdx.components.WanderComponent;

//Default state system
public enum BoidState implements State<BoidEntity> {	
	
	PURSUIT(){
		private ComponentMapper<PursuitComponent> pm = ComponentMapper.getFor(PursuitComponent.class);
		private PursuitComponent pc;
		@Override
		public void enter(BoidEntity boid) {
			Entity target = boid.searchTarget();
			if(target != null){
				pc = new PursuitComponent();
				pc.target = target;
				boid.add(pc);
				System.out.println("Entered State: PERSUIT");
			}else {
				boid.stateMachine.changeState(WANDER);
			}
			
			
		}			
		
		@Override		
        public void update(BoidEntity boid) {
			Entity target = boid.searchTarget();
			if(target != null){				
				pc = pm.get(boid);
				if(pc == null){
					pc = new PursuitComponent();
					boid.add(pc);
				}
				pc.target = target;				
			}
			else{
				boid.stateMachine.changeState(WANDER);
			}
			
			
        }
		
		public void exit(BoidEntity boid){
			boid.remove(PursuitComponent.class);
		}
	},
	
	EVADE(){
		private ComponentMapper<EvadeComponent> pm = ComponentMapper.getFor(EvadeComponent.class);
		private EvadeComponent ec;
		@Override
		public void enter(BoidEntity boid) {
			Entity target = boid.searchTarget();
			
			if(target != null){	
				ec = new EvadeComponent();				
				ec.target = target;
				boid.add(ec);		
				System.out.println("Entered State: EVADE");
			}
			
			updateTarget(boid);
		}
		
		@Override		
        public void update(BoidEntity boid) {
			Entity target = boid.searchTarget();
			ec = pm.get(boid);
			if(ec == null){
				ec = new EvadeComponent();
				boid.add(ec);
			}
			if(target != null){		
				ec.target = target;				
			}
			else{
				boid.remove(EvadeComponent.class);
			}
			
			updateTarget(boid);	
        }
		
		public void exit(BoidEntity boid){
			boid.remove(EvadeComponent.class);
		}
	},
	
	WANDER(){
		private ComponentMapper<WanderComponent> pm = ComponentMapper.getFor(WanderComponent.class);
		private WanderComponent wc;
		
		@Override
		public void enter(BoidEntity boid) {
			Entity target = boid.searchTarget();			
			if(target == null){
				wc = new WanderComponent();
				boid.add(wc);
				System.out.println("Entered State: WANDER");
			}
			else{
				boid.stateMachine.changeState(boid.team == Team.GREEN ? EVADE : PURSUIT);
			}
			
		}
	
		@Override		
	    public void update(BoidEntity boid) {
			Entity target = boid.searchTarget();
			if(target != null){				
				boid.stateMachine.changeState(boid.team == Team.GREEN ? EVADE : PURSUIT);				
			}
	    }
		
		public void exit(BoidEntity boid){
			boid.remove(WanderComponent.class);
		}
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
		
		if(Gdx.input.isKeyPressed(Keys.N)){
			boid.stateMachine.changeState(BoidState.NO_TARGET);
		}
	}
	
	public Vector2 mouseCoordinates(){
		return new Vector2(Gdx.input.getX(), Gdx.app.getGraphics().getHeight() - Gdx.input.getY());		
	}	
	
	//return: das aktuelle Ziel für alle grünen Boids, null falls keins
	@SuppressWarnings("unchecked")
	public static PointOfInterestEntity getGlobalTarget(Engine engine){
		ImmutableArray<Entity> candidates = engine.getEntitiesFor(Family.all(RenderComponent.class, PositionComponent.class).get());
		
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
			boid.setPointOfInterest(pie);
		}
	}
	
	public void updateTarget(BoidEntity boid){
		ComponentMapper<SeekComponent> sm = ComponentMapper.getFor(SeekComponent.class);
		SeekComponent sc = sm.get(boid);
		ComponentMapper<RenderComponent> rm = ComponentMapper.getFor(RenderComponent.class);
		ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
		
		if(sc == null)
			sc = new SeekComponent();
		if(sc.entityTarget == null){
			PointOfInterestEntity target = getGlobalTarget(boid.engine);
			
			if(target == null){
				target = PointOfInterestEntity.createTargetEntity(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				boid.engine.addEntity(target);
				setGlobalTarget(target, boid.engine);
			}
			
			sc.entityTarget = target;			
		}
		PositionComponent targetPosition = pm.get(sc.entityTarget);
		RenderComponent targetRender = rm.get(sc.entityTarget);
		sc.target = targetPosition.position;
		
	}
	

}
