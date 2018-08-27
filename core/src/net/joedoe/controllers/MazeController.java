package net.joedoe.controllers;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
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
        mice.add(new Mouse("Pinky", 39, 4, 2));
        mice.add(new Mouse("The Brain", 39, 23, 2));
//        mice.add(new Mouse("Another Pinky", 0, 4, 4));
//        mice.add(new Mouse("Another Brain", 1, 23, 4));
    }

    public void setCheese(float x, float y) {
        float cheeseX = ((int) x / GameInfo.ONE_TILE) * GameInfo.ONE_TILE;
        float cheeseY = ((int) y / GameInfo.ONE_TILE) * GameInfo.ONE_TILE;
        if (mapController.currentTileIsAccessible(x, y) && !collides(cheeseX, cheeseY)) {
            cheese = new float[]{cheeseX, cheeseY};
            GameInfo.cheeseIsSet = true;
        }
    }

    private boolean collides(float x, float y) {
        for (Mouse mouse : mice) {
            if (x == mouse.getX() && y == mouse.getY())
                return true;
        }
        return false;
    }

    public void updateMice() {
        for (Mouse mouse : mice) {
            if (mouse.getPath() == null) {
                Node startNode = graph.getNodeByCoordinates(mouse.getX(), mouse.getY());
                Node endNode = graph.getNodeByCoordinates(cheese[0], cheese[1]);
                DefaultGraphPath<Node> path = new DefaultGraphPath<Node>();
                pathfinder.searchNodePath(startNode, endNode, heuristic, path);
                mouse.setPath(path);
            }
            mouse.setDirection();
            float[] nextTile = mapController.getCoordinatesOfNextTile(mouse);
            if (mouse.getDistance() > 1 && !collides(nextTile[0], nextTile[1])) {
                mouse.move();
                continue;
            }
            if (collides(nextTile[0], nextTile[1]))
                continue;
            if (mouse.getDistance() == 1) {
                GameInfo.cheeseIsSet = false;
                for (Mouse m : mice)
                    m.setPath(null);
                return;
            }
        }

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