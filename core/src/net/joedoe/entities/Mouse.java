package net.joedoe.entities;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Texture;
import net.joedoe.logics.pathfinding.Graph;
import net.joedoe.logics.pathfinding.ManhattanHeuristic;
import net.joedoe.logics.pathfinding.Node;

public class Mouse extends Actor {
    public Graph graph;
    public DefaultGraphPath<Node> path;
    public IndexedAStarPathFinder<Node> pathfinder;
    public ManhattanHeuristic heuristic;
    public int pathIndex = 1;

    public Mouse(String name, float x, float y, int strength) {
        super(new Texture("entities/enemy.png"), name, x, y, strength);
        path = new DefaultGraphPath<Node>();
        heuristic = new ManhattanHeuristic();
    }

    public boolean calculatePath(Node startNode, Node endNode) {
        pathIndex = 1;
        path.clear();
        return pathfinder.searchNodePath(startNode, endNode, heuristic, path);
    }

    public Node getCurrentNode() {
        return path.nodes.get(pathIndex - 1);
    }

    public Node getNextNode() {
        return path.nodes.get(pathIndex);
    }

    public Node getAfterNextNode() {
        if (path.nodes.size > pathIndex + 1) {
            return path.nodes.get(pathIndex + 1);
        }
        return null;
    }
}
