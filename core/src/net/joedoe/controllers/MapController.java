package net.joedoe.controllers;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import net.joedoe.GameInfo;
import net.joedoe.entities.MapEntity;
import net.joedoe.pathfinding.Graph;
import net.joedoe.pathfinding.GraphGenerator;

class MapController {
    private TiledMap map;

    MapController(String path) {
        map = new TmxMapLoader().load(path);
    }

    boolean currentTileIsAccessible(float x, float y) {
        Cell cell = ((TiledMapTileLayer) map.getLayers().get("top")).getCell((int) x / GameInfo.ONE_TILE, (int) y / GameInfo.ONE_TILE);
        return cell == null;
    }

    float[] getCoordinatesOfNextTile(MapEntity mapEntity) {
        float[] nextTile = new float[]{mapEntity.getX(), mapEntity.getY()};
        switch (mapEntity.getDirection()) {
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

    Graph generateGraph() {
        return new GraphGenerator(map).generateGraph();
    }

    TiledMap getMap() {
        return map;
    }

    void dispose() {
        map.dispose();
    }
}
