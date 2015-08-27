package com.mygdx.States;

import org.luaj.vm2.LuaValue;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.Script.LuaScript;

public class LuaState extends BoidState{
	private LuaScript script;
	private String name = "null";
    public LuaState(LuaScript script){
        this.script = script;
        //Supoptimal muss gaendert werden 
        LuaValue nameValue = script.callForReturn("setName", name);
        name = nameValue.tojstring();
    }
	

	@Override
	public void enter(BoidEntity entity) {		
		System.out.println("Entered" +  name);
	}

	@Override
	public void update(BoidEntity entity) {
		script.executeFunction("update", entity);		
	}

	@Override
	public void exit(BoidEntity entity) {		
		script.executeFunction("exit", entity);
	}

	@Override
	public boolean onMessage(BoidEntity entity, Telegram telegram) {
		// TODO Auto-generated method stub
		return false;
	}
	public String getName()
	{return name;}
	public void setName(String name)
	{this.name=name;}
}
