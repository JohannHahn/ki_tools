package com.mygdx.States;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.components.PursuitComponent;

public class PursuitState extends BoidState{

    private ComponentMapper<PursuitComponent> pm = ComponentMapper.getFor(PursuitComponent.class);
    private PursuitComponent pc;
    @Override
    public void enter(BoidEntity boid) {
        try{
        Entity target = boid.searchTarget();
        if(target != null){
            pc = new PursuitComponent();
            pc.target = target;
            boid.add(pc);
        }else {
            boid.stateMachine.changeState(new WanderState());
        }
        }catch (Exception e){
            System.out.println(e);
        }
       
    }           
    
    @Override       
    public void update(BoidEntity boid) {
        try{
        Entity target = boid.searchTarget();
        if(target != null){
            pc = new PursuitComponent();
            pc.target = target;
            boid.add(pc);
        }else {
            boid.stateMachine.changeState(new WanderState());
        }
        BoidState.checkFuel(boid);
        }catch (Exception e){
            System.out.println(e);
        }
        
       
    }
    
    public void exit(BoidEntity boid){
        boid.remove(PursuitComponent.class);
    }

    @Override
    public boolean onMessage(BoidEntity entity, Telegram telegram) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "pursuitState";
    }

}
