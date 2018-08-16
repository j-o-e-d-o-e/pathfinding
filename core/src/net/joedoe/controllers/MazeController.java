package net.joedoe.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import net.joedoe.GameInfo;
import net.joedoe.entities.Mouse;
import net.joedoe.pathfinding.GraphGenerator;

import java.util.ArrayList;

public class MazeController {
    private TiledMap map;
    private GraphGenerator graphGenerator;
    private ArrayList<Mouse> mice;
    private Texture cheeseTexture;
    private float[] cheese;

    public MazeController(String path) {
        map = new TmxMapLoader().load(path);
        graphGenerator = new GraphGenerator(map);
        cheeseTexture = new Texture("entities/cheese.png");
        initializeMice();
    }

    private void initializeMice() {
        mice = new ArrayList<Mouse>();
        float x1 = GameInfo.WIDTH / 2f + GameInfo.ONE_TILE * 19;
        float y1 = GameInfo.HEIGHT - GameInfo.ONE_TILE * 21;
        mice.add(new Mouse("Pinky", x1, y1));
        float x2 = GameInfo.WIDTH / 2f + GameInfo.ONE_TILE * 19;
        float y2 = GameInfo.HEIGHT - GameInfo.ONE_TILE * 2;
        mice.add(new Mouse("The Brain", x2, y2));
        // float x3 = GameInfo.WIDTH / 2f - GameInfo.ONE_TILE * 19;
        // float y3 = GameInfo.HEIGHT - GameInfo.ONE_TILE * 2;
        // mice.add(new Mouse("Mouse 3", x3, y3));
        // float x4 = GameInfo.WIDTH / 2f - GameInfo.ONE_TILE * 19;
        // float y4 = GameInfo.HEIGHT - GameInfo.ONE_TILE * 21;
        // mice.add(new Mouse("Mouse 4", x4, y4));
    }

    public void setCheese(float x, float y) {
        int tileX = (int) x / GameInfo.ONE_TILE;
        int tileY = (int) y / GameInfo.ONE_TILE;
        float px_X = (int) (x / GameInfo.ONE_TILE) * GameInfo.ONE_TILE;
        float px_Y = (int) (y / GameInfo.ONE_TILE) * GameInfo.ONE_TILE;
        if (tileIsAccessible(tileX, tileY) && !collidesWithMouse(px_X, px_Y)) {
            cheese = new float[]{px_X, px_Y};
            GameInfo.cheeseIsSet = true;
        }
    }

    private boolean tileIsAccessible(int tileX, int tileY) {
        Cell cell = ((TiledMapTileLayer) map.getLayers().get("top")).getCell(tileX, tileY);
        return cell == null;
    }

    private boolean collidesWithMouse(float x, float y) {
        for (Mouse mouse : mice) {
            if (x == mouse.getX() && y == mouse.getY())
                return true;
        }
        return false;
    }

    public void updateMice() {
        for (Mouse mouse : mice) {
            if (!mouse.hasMoved()) {
                mouse.setGraph(graphGenerator.generateGraph(mouse, mice));
                if (mouse.calculatePath(cheese)) {
                    mouse.setDirection();
                    if (mouse.getDistance() > 1)
                        mouse.move();
                    else
                        setMiceMoved(true);
                } else {
                    mouse.setMoved(true);
                }
            } else if (getMiceHaveMoved()) {
                GameInfo.cheeseIsSet = false;
            }
        }
    }

    public void setMiceMoved(boolean moved) {
        for (Mouse mouse : mice)
            mouse.setMoved(moved);
        GameInfo.cheeseIsSet = false;
    }

    private boolean getMiceHaveMoved() {
        for (Mouse mouse : mice) {
            if (!mouse.hasMoved())
                return false;
        }
        return true;
    }

    public void renderMice(SpriteBatch batch, float elapsedTime) {
        for (Mouse mouse : mice) {
            mouse.render(batch, elapsedTime);
        }
    }

    public void renderMicePath(ShapeRenderer shapeRenderer) {
        Mouse mouse1 = mice.get(0);
        mouse1.renderPath(shapeRenderer, Color.RED);
        Mouse mouse2 = mice.get(1);
        mouse2.renderPath(shapeRenderer, Color.YELLOW);
    }

    public TiledMap getMap() {
        return map;
    }

    public ArrayList<Mouse> getMice() {
        return mice;
    }

    public Texture getCheeseTexture() {
        return cheeseTexture;
    }

    public float[] getCheese() {
        return cheese;
    }

    public void dispose() {
        map.dispose();
        cheeseTexture.dispose();
    }
}