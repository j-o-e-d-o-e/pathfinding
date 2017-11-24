package net.joedoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import net.joedoe.GameInfo;
import net.joedoe.GameMain;
import net.joedoe.entities.Enemy;
import net.joedoe.helpers.FightHud;
import net.joedoe.logics.FightLogic;
import net.joedoe.logics.pathfinding.Node;

public class Fight extends MyScreen {
	private OrthogonalTiledMapRenderer renderer;
	private FightLogic fight;
	private FightHud hud;
	private float enemyTimer;

	public Fight(GameMain game) {
		super(game);
		fight = new FightLogic("maps/fight.tmx");
		hud = new FightHud(this.game, fight);
		renderer = new OrthogonalTiledMapRenderer(fight.getMap(), GameInfo.SCALE);
	}

	void handleInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)
				|| Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
				fight.getPlayer().setDirection(1);
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
				fight.getPlayer().setDirection(2);
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
				fight.getPlayer().setDirection(3);
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
				fight.getPlayer().setDirection(4);
			}
			if (fight.nextTileIsAccessible(fight.getPlayer()) && !fight.collidesWithActor(fight.getPlayer())) {
				fight.getPlayer().move();
			}
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
			fight.getPlayer().endTurn();
		}
	}

	void update() {
		if (!fight.getPlayer().hasMoved) {
			handleInput();
			for (Enemy enemy : fight.getEnemies()) {
				enemy.hasMoved = false;
			}
		} else if (enemyTimer >= 0.2) {
			fight.updateEnemies();
			enemyTimer = 0;
		}
	}

	@Override
	public void render(float delta) {
		enemyTimer += delta;
		update();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.render();
		renderer.setView(camera);
		game.getBatch().begin();
		fight.getPlayer().render(game.getBatch());
		for (Enemy enemy : fight.getEnemies()) {
			enemy.render(game.getBatch());
		}
		game.getBatch().end();
		game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
		debug();
		hud.update();
		hud.getStage().draw();
		hud.getStage().act();
	}

	void debug() {
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(game.getBatch().getProjectionMatrix());
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		// PATH OF ENEMY_1 IN RED
		for (Node node : fight.getEnemies().get(0).path) {
			node.render(shapeRenderer, Color.RED);
		}
		// PATH OF ENEMY_2 IN YELLOW
		for (Node node : fight.getEnemies().get(1).path) {
			node.render(shapeRenderer, Color.YELLOW);
		}
		shapeRenderer.end();
	}

	@Override
	public void dispose() {
		fight.dispose();
		hud.dispose();
	}
}
