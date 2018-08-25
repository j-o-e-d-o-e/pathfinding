package net.joedoe.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;
import net.joedoe.GameInfo;

public class ManhattanHeuristic implements Heuristic<Node> {

    @Override
    public float estimate(Node startNode, Node endNode) {
        int startY = startNode.getIndex() / GameInfo.MAP_WIDTH;
        int startX = startNode.getIndex() % GameInfo.MAP_WIDTH;
        int endY = endNode.getIndex() / GameInfo.MAP_WIDTH;
        int endX = endNode.getIndex() % GameInfo.MAP_WIDTH;
        return Math.abs(startX - endX) + Math.abs(startY - endY);
    }
}