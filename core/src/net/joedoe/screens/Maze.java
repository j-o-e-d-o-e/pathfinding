package net.joedoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
	private float mouseTimer;
	private Animation<TextureRegion>[] mouse;
	private Texture cheese;

	public Maze(GameMain game) {
		super(game);
		maze = new MazeLogic("maps/maze.tmx");
		hud = new MazeHud(this.game, maze);
		renderer = new OrthogonalTiledMapRenderer(maze.getMap());
		cheese = new Texture("entities/cheese.png");
		TextureRegion[][] mouseTextures = TextureRegion.split(new Texture("entities/mouse.png"), 38, 26);
		mouse = new Animation[mouseTextures.length];
		for (int i = 0; i < mouseTextures.length; i++) {
			mouse[i] = new Animation<TextureRegion>(10f / 30f, mouseTextures[i]);
		}
		Cursor cursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("entities/cheese.png")), 0, 31);
		Gdx.graphics.setCursor(cursor);
	}

	void handleInput() {
		if (!GameInfo.isPaused) {
			if (Gdx.input.justTouched()) {
				Vector3 vector = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(vector);
				String cheeseXY = (int) (vector.x / GameInfo.ONE_TILE) + "/" + (int) (vector.y / GameInfo.ONE_TILE);
				maze.setCheeseXY(cheeseXY);
				GameInfo.isSet = true;
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
				GameInfo.isPaused = true;
			}
		} else if (GameInfo.isPaused) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
				GameInfo.isPaused = false;
			}
		}
	}

	void update() {
		if (!GameInfo.isSet) {
			handleInput();
			for (Mouse mouse : maze.getMice()) {
				mouse.hasMoved = false;
			}
		} else if (mouseTimer >= 0.3) {
			maze.updateMice();
			mouseTimer = 0;
		}
	}

	@Override
	public void render(float delta) {
		mouseTimer += delta;
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (!GameInfo.isPaused) {
			update();
		}
		renderer.render();
		renderer.setView(camera);
		game.getBatch().begin();
		// maze.getPlayer().render(game.getBatch());
		for (Mouse enemy : maze.getMice()) {
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
		for (Node node : maze.getMice().get(0).path) {
			node.render(shapeRenderer, Color.RED);
		}
		// PATH OF ENEMY_2 IN YELLOW
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
