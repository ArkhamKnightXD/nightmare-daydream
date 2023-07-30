package knight.arkham.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import knight.arkham.helpers.Box2DBody;
import knight.arkham.helpers.Box2DHelper;
import knight.arkham.helpers.GameDataHelper;

import static knight.arkham.helpers.Constants.GAME_DATA_FILENAME;


public class Player extends GameObject {
    private final TextureRegion jumpingRegion;
    private final TextureRegion standingRegion;
    private PlayerAnimationState currentState;
    private PlayerAnimationState previousState;
    private final Animation<TextureRegion> runningAnimation;
    private float stateTimer;
    private boolean isPlayerRunningRight;
    private int jumpCounter;


    public Player(Rectangle bounds, World world, TextureRegion actualRegion) {
        super(
            bounds, world,
            new TextureRegion(actualRegion, 0, 0, 16, 16)
        );

        previousState = PlayerAnimationState.STANDING;
        currentState = PlayerAnimationState.STANDING;

        stateTimer = 0;

        standingRegion = new TextureRegion(actualRegion, 0, 0, 16, 16);
        jumpingRegion = new TextureRegion(actualRegion, 80, 0, 16, 16);

        runningAnimation = makeAnimationByFrameRange(actualRegion, 1, 3, 0.1f);
    }

    @Override
    protected Body createBody() {

        return Box2DHelper.createBody(

            new Box2DBody(
                actualBounds, BodyDef.BodyType.DynamicBody,
                10, actualWorld, this
            )
        );
    }

    public void update(float deltaTime) {

        setActualRegion(getAnimationRegion(deltaTime));

        keyboardControllers();

        playerFallToDead();
    }

    private void keyboardControllers() {

        if (Gdx.input.isKeyPressed(Input.Keys.D) && body.getLinearVelocity().x <= 10)
            applyLinealImpulse(new Vector2(5, 0));

        else if (Gdx.input.isKeyPressed(Input.Keys.A) && body.getLinearVelocity().x >= -10)
            applyLinealImpulse(new Vector2(-5, 0));

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && jumpCounter < 2){
            applyLinealImpulse(new Vector2(0, 140));

            jumpCounter++;
        }

        if (body.getLinearVelocity().y == 0)
            jumpCounter = 0;
    }

    private void playerFallToDead() {

        if (getPixelPosition().y < -100) {

            body.setLinearVelocity(0, 0);

            Vector2 position = GameDataHelper.loadGameData(GAME_DATA_FILENAME).position;

            body.setTransform(position, 0);
        }
    }

    private PlayerAnimationState getPlayerCurrentState() {

        boolean isPlayerMoving = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.D);

        if (body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && previousState == PlayerAnimationState.JUMPING))
            return PlayerAnimationState.JUMPING;

        else if (isPlayerMoving)
            return PlayerAnimationState.RUNNING;

        else if (body.getLinearVelocity().y < 0)
            return PlayerAnimationState.FALLING;

        else
            return PlayerAnimationState.STANDING;
    }


    private TextureRegion getAnimationRegion(float deltaTime) {

        currentState = getPlayerCurrentState();

        TextureRegion region;

        switch (currentState) {

            case JUMPING:
                region = jumpingRegion;
                break;

            case RUNNING:
                region = runningAnimation.getKeyFrame(stateTimer, true);
                break;

            case FALLING:
            case STANDING:
            default:
                region = standingRegion;
        }

        flipPlayerOnXAxis(region);

        stateTimer = currentState == previousState ? stateTimer + deltaTime : 0;
        previousState = currentState;

        return region;
    }

    private void flipPlayerOnXAxis(TextureRegion region) {

        if ((body.getLinearVelocity().x < 0 || !isPlayerRunningRight) && !region.isFlipX()) {

            region.flip(true, false);
            isPlayerRunningRight = false;
        } else if ((body.getLinearVelocity().x > 0 || isPlayerRunningRight) && region.isFlipX()) {

            region.flip(true, false);
            isPlayerRunningRight = true;
        }
    }

    public void getHitByEnemy() {

        applyLinealImpulse(new Vector2(500, 0));
    }

    public float getDistanceInBetween(Vector2 finalPosition) {

//        .dst utiliza la fórmula de calcular la distancia entre 2 puntos.
        return getPixelPosition().dst(finalPosition);
    }
}
