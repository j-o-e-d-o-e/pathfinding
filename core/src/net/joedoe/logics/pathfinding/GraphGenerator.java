package net.joedoe.logics.pathfinding;

import java.util.ArrayList;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;

import net.joedoe.GameInfo;
import net.joedoe.entities.Enemy;

public class GraphGenerator {
    private Array<Node> nodes = new Array<Node>();
    private TiledMapTileLayer layer;
    public static int mapWidth, mapHeight;

    public GraphGenerator(TiledMap map) {
        int index = 0;
        mapWidth = map.getProperties().get("width", Integer.class); // 40
        mapHeight = map.getProperties().get("height", Integer.class); // 25
        layer = (TiledMapTileLayer) map.getLayers().get("middle");
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                nodes.add(new Node(x, y, index++));
            }
        }
    }

    public Graph generateGraph(Enemy currentEnemy, ArrayList<Enemy> enemies) {
        Array<Node> otherEnemies = getOtherEnemies(currentEnemy, enemies);
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if (layer.getCell(x, y) == null) { // current cell is accessible
                    Node currentNode = nodes.get(x + mapWidth * y);
                    if (layer.getCell(x, (y + 1)) == null && y != mapHeight - 1) { // N
                        currentNode.addConnection(nodes.get(x + mapWidth * (y + 1)));
                    }
                    if (layer.getCell((x - 1), y) == null && x != 0) { // W
                        currentNode.addConnection(nodes.get((x - 1) + mapWidth * y));
                    }
                    if (layer.getCell(x, (y - 1)) == null && y != 0) { // S
                        currentNode.addConnection(nodes.get(x + mapWidth * (y - 1)));
                    }
                    if (layer.getCell((x + 1), y) == null && x != mapWidth - 1) { // O
                        currentNode.addConnection(nodes.get((x + 1) + mapWidth * y));
                    }
                    if (otherEnemies.size != 0) {
                        for (Node otherEnemy : otherEnemies) {
                            if (currentNode.getIndex() == otherEnemy.getIndex()) {
                                currentNode.getConnections().clear();
                            }
                        }
                    }
                }
            }
        }
        return new Graph(nodes);
    }

    Array<Node> getOtherEnemies(Enemy currentEnemy, ArrayList<Enemy> enemies) {
        Array<Node> otherEnemies = new Array<Node>();
        for (Enemy enemy : enemies) {
            if (enemy != currentEnemy) {
                int index = (int) enemy.getX() / GameInfo.ONE_TILE + mapWidth * (int) enemy.getY() / GameInfo.ONE_TILE;
                otherEnemies.add(nodes.get(index));
            }
        }
        return otherEnemies;
    }

    public Array<Node> getNodes() {
        return nodes;
    }

    public void setNodes(Array<Node> nodes) {
        this.nodes = nodes;
    }
}
