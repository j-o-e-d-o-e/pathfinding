package net.joedoe.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import net.joedoe.GameInfo;

public class Node implements Connection<Node> {
    private final int x;
    private final int y;
    private int index;
    private Array<Connection<Node>> connections;

    Node(int x, int y, int index) {
        this.index = index;
        this.x = x;
        this.y = y;
        connections = new Array<Connection<Node>>();
    }

    public void render(ShapeRenderer shapeRenderer, Color color) {
        int x = this.x * GameInfo.ONE_TILE;
        int y = this.y * GameInfo.ONE_TILE;
        shapeRenderer.setColor(color);
        shapeRenderer.line(x, y, x, y + GameInfo.ONE_TILE);
        shapeRenderer.line(x + GameInfo.ONE_TILE, y, x + GameInfo.ONE_TILE, y + GameInfo.ONE_TILE);
        shapeRenderer.line(x, y, x + GameInfo.ONE_TILE, y);
        shapeRenderer.line(x, y + GameInfo.ONE_TILE, x + GameInfo.ONE_TILE, y + GameInfo.ONE_TILE);
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

    void addConnection(Node node) {
        if (node != null)
            connections.add(new DefaultConnection<Node>(this, node));
    }

    Array<Connection<Node>> getConnections() {
        return connections;
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
