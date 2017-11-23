package net.joedoe.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.joedoe.GameInfo;

public abstract class MovingEntity {
    protected Texture texture;
    protected float x, y;
    protected int direction;

    public abstract void move();

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, GameInfo.ONE_TILE, GameInfo.ONE_TILE);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(String path) {
        this.texture = new Texture(path);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
