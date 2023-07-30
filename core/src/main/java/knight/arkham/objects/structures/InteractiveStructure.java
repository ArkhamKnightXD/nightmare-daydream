package knight.arkham.objects.structures;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

import static knight.arkham.helpers.Constants.PIXELS_PER_METER;

public abstract class InteractiveStructure {
    protected final Rectangle actualBounds;
    protected final World actualWorld;
    protected final Fixture fixture;
    protected final Body body;
    private final TiledMap tiledMap;

    public InteractiveStructure(Rectangle rectangle, World world, TiledMap tiledMap) {

        actualBounds = rectangle;
        actualWorld = world;
        this.tiledMap = tiledMap;

        fixture = createFixture();

        body = fixture.getBody();
    }

    protected abstract Fixture createFixture();

    protected TiledMapTileLayer.Cell getObjectCellInTheTileMap() {

        TiledMapTileLayer mapLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Terrain");

        return mapLayer.getCell((int) (body.getPosition().x * PIXELS_PER_METER / 16),
            (int) (body.getPosition().y * PIXELS_PER_METER / 16));
    }
}
