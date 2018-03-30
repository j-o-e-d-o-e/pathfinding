package net.joedoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

import net.joedoe.GameInfo;
import net.joedoe.GameMain;
import net.joedoe.entities.Mouse;
import net.joedoe.helpers.MazeHud;
import net.joedoe.logics.MazeLogic;
import net.joedoe.logics.pathfinding.Node;

public class MazeScreen extends MyScreen {
    private OrthogonalTiledMapRenderer renderer;
    private MazeLogic mazeLogic;
    private MazeHud hud;
    private Cursor cursor;
    private float mouseTimer, elapsedTime;

    public MazeScreen(GameMain game) {
        super(game);
        mazeLogic = new MazeLogic("maps/maze.tmx");
        hud = new MazeHud(this.game, mazeLogic);
        renderer = new OrthogonalTiledMapRenderer(mazeLogic.getMap());
        cursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("entities/cheese.png")), 15, 15);
    }

    void handleInput() {
        if (!GameInfo.cheeseIsSet) {
            if (Gdx.input.justTouched()) {
                Vector3 vector = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(vector);
                mazeLogic.setCheese(vector.x, vector.y);
                for (Mouse mouse : mazeLogic.getMice()) {
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
        if (GameInfo.cheeseIsSet && !GameInfo.isPaused && mouseTimer >= 0.2) {
            mazeLogic.updateMice();
            mouseTimer = 0;
        }
        renderer.render();
        renderer.setView(camera);
        game.getBatch().begin();
        if (!GameInfo.cheeseIsSet) {
            Gdx.graphics.setCursor(cursor);
        } else {
            Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
            game.getBatch().draw(mazeLogic.getTextureCheese(), mazeLogic.getCheese()[0], mazeLogic.getCheese()[1]);
        }
        for (Mouse mouse : mazeLogic.getMice()) {
            mouse.render(game.getBatch(), elapsedTime);
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
        for (Node node : mazeLogic.getMice().get(0).path) {
            node.render(shapeRenderer, Color.RED);
        }
        // PATH OF MOUSE_2 IN YELLOW
        for (Node node : mazeLogic.getMice().get(1).path) {
            node.render(shapeRenderer, Color.YELLOW);
        }
        // // PATH OF MOUSE_3 IN BLUE
        // for (Node node : mazeLogic.getMice().get(2).path) {
        // node.render(shapeRenderer, Color.BLUE);
        // }
        // // PATH OF MOUSE_4 IN GREEN
        // for (Node node : mazeLogic.getMice().get(3).path) {
        // node.render(shapeRenderer, Color.GREEN);
        // }
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        mazeLogic.dispose();
        hud.dispose();
    }
}
