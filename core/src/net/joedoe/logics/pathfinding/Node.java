package net.joedoe.logics.pathfinding;

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

    public Node(int x, int y, int index) {
        this.index = index;
        this.x = x;
        this.y = y;
        connections = new Array<Connection<Node>>();
    }

    public int getIndex() {
        return index;
    }

    public Array<Connection<Node>> getConnections() {
        return connections;
    }

    public void addConnection(Node node) {
        if (node != null) { // Make sure there is a neighboring node
            connections.add(new DefaultConnection<Node>(this, node));
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String print() {
        return "\"" + index + "\" at " + x + "/" + y;
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

    @Override
    public float getCost() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Node getFromNode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node getToNode() {
        // TODO Auto-generated method stub
        return null;
    }
}
