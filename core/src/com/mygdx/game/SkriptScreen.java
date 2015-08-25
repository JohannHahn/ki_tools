package com.mygdx.game;

import java.util.ArrayList;

import javax.swing.JFileChooser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.Entities.LuaState;
import com.mygdx.Script.LuaScript;
import com.mygdx.Script.ScriptHolder;

public class SkriptScreen implements Screen {
	private Stage stage;
	private MyGdxGame game;
	TextButton textButtonAddScript;
	private static final String PATH_TO_SKIN = "uiskin.json";
	public static final Skin defaultSkin = new Skin(Gdx.files.internal(PATH_TO_SKIN));
	public static int numberOfScriptstemp;
	private VerticalGroup verticalGroup;
	private Table table;
	private TextButton textButtonBack;
	private ArrayList<CheckBox> checkBoxArrayListGreen = new ArrayList<CheckBox>();
	private ArrayList<CheckBox> checkBoxArrayListRed = new ArrayList<CheckBox>();
	private TextButton textButtonDefault;

	public SkriptScreen(MyGdxGame game) {
		this.game = game;

	}

	public void create() {

	}

	public void resize(int width, int height) {

		stage.getViewport().update(width, height, true);

	}

	public void render(float delta) {
		Gdx.gl.glClearColor(0.8f, 1, 0.8f, 1);// clear the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	public void dispose() {
		stage.dispose();
	}

	@Override
	public void show() {
		checkBoxArrayListGreen = new ArrayList<CheckBox>();
		checkBoxArrayListRed = new ArrayList<CheckBox>();

		stage = new Stage(new ScreenViewport());
		verticalGroup = new VerticalGroup();
		table = new Table(defaultSkin);
		table.setFillParent(true);

		for (int i = 0; i < ScriptHolder.size(); i++) {

			HorizontalGroup hg = new HorizontalGroup();
			Label labelNumber = new Label(i + ",   ", defaultSkin);
			TextArea ta = new TextArea(ScriptHolder.getName(i), defaultSkin);
			TextButton bt = new TextButton("Select", defaultSkin);
			CheckBox cbGreen = new CheckBox("Start State Green", defaultSkin);
			CheckBox cbRed = new CheckBox("Start State Red", defaultSkin);
			checkBoxArrayListGreen.add(cbGreen);
			checkBoxArrayListRed.add(cbRed);
			bt.addListener(new ClickListenerWithIndex(i, ta));

			hg.addActor(labelNumber);
			hg.addActor(ta);
			hg.addActor(bt);
			hg.addActor(cbGreen);
			hg.addActor(cbRed);
			verticalGroup.addActor(hg);
		}

		textButtonAddScript = new TextButton("Add Script", defaultSkin);
		textButtonAddScript.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				ScriptHolder.addScript();
				show();
			}
		});
		verticalGroup.addActor(textButtonAddScript);

		textButtonDefault = new TextButton("Default behaviour", defaultSkin);
		textButtonDefault.addListener(new ClickListener() {
			// Set the Start states for MainScreen
			public void clicked(InputEvent event, float x, float y) {

				MainScreen.startStateGreen = null;
				MainScreen.startStateRed = null;
			}
		});
		verticalGroup.addActor(textButtonDefault);

		textButtonBack = new TextButton("Back", defaultSkin);
		textButtonBack.addListener(new ClickListener() {
			// Set the Start states for MainScreen
			public void clicked(InputEvent event, float x, float y) {

				int indexTemp = getSelectedCheckBoxIndex(checkBoxArrayListGreen);
				if (indexTemp != -1)
					MainScreen.startStateGreen = ScriptHolder.getLuaState(indexTemp);
				indexTemp = getSelectedCheckBoxIndex(checkBoxArrayListRed);
				if (indexTemp != -1)
					MainScreen.startStateRed = ScriptHolder.getLuaState(indexTemp);
				game.setScreen(new MainScreen(game));
			}
		});
		verticalGroup.addActor(textButtonBack);
		table.add(verticalGroup);
		stage.addActor(table);
		Gdx.input.setInputProcessor(stage);

	}

	protected int getSelectedCheckBoxIndex(ArrayList<CheckBox> checkBoxArrayList) {
		for (int i = 0; i < checkBoxArrayList.size(); i++) {
			if (checkBoxArrayList.get(i).isChecked()) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

}
