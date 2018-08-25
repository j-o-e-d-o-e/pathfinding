package net.joedoe.pathfinding;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import net.joedoe.GameInfo;

public class GraphGenerator {
    private TiledMap map;
    private Array<Node> nodes = new Array<Node>();

    public GraphGenerator(TiledMap map) {
        this.map = map;
        createBasicArray();
    }

    private void createBasicArray() {
        int index = 0;
        for (int y = 0; y < GameInfo.MAP_HEIGHT; y++) {
            for (int x = 0; x < GameInfo.MAP_WIDTH; x++)
                nodes.add(new Node(x, y, index++));
        }
    }

    public Graph generateGraph() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("top");
        for (int y = 0; y < GameInfo.MAP_HEIGHT; y++) {
            for (int x = 0; x < GameInfo.MAP_WIDTH; x++) {
                if (layer.getCell(x, y) == null) { // current cell is accessible
                    Node currentNode = nodes.get(x + GameInfo.MAP_WIDTH * y);
                    if (layer.getCell(x, y + 1) == null && y != GameInfo.MAP_HEIGHT - 1) // N
                        currentNode.addConnection(nodes.get(x + GameInfo.MAP_WIDTH * (y + 1)));
                    if (layer.getCell(x - 1, y) == null && x != 0) // W
                        currentNode.addConnection(nodes.get((x - 1) + GameInfo.MAP_WIDTH * y));
                    if (layer.getCell(x, y - 1) == null && y != 0) // S
                        currentNode.addConnection(nodes.get(x + GameInfo.MAP_WIDTH * (y - 1)));
                    if (layer.getCell(x + 1, y) == null && x != GameInfo.MAP_WIDTH - 1) // E
                        currentNode.addConnection(nodes.get((x + 1) + GameInfo.MAP_WIDTH * y));
                }
            }
        }
        return new Graph(nodes);
    }
}
