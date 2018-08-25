package net.joedoe.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;
import net.joedoe.GameInfo;

public class Graph implements IndexedGraph<Node> {
    private Array<Node> nodes;

    Graph(Array<Node> nodes) {
        this.nodes = nodes;
    }

    public Node getNodeByCoordinates(float x, float y) {
        return nodes.get((int) x / GameInfo.ONE_TILE + GameInfo.MAP_WIDTH * (int) y / GameInfo.ONE_TILE);
    }

    @Override
    public int getIndex(Node node) {
        return node.getIndex();
    }

    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        return fromNode.getConnections();
    }

    @Override
    public int getNodeCount() {
        return nodes.size;
    }
}
