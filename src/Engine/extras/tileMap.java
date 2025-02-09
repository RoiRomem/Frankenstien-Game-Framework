package Engine.extras;

import Engine.GameObject;
import Engine.Engine;

public class tileMap {
    private final int tileX,tileY;
    private final int mapWidth,mapHeight;
    private final int mapStartX,mapStartY;
    private final Engine e;

    private GameObject[] tileMap;
    public boolean hasCollider = true;
    public int tileMass = 100;

    public tileMap(Engine engine, int tileX, int tileY, int mapWidth, int mapHeight) {
        this.e = engine;
        this.tileX = tileX;
        this.tileY = tileY;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.mapStartX = 0;
        this.mapStartY = 0;

        this.tileMap = new GameObject[mapWidth*mapHeight]; // Access via: tilemap[row * COLS + col]
    }

    public tileMap(Engine engine, int tileX, int tileY, int mapWidth, int mapHeight, int mapStartX, int mapStartY) {
        this.e = engine;
        this.tileX = tileX;
        this.tileY = tileY;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.mapStartX = mapStartX;
        this.mapStartY = mapStartY;

        tileMap = new GameObject[mapWidth*mapHeight];
    }

    public void setTile(int x, int y, GameObject tile) {
        tile.setHeight(tileY);
        tile.setWidth(tileX);
        tile.setX(mapStartX+tileX*x);
        tile.setY(mapStartY+tileY*y);
        tileMap[x*mapWidth+y] = tile;
        e.addGameObject(tile);
        if (hasCollider) {
            e.addRigidObject(tile, tileMass);
        }
    }

    public GameObject getTile(int x, int y) {
        return tileMap[x*mapWidth+y];
    }

    public void removeTile(int x, int y) {
        tileMap[x*mapWidth+y] = null;
    }
}
