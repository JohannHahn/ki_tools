package com.mygdx.States;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.Telegram;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.components.EvadeComponent;
import com.mygdx.components.RessourceComponent;
import com.mygdx.components.SeekComponent;

public class EvadeState extends BoidState{   

    private ComponentMapper<EvadeComponent> pm = ComponentMapper.getFor(EvadeComponent.class);
    private EvadeComponent ec;
    ComponentMapper<RessourceComponent> rm = ComponentMapper.getFor(RessourceComponent.class);
    RessourceComponent rc;
    ComponentMapper<SeekComponent> sm = ComponentMapper.getFor(SeekComponent.class);
	SeekComponent sc;
    
    @Override
    public void enter(BoidEntity boid) {
        Entity target = boid.searchTarget();
        rc = rm.get(boid);
        if(target != null){ 
            ec = new EvadeComponent();              
            ec.target = target;
            boid.add(ec);       
        } 
        
    }
    
    @Override       
    public void update(BoidEntity boid) {
        Entity target = boid.searchTarget();
        ec = pm.get(boid);
        rc = rm.get(boid);
        sc = sm.get(boid);
        
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
        
        BoidState.updateTarget(boid);        
        BoidState.checkFuel(boid);
        
        if(rc.lowOnFuel){
            boid.stateMachine.changeState(new EvadeAndRefuel());
        }
        
    }
    
    public void exit(BoidEntity boid){
        boid.remove(EvadeComponent.class);
    }

    @Override
    public boolean onMessage(BoidEntity entity, Telegram telegram) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getName() {
        return "evadeState";
    }

}
