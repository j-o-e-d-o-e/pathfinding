package net.joedoe.logics;

import java.util.ArrayList;

import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Texture;

import net.joedoe.GameInfo;
import net.joedoe.entities.Actor;
import net.joedoe.entities.Enemy;
import net.joedoe.logics.pathfinding.GraphGenerator;
import net.joedoe.logics.pathfinding.Node;

public class FightLogic extends MapLogic {
	private ArrayList<Enemy> enemies;
	private GraphGenerator graphGenerator;
	private String playerXY, enemy1XY, enemy2XY;
	public int roundsLeft;

	public FightLogic(String path) {
		super(path);
		graphGenerator = new GraphGenerator(map);
		initializePlayer();
		initializeEnemies();
	}

	void initializePlayer() {
		float x = GameInfo.WIDTH / 2f - GameInfo.ONE_TILE * 19;
		float y = GameInfo.HEIGHT - GameInfo.ONE_TILE * 13;
		player = new Actor(new Texture("entities/player.png"), "Player", x, y, 8);
		playerXY = (int) player.getX() / GameInfo.ONE_TILE + "/" + (int) player.getY() / GameInfo.ONE_TILE;
	}

	void initializeEnemies() {
		enemies = new ArrayList<Enemy>();
		float x1 = GameInfo.WIDTH / 2f + GameInfo.ONE_TILE * 16;
		float y1 = GameInfo.HEIGHT - GameInfo.ONE_TILE * 15;
		Enemy enemy = new Enemy("Enemy 1", x1, y1, 6);
		enemies.add(enemy);
		enemy1XY = (int) enemy.getX() / GameInfo.ONE_TILE + "/" + (int) enemy.getY() / GameInfo.ONE_TILE;
		float x2 = GameInfo.WIDTH / 2f + GameInfo.ONE_TILE * 10;
		float y2 = GameInfo.HEIGHT - GameInfo.ONE_TILE * 7;
		enemy = new Enemy("Enemy 2", x2, y2, 6);
		enemies.add(new Enemy("Enemy 2", x2, y2, 6));
		enemy2XY = (int) enemy.getX() / GameInfo.ONE_TILE + "/" + (int) enemy.getY() / GameInfo.ONE_TILE;
	}

	public boolean collidesWithActor(Actor actor) {
		float[] nextTile = getCoordinatesOfNextTile(actor);
		for (Enemy enemy : enemies) {
			if (nextTile[0] == enemy.getX() && nextTile[1] == enemy.getY()) {
				return true;
			}
		}
		return false;
	}

	public void updateEnemies() {
		for (Enemy enemy : enemies) {
			if (!enemy.hasMoved) {
				enemy.graph = graphGenerator.generateGraph(enemy, enemies);
				enemy.pathfinder = new IndexedAStarPathFinder<Node>(enemy.graph, true);
				Node start = enemy.graph.getNodeByCoordinates(enemy.getX(), enemy.getY());
				Node end = enemy.graph.getNodeByCoordinates(player.getX(), player.getY());
				if (enemy.calculatePath(start, end)) {
					Node nextNode = enemy.getNextNode();
					// SET DIRECTION:
					if (nextNode.getY() * GameInfo.ONE_TILE > enemy.getY()) {
						enemy.setDirection(1); // N
					} else if (nextNode.getX() * GameInfo.ONE_TILE < enemy.getX()) {
						enemy.setDirection(2); // W
					} else if (nextNode.getY() * GameInfo.ONE_TILE < enemy.getY()) {
						enemy.setDirection(3); // S
					} else if (nextNode.getX() * GameInfo.ONE_TILE > enemy.getX()) {
						enemy.setDirection(4); // E
					}
					// DISTANCE BETWEEN PLAYER & ENEMY:
					int distance = 0;
					for (int i = enemy.pathIndex - 1; i < enemy.path.getCount() - 1; i++) {
						distance++;
					}
					if (distance > 1) {
						enemy.move();
						enemy.pathIndex++;
						if (enemy.getName() == "Enemy 1") {
							enemy1XY = (int) enemy.getX() / GameInfo.ONE_TILE + "/"
									+ (int) enemy.getY() / GameInfo.ONE_TILE;
						} else if (enemy.getName() == "Enemy 2") {
							enemy2XY = (int) enemy.getX() / GameInfo.ONE_TILE + "/"
									+ (int) enemy.getY() / GameInfo.ONE_TILE;
						}
					} else {
						enemy.endTurn();
					}
				} else {
					enemy.endTurn();
				}
			} else {
				if (enemiesHaveMoved() && enemy == enemies.get(enemies.size() - 1)) {
					player.setHasMoved(false);
				}
			}
		}
	}

	public boolean enemiesHaveMoved() {
		for (Enemy enemy : enemies) {
			if (!enemy.hasMoved) {
				return false;
			}
		}
		return true;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public GraphGenerator getGraphGenerator() {
		return graphGenerator;
	}

	public void setGraphGenerator(GraphGenerator graphGenerator) {
		this.graphGenerator = graphGenerator;
	}

	public String getPlayerXY() {
		return playerXY;
	}

	public void setPlayerXY(String playerXY) {
		this.playerXY = playerXY;
	}

	public String getEnemy1XY() {
		return enemy1XY;
	}

	public String getEnemy2XY() {
		return enemy2XY;
	}

	public void dispose() {
		player.getTexture().dispose();
		for (Enemy enemy : enemies) {
			enemy.getTexture().dispose();
		}
		map.dispose();
	}
}