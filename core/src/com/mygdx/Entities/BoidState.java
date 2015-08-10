package com.mygdx.Entities;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.components.FleeComponent;
import com.mygdx.components.SeekComponent;

public enum BoidState implements State<BoidEntity> {
	
	NO_TARGET(){
		private ComponentMapper<SeekComponent> sm = ComponentMapper.getFor(SeekComponent.class);
		private ComponentMapper<FleeComponent> fm = ComponentMapper.getFor(FleeComponent.class);
		
		@Override
		public void enter(BoidEntity boid) {
			System.out.println("Entered State: " + boid.stateMachine.getCurrentState());
		}
		
		@Override
        public void update(BoidEntity boid) {
			
			if(Gdx.input.isKeyPressed(Keys.F) && fm.get(boid) != null){
				boid.stateMachine.changeState(BoidState.FLEEING);
			}
			
			if(Gdx.input.isKeyPressed(Keys.S) && sm.get(boid) != null){
				boid.stateMachine.changeState(BoidState.SEEKING);
			}
        }
	},
	
	SEEKING(){
		private ComponentMapper<SeekComponent> sm = ComponentMapper.getFor(SeekComponent.class);
		private ComponentMapper<FleeComponent> fm = ComponentMapper.getFor(FleeComponent.class);
		
		@Override
		public void enter(BoidEntity boid) {
			SeekComponent sc = sm.get(boid);
			sc.target = new Vector2(Gdx.input.getX(), Gdx.app.getGraphics().getHeight() - Gdx.input.getY());
			System.out.println("Entered State: SEEK");
		}			
		
		@Override		
        public void update(BoidEntity boid) {
			sm.get(boid).target = new Vector2(Gdx.input.getX(), Gdx.app.getGraphics().getHeight() - Gdx.input.getY());
			
			if(Gdx.input.isKeyPressed(Keys.N)){
				boid.stateMachine.changeState(BoidState.NO_TARGET);
			}
			
			if(Gdx.input.isKeyPressed(Keys.F) && fm.get(boid) != null){
				boid.stateMachine.changeState(BoidState.FLEEING);
			}
        }
	},
	
	FLEEING(){
		private ComponentMapper<SeekComponent> sm = ComponentMapper.getFor(SeekComponent.class);
		private ComponentMapper<FleeComponent> fm = ComponentMapper.getFor(FleeComponent.class);
		
		@Override
		public void enter(BoidEntity boid) {			
			fm.get(boid).target = new Vector2(Gdx.input.getX(), Gdx.app.getGraphics().getHeight() - Gdx.input.getY());
			System.out.println("Entered State: FLEE");			
		}	
		
		@Override		
        public void update(BoidEntity boid) {
			fm.get(boid).target = new Vector2(Gdx.input.getX(), Gdx.app.getGraphics().getHeight() - Gdx.input.getY());
			
			if(Gdx.input.isKeyPressed(Keys.N)){
				boid.stateMachine.changeState(BoidState.NO_TARGET);
			}
			
			if(Gdx.input.isKeyPressed(Keys.S) && sm.get(boid) != null){
				boid.stateMachine.changeState(BoidState.SEEKING);
			}
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
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void update(BoidEntity boid) {
		
	}

}
