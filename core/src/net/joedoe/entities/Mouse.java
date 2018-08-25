package net.joedoe.entities;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import net.joedoe.GameInfo;
import net.joedoe.pathfinding.Node;

public class Mouse implements MapEntity{
    private String name;
    private float x, y;
    private int direction = 2;
    private Animation<TextureRegion>[] texture;
    private DefaultGraphPath<Node> path;
    private int pathIndex = 1;
    private boolean moved;

    public Mouse(String name, float x, float y) {
        this.texture = initializeTextureRegion();
        this.name = name;
        this.x = x;
        this.y = y;
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

    public int getDistance() {
        return path.getCount() - pathIndex;
    }

    private Node getNextNode() {
        return path.nodes.get(pathIndex);
    }

    public void render(SpriteBatch batch, float elapsedTime) {
        batch.draw(texture[direction - 1].getKeyFrame(elapsedTime, true), x, y, GameInfo.ONE_TILE, GameInfo.ONE_TILE);
    }

    public void renderPath(ShapeRenderer shapeRenderer, Color color) {
        for (Node node : path) {
            node.render(shapeRenderer, color);
        }
    }

    public String getName() {
        return name;
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
