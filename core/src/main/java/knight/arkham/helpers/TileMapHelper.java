package knight.arkham.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import knight.arkham.objects.Enemy;
import knight.arkham.objects.Player;
import knight.arkham.objects.structures.Checkpoint;
import knight.arkham.objects.structures.FinishFlag;

import static knight.arkham.helpers.Constants.MID_SCREEN_WIDTH;
import static knight.arkham.helpers.Constants.PIXELS_PER_METER;

public class TileMapHelper {
    private final World world;
    private final TiledMap tiledMap;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final TextureRegion enemyRegion;
    private final Array<Enemy> enemies;
    private FinishFlag finishFlag;

    public TileMapHelper(World world, TextureAtlas textureAtlas, String mapFilePath) {

        this.world = world;
        enemyRegion = textureAtlas.findRegion("goomba");
        enemies = new Array<>();
        tiledMap = new TmxMapLoader().load(mapFilePath);
        mapRenderer = setupMap();
    }

    public OrthogonalTiledMapRenderer setupMap() {

        MapLayers mapLayers = tiledMap.getLayers();

        for (MapLayer mapLayer : mapLayers){

            parseMapObjectsToBox2DBodies(mapLayer.getObjects(), mapLayer.getName());
        }

        return new OrthogonalTiledMapRenderer(tiledMap, 1 / PIXELS_PER_METER);
    }

    private void parseMapObjectsToBox2DBodies(MapObjects mapObjects, String objectsName) {

        for (MapObject mapObject : mapObjects) {

            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

            Rectangle box2dRectangle = createBox2dRectangle(rectangle);

            switch (objectsName) {

                case "Enemies":
                    enemies.add(new Enemy(box2dRectangle, world, enemyRegion));
                    break;

                case "Checkpoints":
                    new Checkpoint(box2dRectangle, world, tiledMap);
                    break;

                case "FinishFlag":
                    finishFlag = new FinishFlag(box2dRectangle, world, tiledMap);
                    break;

                default:
                    Box2DHelper.createBody(new Box2DBody(box2dRectangle, world));
                    break;
            }
        }
    }

    private Rectangle createBox2dRectangle(Rectangle rectangle){
        return new Rectangle(
            rectangle.x + rectangle.width / 2,
            rectangle.y + rectangle.height / 2,
            rectangle.width, rectangle.height
        );
    }

    public boolean isPlayerInsideMapBounds(Vector2 playerPixelPosition) {

        MapProperties properties = tiledMap.getProperties();

        int mapWidth = properties.get("width", Integer.class);
        int tilePixelWidth = properties.get("tilewidth", Integer.class);

        int mapPixelWidth = mapWidth * tilePixelWidth;

        return playerPixelPosition.x > MID_SCREEN_WIDTH && playerPixelPosition.x < mapPixelWidth - MID_SCREEN_WIDTH;
    }

    public void updateCameraPosition(Player player, OrthographicCamera camera) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.F3))
            camera.zoom += 0.2f;

        if (Gdx.input.isKeyJustPressed(Input.Keys.F4))
            camera.zoom -= 0.2f;

        boolean isPlayerInsideMapBounds = isPlayerInsideMapBounds(player.getPixelPosition());

        if (isPlayerInsideMapBounds)
            camera.position.set(player.getWorldPosition().x, 9.5f, 0);

        camera.update();
    }

    public void update(float deltaTime, Player player, OrthographicCamera camera){

        world.step(1 / 60f, 6, 2);

        player.update(deltaTime);

        updateCameraPosition(player, camera);

        for (Enemy enemy : enemies){

            if (player.getDistanceInBetween(enemy.getPixelPosition()) < 170)
                enemy.getBody().setActive(true);

            enemy.update(deltaTime);
        }
    }


    public void draw(OrthographicCamera camera, Player player){

        mapRenderer.setView(camera);

        mapRenderer.render();

        //If I'm using an OrthogonalMapRender, I may as well use the built-in spriteBatch.
        // So this way I only have to use this instead of having the standard spriteBatch
        mapRenderer.getBatch().setProjectionMatrix(camera.combined);

        mapRenderer.getBatch().begin();

        player.draw(mapRenderer.getBatch());

        mapRenderer.getBatch().end();
    }

    public void dispose(){

//        finishFlag.dispose();
        tiledMap.dispose();
        mapRenderer.dispose();
        world.dispose();
        enemyRegion.getTexture().dispose();

        for (Enemy enemy : enemies)
            enemy.dispose();
    }
}
