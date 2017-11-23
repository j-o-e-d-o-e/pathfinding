package net.joedoe.logics;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.audio.Sound;

import net.joedoe.GameInfo;
import net.joedoe.entities.Actor;
import net.joedoe.entities.Bullet;
import net.joedoe.entities.Enemy;
import net.joedoe.entities.MovingEntity;
import net.joedoe.entities.Player;
import net.joedoe.helpers.GameManager;
import net.joedoe.logics.pathfinding.GraphGenerator;
import net.joedoe.logics.pathfinding.Node;

public class FightLogic extends MapLogic {
	private ArrayList<Enemy> enemies;
	private ArrayList<Bullet> bullets;
	private GraphGenerator graphGenerator;
	private String message;
	public Sound slap;
	public int roundsLeft;

	public FightLogic(String path) {
		super(path);
		graphGenerator = new GraphGenerator(map);
		initializePlayer();
		initializeEnemies();
	}

	void initializePlayer() {
		player = GameManager.player;
		player.setX(GameInfo.WIDTH / 2f - 9 * GameInfo.ONE_TILE);
		player.setY(GameInfo.HEIGHT - 8 * GameInfo.ONE_TILE);
		player.setTexture("entities/player.png");
		player.setActionPointsToDefault();
		player.hasMoved = false;
	}

	void initializeEnemies() {
		enemies = new ArrayList<Enemy>();
		for (int i = 0; i < 2; i++) {
			String name = "Enemy " + (i + 1);
			float x = GameInfo.WIDTH / 2f + 4 * GameInfo.ONE_TILE;
			float y = GameInfo.HEIGHT - (9 + i * 2) * GameInfo.ONE_TILE;
			int health = 50;
			int strength = 25;
			enemies.add(new Enemy(name, x, y, health, strength));
		}
	}

	public void updateEnemies() {
		for (Enemy enemy : enemies) {
			if (!enemy.hasMoved && !enemy.isDead) {
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
						enemy.setDirection(4); // O
					}
					// DISTANCE BETWEEN PLAYER & ENEMY:
					int distance = 0;
					for (int i = enemy.pathIndex - 1; i < enemy.path.getCount() - 1; i++) {
						distance++;
					}
					if (distance > 1) {
						enemy.move();
						enemy.pathIndex++;
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

	boolean enemiesHaveMoved() {
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void dispose() {
		player.getTexture().dispose();
		for (Enemy enemy : enemies) {
			enemy.getTexture().dispose();
		}
		slap.dispose();
		for (Bullet bullet : bullets) {
			bullet.getShot().dispose();
			bullet.getTexture().dispose();
		}
		map.dispose();
	}
}