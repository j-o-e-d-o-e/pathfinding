package net.joedoe.logics.pathfinding;

import java.util.ArrayList;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;

import net.joedoe.GameInfo;
import net.joedoe.entities.Mouse;

public class GraphGenerator {
    private Array<Node> nodes = new Array<Node>();
    private TiledMapTileLayer layer;
    public static int mapWidth, mapHeight;

    public GraphGenerator(TiledMap map) {
        int index = 0;
        mapWidth = map.getProperties().get("width", Integer.class);
        mapHeight = map.getProperties().get("height", Integer.class);
        layer = (TiledMapTileLayer) map.getLayers().get("top");
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                nodes.add(new Node(x, y, index++));
            }
        }
    }

    public Graph generateGraph(Mouse currentEnemy, ArrayList<Mouse> enemies) {
        Array<Node> otherMice = getOtherEnemies(currentEnemy, enemies);
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
                    if (layer.getCell((x + 1), y) == null && x != mapWidth - 1) { // E
                        currentNode.addConnection(nodes.get((x + 1) + mapWidth * y));
                    }
                    if (otherMice.size != 0) {
                        for (Node otherMouse : otherMice) {
                            if (currentNode.getIndex() == otherMouse.getIndex()) {
                                currentNode.getConnections().clear();
                            }
                        }
                    }
                }
            }
        }
        return new Graph(nodes);
    }

    Array<Node> getOtherEnemies(Mouse currentMouse, ArrayList<Mouse> mice) {
        Array<Node> otherMice = new Array<Node>();
        for (Mouse mouse : mice) {
            if (mouse != currentMouse) {
                int index = (int) mouse.getX() / GameInfo.ONE_TILE + mapWidth * (int) mouse.getY() / GameInfo.ONE_TILE;
                otherMice.add(nodes.get(index));
            }
        }
        return otherMice;
    }
}
