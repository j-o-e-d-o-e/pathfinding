package net.joedoe.entities;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.joedoe.GameInfo;
import net.joedoe.pathfinding.Graph;
import net.joedoe.pathfinding.ManhattanHeuristic;
import net.joedoe.pathfinding.Node;

public class Mouse {
    private Animation<TextureRegion>[] texture;
    private float x, y;
    private int direction = 2;
    private String name;
    private boolean moved;
    private Graph graph;
    private DefaultGraphPath<Node> path;
    private ManhattanHeuristic heuristic;
    private int pathIndex = 1;

    public Mouse(String name, float x, float y) {
        this.texture = initializeTextureRegion();
        this.name = name;
        this.x = x;
        this.y = y;
        path = new DefaultGraphPath<Node>();
        heuristic = new ManhattanHeuristic();
    }

    @SuppressWarnings("unchecked")
    private Animation<TextureRegion>[] initializeTextureRegion() {
        TextureRegion[][] textureRegions = TextureRegion.split(new Texture("entities/mouse.png"), 38, 26);
        Animation<TextureRegion>[] animation = new Animation[textureRegions.length];
        for (int i = 0; i < textureRegions.length; i++)
            animation[i] = new Animation<TextureRegion>(10f / 30f, textureRegions[i]);
        return animation;
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

    public boolean calculatePath(float[] cheese) {
        IndexedAStarPathFinder<Node> pathfinder = new IndexedAStarPathFinder<Node>(graph, true);
        Node startNode = graph.getNodeByCoordinates(x, y);
        Node endNode = graph.getNodeByCoordinates(cheese[0], cheese[1]);
        pathIndex = 1;
        path.clear();
        return pathfinder.searchNodePath(startNode, endNode, heuristic, path);
    }

    public int getDistance() {
        int distance = 0;
        for (int i = pathIndex - 1; i < path.getCount() - 1; i++)
            distance++;
        return distance;
    }

    private Node getNextNode() {
        return path.nodes.get(pathIndex);
    }

    public void render(SpriteBatch batch, float elapsedTime) {
        batch.draw(texture[direction - 1].getKeyFrame(elapsedTime, true), x, y, GameInfo.ONE_TILE, GameInfo.ONE_TILE);
    }

    public String getName() {
        return name;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }


    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public DefaultGraphPath<Node> getPath() {
        return path;
    }
}
