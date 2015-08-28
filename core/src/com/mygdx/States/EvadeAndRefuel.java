package com.mygdx.States;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.components.EvadeComponent;
import com.mygdx.components.RessourceComponent;

public class EvadeAndRefuel extends EvadeState{

    private ComponentMapper<EvadeComponent> pm = ComponentMapper.getFor(EvadeComponent.class);
    private EvadeComponent ec;
    ComponentMapper<RessourceComponent> rm = ComponentMapper.getFor(RessourceComponent.class);
    RessourceComponent rc;
    
    
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
        
        if(BoidState.checkFuel(boid)) {
        	sc.entityTarget = BoidState.getGlobalTarget(boid.engine);
        	boid.stateMachine.changeState(new EvadeState());
        }  
    }
    
    public void exit(BoidEntity boid){
        //boid.remove(EvadeComponent.class);
    }

}
