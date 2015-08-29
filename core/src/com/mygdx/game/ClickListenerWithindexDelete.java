package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.Script.ScriptHolder;


public class ClickListenerWithindexDelete  extends ClickListener{
	private int index;
	private Screen screen;
	public ClickListenerWithindexDelete(int index,Screen screen) {
		super();
		this.index=index;
		this.screen=screen;
	}
	@Override
	public void clicked(InputEvent event, float x, float y) {
		
		super.clicked(event, x, y);
		ScriptHolder.removeScriptByIndex(index);
		screen.show();
	}

}
