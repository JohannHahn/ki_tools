package com.mygdx.Script;

import java.util.ArrayList;

import com.mygdx.Entities.LuaState;

public class ScriptHolder {

	public static ArrayList<LuaState> scriptStatesList= new ArrayList<LuaState>();

	//return indexofScript in ScripStateList
	public int getIndexOfScript(String name)
	{
		for (LuaState luaState : scriptStatesList) {
			if(luaState.getName().equals(name))
				return scriptStatesList.indexOf(luaState);
		} 
		return -1;
	}
	
	
}
