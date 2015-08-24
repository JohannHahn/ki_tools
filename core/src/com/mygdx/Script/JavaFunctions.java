package com.mygdx.Script;

import javax.xml.transform.TransformerException;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import com.badlogic.ashley.core.Entity;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.Entities.BoidState;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.operations.UnaryOperation;

public class JavaFunctions extends OneArgFunction {
	
	public JavaFunctions(){
		
	}

	@Override
	public LuaValue call(LuaValue env) {
		LuaTable functions = new LuaTable();
		env.set("JavaFunctions", functions);
		return null;
	}
	
	static final class SearchTarget extends OneArgFunction{
	    

		@Override
		public LuaValue call(LuaValue arg) {
			// TODO Auto-generated method stub
			return null;
		}

		
	}
}
