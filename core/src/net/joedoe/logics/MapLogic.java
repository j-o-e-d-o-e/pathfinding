package net.joedoe.logics;

import java.util.Iterator;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import net.joedoe.GameInfo;
import net.joedoe.entities.Bullet;
import net.joedoe.entities.MovingEntity;
import net.joedoe.entities.Player;

public class MapLogic {
    protected TiledMap map;
    protected Player player;

    public MapLogic(String path) {
        map = new TmxMapLoader().load(path);
    }

    public boolean nextTileIsAccessible(MovingEntity entity) {
        float[] nextTile = getCoordinatesOfNextTile(entity);
        Cell cell = ((TiledMapTileLayer) map.getLayers().get("middle")).getCell((int) nextTile[0] / GameInfo.ONE_TILE,
                (int) nextTile[1] / GameInfo.ONE_TILE);
        if (cell == null) {
            return true;
        } else {
            if (entity instanceof Bullet) {
                ((Bullet) entity).remove = true;
            }
            return false;
        }
    }

    public float[] getCoordinatesOfNextTile(MovingEntity entity) {
        float[] nextTile = new float[] { entity.getX(), entity.getY() };
        switch (entity.getDirection()) {
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

    public String getTileName() {
        float[] nextTile = getCoordinatesOfNextTile(player);
        float playerX = nextTile[0] / GameInfo.ONE_TILE;
        float playerY = nextTile[1] / GameInfo.ONE_TILE;
        MapObjects objects = map.getLayers().get("objects").getObjects();
        Iterator<MapObject> objectIterator = objects.iterator();
        while (objectIterator.hasNext()) {
            MapObject object = objectIterator.next();
            float placeX = object.getProperties().get("x", float.class) / GameInfo.PIXEL;
            float placeY = object.getProperties().get("y", float.class) / GameInfo.PIXEL;
            if (playerX == placeX && playerY == placeY) {
                return object.getName();
            }
        }
        return null;
    }

    public TiledMap getMap() {
        return map;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
