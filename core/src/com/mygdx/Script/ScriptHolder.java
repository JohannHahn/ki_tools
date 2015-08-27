package com.mygdx.Script;

import java.util.ArrayList;

import javax.swing.JFileChooser;

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

	public static int size() {
		
		return scriptStatesList.size();
	}
	public static String getName(int index)
	{
		return scriptStatesList.get(index).getName();
		
	}
	public static LuaState getLuaState(int index)
	{
		return scriptStatesList.get(index);
	}
	public LuaState getLuaStateByName(String name)
	{
		
		
		return getLuaState(getIndexOfScript(name));
		
	}
	
	public static void insertScript(String realtiveScriptPath)
	{
		scriptStatesList.add(new LuaState(new LuaScript(realtiveScriptPath)));
	}

	public static void addScript(int index) {
		// Create a file chooser
		final JFileChooser fc = new JFileChooser();

		// In response to a button click:
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// Pfad ANpassen
			String path;
			String absoulutPath = fc.getSelectedFile().getAbsolutePath();
			int cut = absoulutPath.indexOf("assets\\") + 7;// "assets//"=8
			path = absoulutPath.substring(cut);
			path = path.replace("\\", "/");

			// Create LuaScript&LuaState
			LuaScript newScript = new LuaScript(path);
			if (newScript.canExecute())
				ScriptHolder.scriptStatesList.set(index, new LuaState(newScript));
		}

	}
	public static void addScript() {
		// Create a file chooser
		final JFileChooser fc = new JFileChooser();

		// In response to a button click:
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			// Pfad ANpassen
			String path;
			String absoulutPath = fc.getSelectedFile().getAbsolutePath();
			int cut = absoulutPath.indexOf("assets\\") + 7;// "assets//"=8
			path = absoulutPath.substring(cut);
			path = path.replace("\\", "/");

			// Create LuaScript&LuaState
			LuaScript newScript = new LuaScript(absoulutPath);
			if (newScript.canExecute())
				ScriptHolder.scriptStatesList.add(new LuaState(newScript));
		}

	}
}
