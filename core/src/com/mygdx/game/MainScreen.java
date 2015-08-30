package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.Entities.BoidEntity;
import com.mygdx.States.BoidState;
import com.mygdx.States.EvadeState;
import com.mygdx.States.PursuitState;
import com.mygdx.Entities.PointOfInterestEntity;
import com.mygdx.Script.ScriptHolder;
import com.mygdx.components.BoidCenterComponent;
import com.mygdx.components.BoidDistanceComponent;
import com.mygdx.components.BoidMatchVelocityComponent;
import com.mygdx.components.PositionComponent;
import com.mygdx.components.RenderComponent;
import com.mygdx.components.RessourceComponent;
import com.mygdx.components.VelocityComponent;
import com.mygdx.system.MovementSystem;
import com.mygdx.system.RenderSystem;
import com.mygdx.system.RessourceSystem;

public class MainScreen implements Screen {

	Engine engine;

	Stage stage = new Stage();

	MyGdxGame game;

	public static int boidTeamSizeRed = 2;
	public static int boidTeamSizeGreen = 2;
	private int windowWidth = Gdx.graphics.getWidth();
	private int windowHeight = Gdx.graphics.getHeight();

	public static BoidState startStateGreen;
	public static BoidState startStateRed;

	public MainScreen(MyGdxGame game) {

		engine = new Engine();
		setupEntities();
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
		if (Gdx.input.isKeyPressed(Keys.R)) {
			addBoidEntity(BoidEntity.Team.RED);
		}
		if (Gdx.input.isKeyPressed(Keys.G)) {
			addBoidEntity(BoidEntity.Team.GREEN);
		}

		if (Gdx.input.isKeyJustPressed(Keys.S)) {
			game.setScreen(new SkriptScreen(game));

		}

		engine.update(delta);
	}

	private void addBoidEntity(BoidEntity.Team team) {

		BoidEntity boidR = new BoidEntity(team, engine, ScriptHolder.scriptStatesList.get(2));
		boidR.add(new PositionComponent(MathUtils.random(0, 600), MathUtils.random(0, 600)));
		boidR.add(new VelocityComponent());
		boidR.add(new RenderComponent());
		boidR.add(new BoidCenterComponent());
		boidR.add(new BoidDistanceComponent());
		boidR.add(new BoidMatchVelocityComponent());
		boidR.add(new RessourceComponent());
		engine.addEntity(boidR);

	}

	private void setupEntities()
	{
		BoidState.globalTarget = null;
		// Create Team Red
		for (int i = 0; i < boidTeamSizeRed; i++) {
			BoidEntity boidR;
			if (startStateRed == null) {
				boidR = new BoidEntity(BoidEntity.Team.RED, engine, new PursuitState());

			} else {
				boidR = new BoidEntity(BoidEntity.Team.RED, engine, startStateRed);
			}
			boidR.add(new PositionComponent(MathUtils.random(0, windowWidth / 4f),
					MathUtils.random(0, windowHeight / 4f)));
			boidR.add(new VelocityComponent());
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
			if (startStateGreen == null) {
				boidR = new BoidEntity(BoidEntity.Team.GREEN, engine, new EvadeState());
			} else {
				boidR = new BoidEntity(BoidEntity.Team.GREEN, engine, startStateGreen);
			}
			boidR.add(new PositionComponent(MathUtils.random(windowWidth, windowWidth - windowWidth / 4f),
					MathUtils.random(windowHeight, windowHeight - windowHeight / 4f)));
			boidR.add(new VelocityComponent());
			// boidR.add(new SeekComponent());
			boidR.add(new RenderComponent());
			boidR.add(new BoidCenterComponent());
			boidR.add(new BoidDistanceComponent());
			boidR.add(new BoidMatchVelocityComponent());
			boidR.add(new RessourceComponent());
			engine.addEntity(boidR);
		}

		// Add Tankstellen
		final String PATH_TO_SKIN = "POI(tankestelle).png";
		PointOfInterestEntity tankeGreen = new PointOfInterestEntity("Tankstelle-GREEN");
		tankeGreen.add(new RenderComponent(new Texture(PATH_TO_SKIN), 70, 70));
		tankeGreen.add(new PositionComponent(MathUtils.random(0, windowWidth), MathUtils.random(0, windowHeight)));
		engine.addEntity(tankeGreen);

		PointOfInterestEntity tankeRed = new PointOfInterestEntity("Tankstelle-RED");
		tankeRed.add(new RenderComponent(new Texture(PATH_TO_SKIN), 70, 70));
		tankeRed.add(new PositionComponent(MathUtils.random(0, windowWidth), MathUtils.random(0, windowHeight)));
		engine.addEntity(tankeRed);
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
