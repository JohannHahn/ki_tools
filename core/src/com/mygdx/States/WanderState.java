package com.mygdx.States;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.Entities.BoidEntity.Team;
import com.mygdx.components.WanderComponent;

public class WanderState implements State<BoidEntity>{

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
        BoidState.checkFuel(boid);
    }
    
    public void exit(BoidEntity boid){
        boid.remove(WanderComponent.class);
    }

   

    @Override
    public boolean onMessage(BoidEntity entity, Telegram telegram) {
        // TODO Auto-generated method stub
        return false;
    }

}
