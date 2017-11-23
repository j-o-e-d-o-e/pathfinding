package net.joedoe.logics.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class ManhattanHeuristic implements Heuristic<Node> {

    @Override
    public float estimate(Node startNode, Node endNode) {
        int startY = startNode.getIndex() / GraphGenerator.mapWidth;
        int startX = startNode.getIndex() % GraphGenerator.mapWidth;
        int endY = endNode.getIndex() / GraphGenerator.mapWidth;
        int endX = endNode.getIndex() % GraphGenerator.mapWidth;
        return Math.abs(startX - endX) + Math.abs(startY - endY);
    }
}