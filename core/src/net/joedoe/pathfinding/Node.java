package net.joedoe.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.utils.Array;

public class Node implements Connection<Node> {
    private final int x;
    private final int y;
    private int index;
    private Array<Connection<Node>> connections = new Array<Connection<Node>>();

    Node(int x, int y, int index) {
        this.index = index;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    int getIndex() {
        return index;
    }

    Array<Connection<Node>> getConnections() {
        return connections;
    }

    void addConnection(Node node) {
        if (node != null)
            connections.add(new DefaultConnection<Node>(this, node));
    }

    @Override
    public float getCost() {
        return 0;
    }

    @Override
    public Node getFromNode() {
        return null;
    }

    @Override
    public Node getToNode() {
        return null;
    }
}
