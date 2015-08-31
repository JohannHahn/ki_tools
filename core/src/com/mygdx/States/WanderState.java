package com.mygdx.States;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.Entities.BoidEntity.Team;
import com.mygdx.components.SeekComponent;
import com.mygdx.components.WanderComponent;

public class WanderState extends BoidState{

    private ComponentMapper<WanderComponent> pm = ComponentMapper.getFor(WanderComponent.class);
    private WanderComponent wc;
    
    @Override
    public void enter(BoidEntity boid) {
        Entity target = boid.searchTarget();            
        if(target == null){
            wc = new WanderComponent();
            boid.add(wc);
        }
        else{
            boid.stateMachine.changeState(boid.team == Team.GREEN ? new EvadeState() : new PursuitState());
        }
        
    }

    @Override       
    public void update(BoidEntity boid) {
        Entity target = boid.searchTarget();
        if(target != null){             
            boid.stateMachine.changeState(boid.team == Team.GREEN ? new EvadeState() : new PursuitState());               
        }
        
        if(boid.getComponent(WanderComponent.class) == null)
        {
        	boid.add(new WanderComponent());
        }
        
        if(BoidState.checkFuel(boid)){
        	boid.remove(SeekComponent.class);
        }
    }
    
    public void exit(BoidEntity boid){
        boid.remove(WanderComponent.class);
    }

   

    @Override
    public boolean onMessage(BoidEntity entity, Telegram telegram) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getName() {
        return "wanderState";
    }

}
