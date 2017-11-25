package net.joedoe.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.joedoe.GameInfo;

public abstract class Actor {
	protected Animation<TextureRegion>[] texture;
	protected float x, y;
	protected int direction = 1;
	protected String name;
	public boolean hasMoved;

	public Actor(String path, String name, float x, float y, int strength) {
		this.texture = initializeTextureRegion(path);
		this.name = name;
		this.x = x;
		this.y = y;
	}

	Animation<TextureRegion>[] initializeTextureRegion(String path) {
		TextureRegion[][] textureRegions = TextureRegion.split(new Texture(path), 38, 26);
		@SuppressWarnings("unchecked")
		Animation<TextureRegion>[] animation = new Animation[textureRegions.length];
		for (int i = 0; i < textureRegions.length; i++) {
			animation[i] = new Animation<TextureRegion>(10f / 30f, textureRegions[i]);
		}
		return animation;
	}

	public void move() {
		switch (direction) {
		case 0: // N
			y += GameInfo.ONE_TILE;
			if (y > GameInfo.HEIGHT - GameInfo.ONE_TILE) {
				y = GameInfo.HEIGHT - GameInfo.ONE_TILE;
			}
			break;
		case 1: // W
			x -= GameInfo.ONE_TILE;
			if (x < 0) {
				x = 0;
			}
			break;
		case 2: // S
			y -= GameInfo.ONE_TILE;
			if (y < 0) {
				y = 0;
			}
			break;
		case 3: // E
			x += GameInfo.ONE_TILE;
			if (x > GameInfo.WIDTH - GameInfo.ONE_TILE) {
				x = GameInfo.WIDTH - GameInfo.ONE_TILE;
			}
			break;
		}
	}

	public void render(SpriteBatch batch, float elapsedTime) {
		batch.draw(texture[direction].getKeyFrame(elapsedTime, true), x, y, GameInfo.ONE_TILE, GameInfo.ONE_TILE);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Animation<TextureRegion>[] getTexture() {
		return texture;
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
