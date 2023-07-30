package knight.arkham.objects.structures;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import knight.arkham.helpers.AssetsHelper;
import knight.arkham.helpers.Box2DBody;
import knight.arkham.helpers.Box2DHelper;

import static knight.arkham.helpers.Constants.DESTROYED_BIT;

public class Checkpoint extends InteractiveStructure {

    public Checkpoint(Rectangle rectangle, World world, TiledMap tiledMap) {
        super(rectangle, world, tiledMap);
    }

    @Override
    protected Fixture createFixture() {

        return Box2DHelper.createStaticFixture(
            new Box2DBody(actualBounds, actualWorld, this)
        );
    }

    public void createCheckpoint() {

        Filter filter = new Filter();

        filter.categoryBits = DESTROYED_BIT;
        fixture.setFilterData(filter);

        Sound sound = AssetsHelper.loadSound("coin.wav");
        sound.play();

//        Journey.INSTANCE.saveGameData("GameScreen", body.getPosition());

        getObjectCellInTheTileMap().setTile(null);
    }
}
