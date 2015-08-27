package com.mygdx.States;

import com.badlogic.ashley.core.ComponentMapper;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.components.EvadeComponent;
import com.mygdx.components.RessourceComponent;

public class EvadeAndRefuel extends EvadeState {

    private ComponentMapper<EvadeComponent> pm = ComponentMapper.getFor(EvadeComponent.class);
    private EvadeComponent ec;
    ComponentMapper<RessourceComponent> rm = ComponentMapper.getFor(RessourceComponent.class);
    RessourceComponent rc;
    
    
    @Override
    public void enter(BoidEntity boid) {
        super.enter(boid);          
        
        if(!rc.lowOnFuel) {
            boid.stateMachine.changeState(new EvadeState());
        }
        BoidState.checkFuel(boid);
        
    }
    
    @Override       
    public void update(BoidEntity boid) {
        super.update(boid);          
        
        if(!rc.lowOnFuel) {
            boid.stateMachine.changeState(new EvadeState());
        }
        BoidState.checkFuel(boid);
    }
    
    public void exit(BoidEntity boid){
        //boid.remove(EvadeComponent.class);
    }

}
