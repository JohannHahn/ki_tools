package com.mygdx.Entities;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.components.BoidCenterComponent;
import com.mygdx.components.FleeComponent;
import com.mygdx.components.SeekComponent;

public enum BoidState implements State<BoidEntity> {
	
	SEEKING(){
		private ComponentMapper<SeekComponent> sm = ComponentMapper.getFor(SeekComponent.class);
		private SeekComponent sc;
		@Override
		public void enter(BoidEntity boid) {
			sc = sm.get(boid);
			if(sc != null){
				sc.target = mouseCoordinates();
				boid.target = mouseCoordinates();
			}
			System.out.println("Entered State: SEEK");
		}			
		
		@Override		
        public void update(BoidEntity boid) {
			sc = sm.get(boid);
			if(sc != null){
				sc.target = mouseCoordinates();
				boid.target = mouseCoordinates();
			}
			checkInput(boid);
			
        }
	},
	
	NO_TARGET(){		
		
		@Override
		public void enter(BoidEntity boid) {
			System.out.println("Entered State: " + boid.stateMachine.getCurrentState());
			boid.target = boid.getComponent(BoidCenterComponent.class).vectorCenter;
		}
		
		@Override
        public void update(BoidEntity boid) {
			checkInput(boid);
			boid.target = boid.getComponent(BoidCenterComponent.class).vectorCenter;
        }
	},	
	
	FLEEING(){
		private ComponentMapper<FleeComponent> fm = ComponentMapper.getFor(FleeComponent.class);
		private FleeComponent fc;
		@Override
		public void enter(BoidEntity boid) {		
			fc = fm.get(boid);
			boid.target = null;
			if(fc != null){
				fc.target = mouseCoordinates();				
			}
			System.out.println("Entered State: FLEE");			
		}	
		
		@Override		
        public void update(BoidEntity boid) {			
			fc = fm.get(boid);
			if(fc != null){
				fc.target = mouseCoordinates();
			}
			checkInput(boid);
			
        }
	}
	;
	
	
	
	@Override
	public void enter(BoidEntity boid) {
	}	

	@Override
	public void exit(BoidEntity boid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMessage(BoidEntity boid, Telegram telegram) {
		System.out.println(telegram);
		return false;
	}



	@Override
	public void update(BoidEntity boid) {
		checkInput(boid);
	}
	
	public void checkInput(BoidEntity boid){
		ComponentMapper<SeekComponent> sm = ComponentMapper.getFor(SeekComponent.class);
		ComponentMapper<FleeComponent> fm = ComponentMapper.getFor(FleeComponent.class);
		
		if(Gdx.input.isKeyPressed(Keys.N)){
			boid.stateMachine.changeState(BoidState.NO_TARGET);
		}
		
		if(Gdx.input.isKeyPressed(Keys.S) && sm.get(boid) != null){
			boid.stateMachine.changeState(BoidState.SEEKING);
		}
		
		if(Gdx.input.isKeyPressed(Keys.F) && fm.get(boid) != null){
			boid.stateMachine.changeState(BoidState.FLEEING);
		}
	}
	
	public Vector2 mouseCoordinates(){
		return new Vector2(Gdx.input.getX(), Gdx.app.getGraphics().getHeight() - Gdx.input.getY());		
	}

}
