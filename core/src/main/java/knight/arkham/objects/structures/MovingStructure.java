package knight.arkham.objects.structures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import knight.arkham.helpers.Box2DBody;
import knight.arkham.helpers.Box2DHelper;

public class MovingStructure {
    private final Body body;
    private final Texture sprite;
    private float stateTimer;
    private final Vector2 velocity;
    private final Rectangle bounds;

    public MovingStructure(Rectangle rectangle, World world, Vector2 velocity) {

        bounds = rectangle;

        body = Box2DHelper.createBody(

            new Box2DBody(
                rectangle, BodyDef.BodyType.KinematicBody,
                0, world, this
            )
        );

        sprite = new Texture("images/MovingStructure.jpg");

        this.velocity = velocity;

        body.setLinearVelocity(velocity);
    }


//    Any moving object will always need a draw method.
    public void draw(Batch batch) {

        Rectangle drawBounds = Box2DHelper.getDrawBounds(bounds, body);

        batch.draw(sprite, drawBounds.x, drawBounds.y, drawBounds.width, drawBounds.height);
    }

    public void update(float deltaTime){

        stateTimer += deltaTime;

        if (stateTimer > 5){

            //Multiply the vector velocity by -1 to change direction every 5 sec
            velocity.scl(-1);

            body.setLinearVelocity(velocity);

            stateTimer = 0;
        }
    }

    public void dispose() {sprite.dispose();}
}
