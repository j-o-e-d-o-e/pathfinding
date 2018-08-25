package net.joedoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    private ShapeRenderer shapeRenderer;
    private MazeController controller;
    private Panel panel;
    private Cursor cursor;
    private Texture cheese;
    private Animation<TextureRegion>[] animation;
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
        cheese = new Texture("entities/cheese.png");
        animation = initializeMouseAnimation();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(game.getBatch().getProjectionMatrix());
    }

    @SuppressWarnings("unchecked")
    private Animation<TextureRegion>[] initializeMouseAnimation() {
        TextureRegion[][] textureRegions = TextureRegion.split(new Texture("entities/mouse.png"), 38, 26);
        Animation<TextureRegion>[] animation = new Animation[textureRegions.length];
        for (int i = 0; i < textureRegions.length; i++)
            animation[i] = new Animation<TextureRegion>(10f / 30f, textureRegions[i]);
        return animation;
    }

    private void handleInput() {
        if (!GameInfo.cheeseIsSet) {
            if (Gdx.input.justTouched()) {
                Vector3 vector = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(vector);
                controller.setCheese(vector.x, vector.y);
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
            game.getBatch().draw(cheese, controller.getCheese()[0], controller.getCheese()[1]);
        }
        renderMice();
        game.getBatch().end();
        game.getBatch().setProjectionMatrix(panel.getStage().getCamera().combined);
        if (GameInfo.cheeseIsSet)
            renderMicePath();
        panel.getStage().draw();
        panel.getStage().act();
        panel.update(controller.getMice(), controller.getCheese());
    }

    private void renderMice() {
        for (Mouse mouse : controller.getMice()) {
            float x = mouse.getX();
            float y = mouse.getY();
            int direction = mouse.getDirection() - 1;
            game.getBatch().draw(animation[direction].getKeyFrame(elapsedTime, true), x, y, GameInfo.ONE_TILE, GameInfo.ONE_TILE);
        }
    }

    private void renderMicePath() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Color[] colors = new Color[2];
        colors[0] = Color.RED;
        colors[1] = Color.YELLOW;
        int index = 0;
        for (Mouse mouse : controller.getMice()) {
            for (Node node : mouse.getPath()) {
                int x = node.getX() * GameInfo.ONE_TILE;
                int y = node.getY() * GameInfo.ONE_TILE;
                shapeRenderer.setColor(colors[index]);
                shapeRenderer.line(x, y, x, y + GameInfo.ONE_TILE);
                shapeRenderer.line(x + GameInfo.ONE_TILE, y, x + GameInfo.ONE_TILE, y + GameInfo.ONE_TILE);
                shapeRenderer.line(x, y, x + GameInfo.ONE_TILE, y);
                shapeRenderer.line(x, y + GameInfo.ONE_TILE, x + GameInfo.ONE_TILE, y + GameInfo.ONE_TILE);
            }
            index++;
        }
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
        cursor.dispose();
        cheese.dispose();
    }
}