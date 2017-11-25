package net.joedoe.logics;

import java.util.ArrayList;

import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import net.joedoe.GameInfo;
import net.joedoe.entities.Actor;
import net.joedoe.entities.Mouse;
import net.joedoe.logics.pathfinding.GraphGenerator;
import net.joedoe.logics.pathfinding.Node;

public class MazeLogic {
	private TiledMap map;
	private ArrayList<Mouse> mice;
	private GraphGenerator graphGenerator;
	private String cheeseXY, mouse1XY, mouse2XY;
	private float cheeseX, cheeseY;

	public MazeLogic(String path) {
		map = new TmxMapLoader().load(path);
		graphGenerator = new GraphGenerator(map);
		initializeMice();
	}

	void initializeMice() {
		mice = new ArrayList<Mouse>();
		float x1 = GameInfo.WIDTH / 2f + GameInfo.ONE_TILE * 19;
		float y1 = GameInfo.HEIGHT - GameInfo.ONE_TILE * 22;
		Mouse mouse = new Mouse("Pinky", x1, y1, 6);
		mice.add(mouse);
		mouse1XY = (int) mouse.getX() / GameInfo.ONE_TILE + "/" + (int) mouse.getY() / GameInfo.ONE_TILE;
		float x2 = GameInfo.WIDTH / 2f + GameInfo.ONE_TILE * 19;
		float y2 = GameInfo.HEIGHT - GameInfo.ONE_TILE * 2;
		mouse = new Mouse("The Brain", x2, y2, 6);
		mice.add(mouse);
		mouse2XY = (int) mouse.getX() / GameInfo.ONE_TILE + "/" + (int) mouse.getY() / GameInfo.ONE_TILE;
	}

	public boolean tileIsAccessible(int tileX, int tileY) {
		Cell cell = ((TiledMapTileLayer) map.getLayers().get("top")).getCell(tileX, tileY);
		if (cell == null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean nextTileIsAccessible(Actor actor) {
		float[] nextTile = getCoordinatesOfNextTile(actor);
		Cell cell = ((TiledMapTileLayer) map.getLayers().get("top")).getCell((int) nextTile[0] / GameInfo.ONE_TILE,
				(int) nextTile[1] / GameInfo.ONE_TILE);
		if (cell == null) {
			return true;
		} else {
			return false;
		}
	}

	public float[] getCoordinatesOfNextTile(Actor actor) {
		float[] nextTile = new float[] { actor.getX(), actor.getY() };
		switch (actor.getDirection()) {
		case 1: // N
			nextTile[1] += GameInfo.ONE_TILE;
			break;
		case 2: // W
			nextTile[0] -= GameInfo.ONE_TILE;
			break;
		case 3: // S
			nextTile[1] -= GameInfo.ONE_TILE;
			break;
		case 4: // E
			nextTile[0] += GameInfo.ONE_TILE;
			break;
		}
		return nextTile;
	}

	public boolean collidesWithActor(float x, float y) {
		for (Mouse mouse : mice) {
			if (x == mouse.getX() && y == mouse.getY()) {
				return true;
			}
		}
		return false;
	}

	public boolean collidesWithActor(Actor actor) {
		float[] nextTile = getCoordinatesOfNextTile(actor);
		for (Mouse mouse : mice) {
			if (nextTile[0] == mouse.getX() && nextTile[1] == mouse.getY()) {
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
				Node end = mouse.graph.getNodeByCoordinates(cheeseX, cheeseY);
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
					if (distance > 1) {
						mouse.move();
						mouse.pathIndex++;
						if (mouse == mice.get(0)) {
							mouse1XY = (int) mouse.getX() / GameInfo.ONE_TILE + "/"
									+ (int) mouse.getY() / GameInfo.ONE_TILE;
						} else if (mouse == mice.get(1)) {
							mouse2XY = (int) mouse.getX() / GameInfo.ONE_TILE + "/"
									+ (int) mouse.getY() / GameInfo.ONE_TILE;
						}
					} else {
						mouse.hasMoved = true;
					}
				} else {
					mouse.hasMoved = true;
				}
			} else {
				if (miceHaveMoved() && mouse == mice.get(mice.size() - 1)) {
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

	public ArrayList<Mouse> getMice() {
		return mice;
	}

	public GraphGenerator getGraphGenerator() {
		return graphGenerator;
	}

	public void setGraphGenerator(GraphGenerator graphGenerator) {
		this.graphGenerator = graphGenerator;
	}

	public TiledMap getMap() {
		return map;
	}

	public String getCheeseXY() {
		return cheeseXY;
	}

	public void setCheeseXY(String cheeseXY) {
		this.cheeseXY = cheeseXY;
	}

	public float getCheeseX() {
		return cheeseX;
	}

	public void setCheeseX(float cheeseX) {
		this.cheeseX = cheeseX;
	}

	public float getCheeseY() {
		return cheeseY;
	}

	public void setCheeseY(float cheeseY) {
		this.cheeseY = cheeseY;
	}

	public String getMouse1XY() {
		return mouse1XY;
	}

	public String getMouse2XY() {
		return mouse2XY;
	}

	public void dispose() {
		map.dispose();
	}
}