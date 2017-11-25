package net.joedoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

import net.joedoe.GameInfo;
import net.joedoe.GameMain;
import net.joedoe.entities.Mouse;
import net.joedoe.helpers.MazeHud;
import net.joedoe.logics.MazeLogic;
import net.joedoe.logics.pathfinding.Node;

public class Maze extends MyScreen {
	private OrthogonalTiledMapRenderer renderer;
	private MazeLogic maze;
	private MazeHud hud;
	private Cursor cursor;
	private float mouseTimer, elapsedTime;
	private Texture cheese;
	private float cheeseX, cheeseY;

	public Maze(GameMain game) {
		super(game);
		maze = new MazeLogic("maps/maze.tmx");
		hud = new MazeHud(this.game, maze);
		renderer = new OrthogonalTiledMapRenderer(maze.getMap());
		cheese = new Texture("entities/cheese.png");
		cursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("entities/cheese.png")), 0, 31);

	}

	void handleInput() {
		if (!GameInfo.cheeseIsSet) {
			if (Gdx.input.justTouched()) {
				Vector3 vector = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(vector);
				int tileX = (int) vector.x / GameInfo.ONE_TILE;
				int tileY = (int) vector.y / GameInfo.ONE_TILE;
				float cheeseX = (int) (vector.x / GameInfo.ONE_TILE) * GameInfo.ONE_TILE;
				float cheeseY = (int) (vector.y / GameInfo.ONE_TILE) * GameInfo.ONE_TILE;
				if (maze.tileIsAccessible(tileX, tileY) && !maze.collidesWithActor(cheeseX, cheeseY)) {
					this.cheeseX = cheeseX;
					this.cheeseY = cheeseY;
					maze.setCheeseX(this.cheeseX);
					maze.setCheeseY(this.cheeseY);
					maze.setCheeseXY(tileX + "/" + tileY);
					GameInfo.cheeseIsSet = true;
				}
				for (Mouse mouse : maze.getMice()) {
					mouse.hasMoved = false;
				}
			}
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			if (GameInfo.isPaused) {
				GameInfo.isPaused = false;
			} else if (!GameInfo.isPaused) {
				GameInfo.isPaused = true;
			}
		}
	}

	@Override
	public void render(float delta) {
		mouseTimer += delta;
		elapsedTime += delta;
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		handleInput();
		if (!GameInfo.isPaused && mouseTimer >= 0.2) {
			maze.updateMice();
			mouseTimer = 0;
		}
		renderer.render();
		renderer.setView(camera);
		game.getBatch().begin();
		if (GameInfo.cheeseIsSet) {
			Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
			game.getBatch().draw(cheese, cheeseX, cheeseY);
		} else if (!GameInfo.cheeseIsSet) {
			Gdx.graphics.setCursor(cursor);
		}
		for (Mouse enemy : maze.getMice()) {
			enemy.render(game.getBatch(), elapsedTime);
		}
		game.getBatch().end();
		game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
		if (GameInfo.cheeseIsSet) {
			debug();
		}
		hud.update();
		hud.getStage().draw();
		hud.getStage().act();
	}

	void debug() {
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(game.getBatch().getProjectionMatrix());
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		// PATH OF MOUSE_1 IN RED
		for (Node node : maze.getMice().get(0).path) {
			node.render(shapeRenderer, Color.RED);
		}
		// PATH OF MOUSE_2 IN YELLOW
		for (Node node : maze.getMice().get(1).path) {
			node.render(shapeRenderer, Color.YELLOW);
		}
		shapeRenderer.end();
	}

	@Override
	public void dispose() {
		maze.dispose();
		hud.dispose();
	}
}
