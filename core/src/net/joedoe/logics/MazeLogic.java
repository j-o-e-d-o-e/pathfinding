package net.joedoe.logics;

import java.util.ArrayList;

import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import net.joedoe.GameInfo;
import net.joedoe.entities.Mouse;
import net.joedoe.logics.pathfinding.GraphGenerator;
import net.joedoe.logics.pathfinding.Node;

public class MazeLogic {
	private TiledMap map;
	private GraphGenerator graphGenerator;
	private ArrayList<Mouse> mice;
	private Texture textureCheese;
	private float[] cheese;

	public MazeLogic(String path) {
		map = new TmxMapLoader().load(path);
		graphGenerator = new GraphGenerator(map);
		textureCheese = new Texture("entities/cheese.png");
		initializeMice();
	}

	void initializeMice() {
		mice = new ArrayList<Mouse>();
		float x1 = GameInfo.WIDTH / 2f + GameInfo.ONE_TILE * 19;
		float y1 = GameInfo.HEIGHT - GameInfo.ONE_TILE * 21;
		mice.add(new Mouse("Pinky", x1, y1));
		float x2 = GameInfo.WIDTH / 2f + GameInfo.ONE_TILE * 19;
		float y2 = GameInfo.HEIGHT - GameInfo.ONE_TILE * 2;
		mice.add(new Mouse("The Brain", x2, y2));
//		float x3 = GameInfo.WIDTH / 2f - GameInfo.ONE_TILE * 19;
//		float y3 = GameInfo.HEIGHT - GameInfo.ONE_TILE * 2;
//		mice.add(new Mouse("Mouse 3", x3, y3));
//		float x4 = GameInfo.WIDTH / 2f - GameInfo.ONE_TILE * 19;
//		float y4 = GameInfo.HEIGHT - GameInfo.ONE_TILE * 21;
//		mice.add(new Mouse("Mouse 4", x4, y4));
	}

	public void setCheese(float x, float y) {
		int tileX = (int) x / GameInfo.ONE_TILE;
		int tileY = (int) y / GameInfo.ONE_TILE;
		float px_X = (int) (x / GameInfo.ONE_TILE) * GameInfo.ONE_TILE;
		float px_Y = (int) (y / GameInfo.ONE_TILE) * GameInfo.ONE_TILE;
		if (tileIsAccessible(tileX, tileY) && !collidesWithMouse(px_X, px_Y)) {
			cheese = new float[] { px_X, px_Y };
			GameInfo.cheeseIsSet = true;
		}
	}

	public boolean tileIsAccessible(int tileX, int tileY) {
		Cell cell = ((TiledMapTileLayer) map.getLayers().get("top")).getCell(tileX, tileY);
		if (cell == null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean collidesWithMouse(float x, float y) {
		for (Mouse mouse : mice) {
			if (x == mouse.getX() && y == mouse.getY()) {
				return true;
			}
		}
		return false;
	}

	public void updateMice() {
		for (Mouse mouse : mice) {
			if (!mouse.hasMoved) {
				mouse.graph = graphGenerator.generateGraph(mouse, mice);
				mouse.pathfinder = new IndexedAStarPathFinder<Node>(mouse.graph, true);
				Node start = mouse.graph.getNodeByCoordinates(mouse.getX(), mouse.getY());
				Node end = mouse.graph.getNodeByCoordinates(cheese[0], cheese[1]);
				if (mouse.calculatePath(start, end)) {
					Node nextNode = mouse.getNextNode();
					// SET DIRECTION:
					if (nextNode.getY() * GameInfo.ONE_TILE > mouse.getY()) {
						mouse.setDirection(0); // N
					} else if (nextNode.getX() * GameInfo.ONE_TILE < mouse.getX()) {
						mouse.setDirection(1); // W
					} else if (nextNode.getY() * GameInfo.ONE_TILE < mouse.getY()) {
						mouse.setDirection(2); // S
					} else if (nextNode.getX() * GameInfo.ONE_TILE > mouse.getX()) {
						mouse.setDirection(3); // E
					}
					// DISTANCE BETWEEN CHEESE & MOUSE:
					int distance = 0;
					for (int i = mouse.pathIndex - 1; i < mouse.path.getCount() - 1; i++) {
						distance++;
					}
					// DECISION MAKING:
					if (distance > 1) {
						mouse.move();
						mouse.pathIndex++;
					} else {
						for (Mouse m : mice) {
							m.hasMoved = true;
						}
						GameInfo.cheeseIsSet = false;
					}
				} else {
					mouse.hasMoved = true;
				}
			} else {
				if (miceHaveMoved()) {
					GameInfo.cheeseIsSet = false;
				}
			}
		}
	}

	public boolean miceHaveMoved() {
		for (Mouse mouse : mice) {
			if (!mouse.hasMoved) {
				return false;
			}
		}
		return true;
	}

	public TiledMap getMap() {
		return map;
	}

	public ArrayList<Mouse> getMice() {
		return mice;
	}

	public Texture getTextureCheese() {
		return textureCheese;
	}

	public float[] getCheese() {
		return cheese;
	}

	public void dispose() {
		map.dispose();
		textureCheese.dispose();
	}
}
