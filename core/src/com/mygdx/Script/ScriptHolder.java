package com.mygdx.Script;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.luaj.vm2.ast.Str;

import com.badlogic.gdx.ai.fsm.State;
import com.mygdx.States.BoidState;
import com.mygdx.States.EvadeState;
import com.mygdx.States.LuaState;
import com.mygdx.States.PursuitState;
import com.mygdx.States.WanderState;

public class ScriptHolder {

	private static final String SCRIPTPATH_EVADE     = "data/scripts/Evade.lua";
	private static final String SCRIPTPATH_PURSUIT   = "data/scripts/PursuitBehaviour.lua";
	private static final String SCRIPTPATH_NO_TARGET = "data/scripts/No_Target.lua";
	private static final String SCRIPTPATH_WANDER    = "data/scripts/Wander.lua";
	
	public static ArrayList<BoidState> scriptStatesList= new ArrayList<BoidState>();

	//return indexofScript in ScripStateList
	public static int getIndexOfScript(String name)
	{
		for (BoidState BoidState : scriptStatesList) {
			if(BoidState.getName().equals(name))
				return scriptStatesList.indexOf(BoidState);
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
	public static BoidState getBoidState(int index)
	{
		return scriptStatesList.get(index);
	}
	public static BoidState getLuaStateByName(String name)
	{
		
		
		return getBoidState(getIndexOfScript(name));
		
	}
	
	public static void insertScript(String realtiveScriptPath)
	{
		scriptStatesList.add(new LuaState(new LuaScript(realtiveScriptPath)));
	}

	public static void addScript(int index) {
		// Create a file chooser
		final JFileChooser fc = new JFileChooser();
		

		//Set default directory to assets
		File currentDirFile = new File("");
		String helper = currentDirFile.getAbsolutePath();
		int cut = helper.indexOf("test") + 4 ;
		String defaultPath = helper.substring(0, cut);
		defaultPath += "\\core\\assets\\data\\scripts";
		currentDirFile = new File(defaultPath);
		fc.setCurrentDirectory(currentDirFile);

		// In response to a button click:
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// Pfad ANpassen
			String absoulutPath = fc.getSelectedFile().getAbsolutePath();

			
			// Create LuaScript&LuaState
			LuaScript newScript = new LuaScript(absoulutPath);
			if (newScript.canExecute())
				ScriptHolder.scriptStatesList.set(index, new LuaState(newScript));
		}

	}
	public static void addScript() {
		// Create a file chooser
		final JFileChooser fc = new JFileChooser();
		
		//Set default directory to assets
		File currentDirFile = new File("");
		String helper = currentDirFile.getAbsolutePath();
		int cut = helper.indexOf("test") + 4 ;
		String defaultPath = helper.substring(0, cut);
		defaultPath += "\\core\\assets\\data\\scripts";
		currentDirFile = new File(defaultPath);
		fc.setCurrentDirectory(currentDirFile);
		
		// In response to a button click:
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			// Pfad ANpassen
			String absoulutPath = fc.getSelectedFile().getAbsolutePath();
			
			// Create LuaScript&LuaState
			LuaScript newScript = new LuaScript(absoulutPath);
			if (newScript.canExecute())
				ScriptHolder.scriptStatesList.add(new LuaState(newScript));
		}

	}

	public static void loadDefault() {
		ScriptHolder.addBoidState(new EvadeState());
		ScriptHolder.addBoidState(new PursuitState());
		ScriptHolder.addBoidState(new WanderState());
		
		
	}

	private static void addBoidState(BoidState State) {
		scriptStatesList.add(State);
		
	}
	private String getRelativPfad(String absoulutPath){
		String path;
		
		int cut = absoulutPath.indexOf("assets\\") + 7;
		path = absoulutPath.substring(cut);
		path = path.replace("\\", "/");
		return path;
	}

	public static void removeScriptByIndex(int index) {
		scriptStatesList.remove(index);
		
	}
}
