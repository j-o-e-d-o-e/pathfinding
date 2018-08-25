package net.joedoe.pathfinding;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;

public class GraphGenerator {
    private Array<Node> nodes = new Array<Node>();
    private TiledMap map;
    private static int mapHeight;
    static int mapWidth;

    public GraphGenerator(TiledMap map) {
        this.map = map;
        mapWidth = map.getProperties().get("width", Integer.class);
        mapHeight = map.getProperties().get("height", Integer.class);
        createBasicArray();
    }

    private void createBasicArray() {
        int index = 0;
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++)
                nodes.add(new Node(x, y, index++));
        }
    }

    public Graph generateGraph() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("top");
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if (layer.getCell(x, y) == null) { // current cell is accessible
                    // get the current node from nodes and add connections to neighboring nodes:
                    Node currentNode = nodes.get(x + mapWidth * y);
                    if (layer.getCell(x, y + 1) == null && y != mapHeight - 1) // N
                        currentNode.addConnection(nodes.get(x + mapWidth * (y + 1)));
                    if (layer.getCell(x - 1, y) == null && x != 0) // W
                        currentNode.addConnection(nodes.get((x - 1) + mapWidth * y));
                    if (layer.getCell(x, y - 1) == null && y != 0) // S
                        currentNode.addConnection(nodes.get(x + mapWidth * (y - 1)));
                    if (layer.getCell(x + 1, y) == null && x != mapWidth - 1) // E
                        currentNode.addConnection(nodes.get((x + 1) + mapWidth * y));
                }
            }
        }
        return new Graph(nodes);
    }
}
