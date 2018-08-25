package net.joedoe.entities;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import net.joedoe.GameInfo;
import net.joedoe.pathfinding.Node;

public class Mouse implements MapEntity {
    private String name;
    private float x, y;
    private int direction = 2;
    private DefaultGraphPath<Node> path;
    private int pathIndex = 1;
    private boolean moved;

    public Mouse(String name, float x, float y) {
        this.name = name;
        this.x = x * GameInfo.ONE_TILE;
        this.y = y * GameInfo.ONE_TILE;
    }

    public void setDirection() {
        Node nextNode = getNextNode();
        if (nextNode.getY() * GameInfo.ONE_TILE > y)
            direction = 1; // N
        else if (nextNode.getX() * GameInfo.ONE_TILE < x)
            direction = 2; // W
        else if (nextNode.getY() * GameInfo.ONE_TILE < y)
            direction = 3; // S
        else if (nextNode.getX() * GameInfo.ONE_TILE > x)
            direction = 4; // E
    }

    public void move() {
        pathIndex++;
        switch (direction) {
            case 1: // N
                y += GameInfo.ONE_TILE;
                if (y > GameInfo.HEIGHT - GameInfo.ONE_TILE)
                    y = GameInfo.HEIGHT - GameInfo.ONE_TILE;
                break;
            case 2: // W
                x -= GameInfo.ONE_TILE;
                if (x < 0)
                    x = 0;
                break;
            case 3: // S
                y -= GameInfo.ONE_TILE;
                if (y < 0)
                    y = 0;
                break;
            case 4: // E
                x += GameInfo.ONE_TILE;
                if (x > GameInfo.WIDTH - GameInfo.ONE_TILE)
                    x = GameInfo.WIDTH - GameInfo.ONE_TILE;
                break;
        }
    }

    public int getDistance() {
        return path.getCount() - pathIndex;
    }

    private Node getNextNode() {
        return path.nodes.get(pathIndex);
    }

    public String getName() {
        return name;
    }

    public DefaultGraphPath<Node> getPath() {
        return path;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public int getDirection() {
        return direction;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public void setPath(DefaultGraphPath<Node> path) {
        pathIndex = 1;
        this.path = path;
    }
}
