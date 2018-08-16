package net.joedoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.joedoe.GameInfo;
import net.joedoe.GameMain;
import net.joedoe.controllers.MazeController;
import net.joedoe.entities.Mouse;
import net.joedoe.gui_elements.Panel;
import net.joedoe.pathfinding.Node;

public class MazeScreen implements Screen {
    private GameMain game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private OrthogonalTiledMapRenderer renderer;
    private MazeController controller;
    private Panel panel;
    private Cursor cursor;
    private float mouseTimer, elapsedTime;

    public MazeScreen(GameMain game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameInfo.WIDTH, GameInfo.HEIGHT);
        camera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);
        viewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, camera);
        controller = new MazeController("maps/maze.tmx");
        panel = new Panel(this.game);
        renderer = new OrthogonalTiledMapRenderer(controller.getMap());
        cursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("entities/cheese.png")), 15, 15);
    }

    private void handleInput() {
        if (!GameInfo.cheeseIsSet) {
            if (Gdx.input.justTouched()) {
                Vector3 vector = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(vector);
                controller.setCheese(vector.x, vector.y);
                for (Mouse mouse : controller.getMice()) {
                    mouse.setMoved(false);
                }
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            if (GameInfo.isPaused)
                GameInfo.isPaused = false;
            else if (!GameInfo.isPaused)
                GameInfo.isPaused = true;
        }
    }

    @Override
    public void render(float delta) {
        mouseTimer += delta;
        elapsedTime += delta;
        handleInput();
        if (GameInfo.cheeseIsSet && !GameInfo.isPaused && mouseTimer >= 0.2) {
            controller.updateMice();
            mouseTimer = 0;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        renderer.setView(camera);
        game.getBatch().begin();
        if (!GameInfo.cheeseIsSet) {
            Gdx.graphics.setCursor(cursor);
        } else {
            Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
            game.getBatch().draw(controller.getCheeseTexture(), controller.getCheese()[0], controller.getCheese()[1]);
        }
        for (Mouse mouse : controller.getMice()) {
            mouse.render(game.getBatch(), elapsedTime);
        }
        game.getBatch().end();
        game.getBatch().setProjectionMatrix(panel.getStage().getCamera().combined);
        if (GameInfo.cheeseIsSet)
            debug();
        panel.getStage().draw();
        panel.getStage().act();
        panel.update(controller.getMice(), controller.getCheese());
    }

    private void debug() {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(game.getBatch().getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        // PATH OF MOUSE_1 IN RED
        for (Node node : controller.getMice().get(0).getPath()) {
            node.render(shapeRenderer, Color.RED);
        }
        // PATH OF MOUSE_2 IN YELLOW
        for (Node node : controller.getMice().get(1).getPath()) {
            node.render(shapeRenderer, Color.YELLOW);
        }
        // PATH OF MOUSE_3 IN BLUE
        // for (Node node : controller.getMice().get(2).getPath()) {
        // node.render(shapeRenderer, Color.BLUE);
        // }
        // PATH OF MOUSE_4 IN GREEN
        // for (Node node : controller.getMice().get(3).getPath()) {
        // node.render(shapeRenderer, Color.GREEN);
        // }
        shapeRenderer.end();
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        controller.dispose();
        panel.dispose();
    }
}