package net.joedoe.controllers;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import net.joedoe.GameInfo;
import net.joedoe.entities.Mouse;
import net.joedoe.pathfinding.Graph;
import net.joedoe.pathfinding.ManhattanHeuristic;
import net.joedoe.pathfinding.Node;

import java.util.ArrayList;

public class MazeController {
    private MapController mapController;
    private Graph graph;
    private IndexedAStarPathFinder<Node> pathfinder;
    private ManhattanHeuristic heuristic = new ManhattanHeuristic();
    private ArrayList<Mouse> mice;
    private float[] cheese;

    public MazeController(String path) {
        mapController = new MapController(path);
        graph = mapController.generateGraph();
        pathfinder = new IndexedAStarPathFinder<Node>(graph, true);
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
        float cheeseX = ((int) x / GameInfo.ONE_TILE) * GameInfo.ONE_TILE;
        float cheeseY = ((int) y / GameInfo.ONE_TILE) * GameInfo.ONE_TILE;
        if (mapController.currentTileIsAccessible(x, y) && !collides(cheeseX, cheeseY)) {
            cheese = new float[]{cheeseX, cheeseY};
            GameInfo.cheeseIsSet = true;
            setMiceMoved(false);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean collides(float x, float y) {
        for (Mouse mouse : mice) {
            if (x == mouse.getX() && y == mouse.getY())
                return true;
        }
        return false;
    }

    public void updateMice() {
        for (Mouse mouse : mice) {
            if (!mouse.hasMoved()) {
                Node startNode = graph.getNodeByCoordinates(mouse.getX(), mouse.getY());
                Node endNode = graph.getNodeByCoordinates(cheese[0], cheese[1]);
                DefaultGraphPath<Node> path = new DefaultGraphPath<Node>();
                if (pathfinder.searchNodePath(startNode, endNode, heuristic, path)) {
                    mouse.setPath(path);
                    mouse.setDirection();
                    float[] nextTile = mapController.getCoordinatesOfNextTile(mouse);
                    if (mouse.getDistance() > 1 && !collides(nextTile[0], nextTile[1]))
                        mouse.move();
                    else if (mouse.getDistance() == 1)
                        setMiceMoved(true);
                    else
                        mouse.setMoved(true);
                } else {
                    mouse.setMoved(true);
                }
            } else if (getMiceHaveMoved()) {
                GameInfo.cheeseIsSet = false;
            }
        }
    }

    private void setMiceMoved(boolean moved) {
        for (Mouse mouse : mice)
            mouse.setMoved(moved);
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
        return mapController.getMap();
    }

    public ArrayList<Mouse> getMice() {
        return mice;
    }

    public float[] getCheese() {
        return cheese;
    }

    public void dispose() {
        mapController.dispose();
    }
}