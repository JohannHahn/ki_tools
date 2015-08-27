package com.mygdx.game;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.Entities.BoidState;
import com.mygdx.Entities.LuaState;
import com.mygdx.Entities.PointOfInterestEntity;
import com.mygdx.Script.LuaScript;
import com.mygdx.Script.ScriptHolder;
import com.mygdx.Script.ScriptManager;
import com.mygdx.components.BoidCenterComponent;
import com.mygdx.components.BoidDistanceComponent;
import com.mygdx.components.BoidMatchVelocityComponent;
import com.mygdx.components.FleeComponent;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.RenderComponent;
import com.mygdx.components.RessourceComponent;
import com.mygdx.components.SeekComponent;
import com.mygdx.components.VelocityComponent;
import com.mygdx.system.MovementSystem;
import com.mygdx.system.RenderSystem;
import com.mygdx.system.RessourceSystem;
import com.sun.corba.se.spi.orbutil.fsm.State;
import com.sun.org.apache.bcel.internal.generic.LUSHR;

import javax.swing.JFileChooser;

//new
import org.luaj.*;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.LuaValue;

public class MainScreen implements Screen {

	Engine engine;
	BoidEntity boid, boid2, boid3, boid4, boid5, boid6, boid7;
	Texture text = new Texture("smiley.png");
	Stage stage = new Stage();
	Image img = new Image(text);
	MyGdxGame game;
	int boidTeamSizeRed = 5;
	int boidTeamSizeGreen= 5; 
	private int windowWidth = Gdx.graphics.getWidth();
	private int windowHeight = Gdx.graphics.getHeight();
	private LuaScript stateScript;
	private BoidEntity boidTest;
	public static LuaState startStateGreen;
	public static LuaState startStateRed;

	public MainScreen(MyGdxGame game) {
		stateScript = new LuaScript("data/scripts/wanderState.lua");
		System.out.println(stateScript.canExecute());
		engine = new Engine();
		
		// Create Team Red
		for (int i = 0; i < boidTeamSizeRed; i++) {
			BoidEntity boidR;
			if (startStateRed == null) {
				boidR = new BoidEntity(BoidEntity.Team.RED, engine, BoidState.PURSUIT);
				
			} else {
				boidR = new BoidEntity(BoidEntity.Team.RED, engine, startStateRed);
			}
			boidR.add(new PositionComponent(MathUtils.random(0, windowWidth / 4f),
					MathUtils.random(0, windowHeight / 4f)));
			boidR.add(new VelocityComponent());
			// boidR.add(new SeekComponent());
			// boidR.add(new FleeComponent());
			boidR.add(new RenderComponent());
			boidR.add(new BoidCenterComponent());
			boidR.add(new BoidDistanceComponent());
			boidR.add(new BoidMatchVelocityComponent());
			boidR.add(new RessourceComponent());
			engine.addEntity(boidR);
		}

		// Create Team Green
		for (int i = 0; i < boidTeamSizeGreen; i++) {
			BoidEntity boidR;
			if (startStateRed == null) {
				boidR = new BoidEntity(BoidEntity.Team.GREEN, engine, BoidState.EVADE);
			} else {
				boidR = new BoidEntity(BoidEntity.Team.GREEN, engine, startStateGreen);
			}
			boidR.add(new PositionComponent(MathUtils.random(windowWidth, windowWidth - windowWidth / 4f),
					MathUtils.random(windowHeight, windowHeight - windowHeight / 4f)));
			boidR.add(new VelocityComponent());
			boidR.add(new SeekComponent());
			boidR.add(new RenderComponent());
			boidR.add(new BoidCenterComponent());
			boidR.add(new BoidDistanceComponent());
			boidR.add(new BoidMatchVelocityComponent());
			boidR.add(new RessourceComponent());
			engine.addEntity(boidR);
		}

		// Add PointOfInterrest
		final String PATH_TO_SKIN = "POI(tankestelle).png";
		PointOfInterestEntity poiEntitity = new PointOfInterestEntity("Tankestelle");
		poiEntitity.add(new RenderComponent(new Texture(PATH_TO_SKIN), 70, 70));
		poiEntitity.add(new PositionComponent(100, 100));
		engine.addEntity(poiEntitity);

		this.game = game;
		engine.addSystem(new MovementSystem());
		engine.addSystem(new RenderSystem(game.batch));
		engine.addSystem(new RessourceSystem());
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// AddBoidEntity
		if (Gdx.input.isKeyPressed(Keys.B)) {
			addBoidEntity();
		}

		// TODO: Pfad auf allgemin anpassen an den Loader angepasst
		if (Gdx.input.isKeyJustPressed(Keys.S)) {
			game.setScreen(new SkriptScreen(game));
			// addScript();

		}

		engine.update(delta);
	}

	private void addBoidEntity() {

		BoidEntity boidR = new BoidEntity(BoidEntity.Team.RED, engine, ScriptHolder.scriptStatesList.get(0));
		boidR.add(new PositionComponent(MathUtils.random(0, 600), MathUtils.random(0, 600)));
		boidR.add(new VelocityComponent());
		boidR.add(new SeekComponent());
		boidR.add(new FleeComponent());
		boidR.add(new RenderComponent());
		boidR.add(new BoidCenterComponent());
		boidR.add(new BoidDistanceComponent());
		boidR.add(new BoidMatchVelocityComponent());
		engine.addEntity(boidR);

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
