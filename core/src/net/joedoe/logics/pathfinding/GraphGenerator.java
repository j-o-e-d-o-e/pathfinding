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
		layer = (TiledMapTileLayer) map.getLayers().get("top");
		// create a basic array which contains a node for each tile on the map:
		int index = 0;
		mapWidth = map.getProperties().get("width", Integer.class);
		mapHeight = map.getProperties().get("height", Integer.class);
		for (int y = 0; y < mapHeight; y++) {
			for (int x = 0; x < mapWidth; x++) {
				nodes.add(new Node(x, y, index++));
			}
		}
	}

	public Graph generateGraph(Mouse currentMouse, ArrayList<Mouse> mice) {
		Array<Node> otherMice = getOtherMice(currentMouse, mice);
		for (int y = 0; y < mapHeight; y++) {
			for (int x = 0; x < mapWidth; x++) {
				if (layer.getCell(x, y) == null) { // current cell is accessible
					// get the current node from Array<Node> nodes
					// and add connections to neighboring nodes:
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
					// delete positions of other mice from path of current mouse
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

	Array<Node> getOtherMice(Mouse currentMouse, ArrayList<Mouse> mice) {
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
