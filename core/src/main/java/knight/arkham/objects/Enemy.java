package knight.arkham.objects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import knight.arkham.helpers.AssetsHelper;
import knight.arkham.helpers.Box2DBody;
import knight.arkham.helpers.Box2DHelper;

public class Enemy extends GameObject {
    private final Animation<TextureRegion> runningAnimation;
    private float stateTimer;

    //bolean variables by default initialize on false. If I want the variable to start on false,
    // I don't have to initialized on false.
    public boolean isMovingRight;
    private boolean setToDestroy;
    private boolean isDestroyed;
    private final TextureRegion region;

    public Enemy(Rectangle rectangle, World world, TextureRegion region) {
        super(
            rectangle, world,
            new TextureRegion(region, 0, 0, 16, 16)
        );

        this.region = region;

        body.setActive(false);

        stateTimer = 0;

        runningAnimation = makeAnimationByFrameRange(region, 0, 1, 0.4f);
    }

    @Override
    protected Body createBody() {

        return Box2DHelper.createBody(

            new Box2DBody(actualBounds, BodyDef.BodyType.DynamicBody,
                10, actualWorld, this)
        );
    }

    private void destroyEnemy() {

        actualWorld.destroyBody(body);
        isDestroyed = true;

        setActualRegion(new TextureRegion(region, 32, 0, 16, 16));

        stateTimer = 0;
    }

    public void update(float deltaTime) {

        //Debido a que estoy haciendo += con mi deltaTime, esta variable ira guardando
        // los segundos que han pasado, desde que empezó mi gameLoop. Esta variable es básicamente un contador.
        stateTimer += deltaTime;

        if (setToDestroy && !isDestroyed)
            destroyEnemy();

        else if (!isDestroyed && body.isActive()) {

            setActualRegion(runningAnimation.getKeyFrame(stateTimer, true));

            if (isMovingRight && body.getLinearVelocity().x <= 4)
                applyLinealImpulse(new Vector2(2, 0));

            else if (!isMovingRight && body.getLinearVelocity().x >= -4)
                applyLinealImpulse(new Vector2(-2, 0));

            if (getPixelPosition().y < -50)
                setToDestroy = true;
        }
    }

    @Override
    public void draw(Batch batch) {
        if (!isDestroyed || stateTimer < 1)
            super.draw(batch);
    }

    public void hitOnHead() {
        setToDestroy = true;

        Sound sound = AssetsHelper.loadSound("stomp.wav");

        sound.play();
    }
}
