package net.joedoe.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.joedoe.GameInfo;

public class Actor {
	protected Texture texture;
	protected float x, y;
	protected int direction;
	protected String name;
	protected int strength, actionPoints;
	public boolean hasMoved;
	
    public Actor(Texture texture, String name, float x, float y, int strength) {
        this.texture = texture;
        this.name = name;
        this.x = x;
        this.y = y;
        this.strength = strength;
        this.actionPoints = strength;
    }

	public void move() {
		decreaseActionPointsBy(1);
		switch (direction) {
		case 1: // N
			y += GameInfo.ONE_TILE;
			if (y > GameInfo.HEIGHT - GameInfo.ONE_TILE) {
				y = GameInfo.HEIGHT - GameInfo.ONE_TILE;
			}
			break;
		case 2: // W
			x -= GameInfo.ONE_TILE;
			if (x < 0) {
				x = 0;
			}
			break;
		case 3: // S
			y -= GameInfo.ONE_TILE;
			if (y < 0) {
				y = 0;
			}
			break;
		case 4: // O
			x += GameInfo.ONE_TILE;
			if (x > GameInfo.WIDTH - GameInfo.ONE_TILE) {
				x = GameInfo.WIDTH - GameInfo.ONE_TILE;
			}
			break;
		}
	}

	public void endTurn() {
		decreaseActionPointsBy(actionPoints);
	}

	public boolean decreaseActionPointsBy(int amount) {
		if (actionPoints >= amount) {
			actionPoints -= amount;
			if (actionPoints == 0) {
				hasMoved = true;
				if (this instanceof Enemy) {
					setActionPointsToDefault();
				}
			}
			return true;
		}
		return false;
	}

	public void setHasMoved(boolean hasMoved) {
		if (!hasMoved) {
			setActionPointsToDefault();
		}
		this.hasMoved = hasMoved;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStrength() {
		return strength;
	}

	public int getActionPoints() {
		return actionPoints;
	}

	public void setActionPointsToDefault() {
		this.actionPoints = strength;
	}

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
