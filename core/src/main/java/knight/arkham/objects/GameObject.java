package knight.arkham.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import knight.arkham.helpers.Box2DHelper;

import static knight.arkham.helpers.Constants.PIXELS_PER_METER;

public abstract class GameObject {
    protected final Rectangle actualBounds;
    protected final World actualWorld;
    protected final Body body;
    private TextureRegion actualRegion;

    protected GameObject(Rectangle bounds, World world, TextureRegion region) {

        actualBounds = bounds;
        actualWorld = world;
        actualRegion = region;

        body = createBody();
    }

    protected abstract Body createBody();

    public void draw(Batch batch) {

        Rectangle drawBounds = Box2DHelper.getDrawBounds(actualBounds, body);

        batch.draw(actualRegion, drawBounds.x, drawBounds.y, drawBounds.width, drawBounds.height);
    }

    protected Animation<TextureRegion> makeAnimationByFrameRange(TextureRegion characterRegion, int initialFrame, int finalFrame, float frameDuration) {

        Array<TextureRegion> animationFrames = new Array<>();

        for (int i = initialFrame; i <= finalFrame; i++)
            animationFrames.add(new TextureRegion(characterRegion, i * 16, 0, 16, 16));

        return new Animation<>(frameDuration, animationFrames);
    }

    public Body getBody() {return body;}

    protected void applyLinealImpulse(Vector2 impulseDirection) {
        body.applyLinearImpulse(impulseDirection, body.getWorldCenter(), true);
    }

    public Vector2 getPixelPosition() {

        return new Vector2(body.getPosition().x * PIXELS_PER_METER, body.getPosition().y * PIXELS_PER_METER);
    }

    public Vector2 getWorldPosition() {return body.getPosition();}

    protected void setActualRegion(TextureRegion actualRegion) {this.actualRegion = actualRegion;}

    public void dispose() {actualRegion.getTexture().dispose();}
}
