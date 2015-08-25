package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.Script.ScriptHolder;

public class ClickListenerWithIndex extends ClickListener {
	
	private int indexToAdd;
	private TextArea textArea;

	public ClickListenerWithIndex(int index,TextArea textArea)
	{super();
	indexToAdd=index;
	this.textArea=textArea;
	}
	public void clicked(InputEvent event, float x, float y) {
		ScriptHolder.addScript(indexToAdd);
		textArea.setText(ScriptHolder.getName(indexToAdd));
		
	}
}
