package knight.arkham.helpers;

import com.badlogic.gdx.physics.box2d.*;
import knight.arkham.objects.Enemy;
import knight.arkham.objects.Player;
import knight.arkham.objects.structures.Checkpoint;
import knight.arkham.objects.structures.FinishFlag;

import static knight.arkham.helpers.Constants.*;

public class GameContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int collisionDefinition = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (collisionDefinition) {

            case PLAYER_BIT | ENEMY_HEAD_BIT:

                if (fixtureA.getFilterData().categoryBits == ENEMY_HEAD_BIT)
                    ((Enemy) fixtureA.getUserData()).hitOnHead();

                else
                    ((Enemy) fixtureB.getUserData()).hitOnHead();
                break;


            case PLAYER_BIT | ENEMY_BIT:

                if (fixtureA.getFilterData().categoryBits == PLAYER_BIT)
                    ((Player) fixtureA.getUserData()).getHitByEnemy();

                else
                    ((Player) fixtureB.getUserData()).getHitByEnemy();
                break;


            case PLAYER_BIT | CHECKPOINT_BIT:

                if (fixtureA.getFilterData().categoryBits == CHECKPOINT_BIT)
                    ((Checkpoint) fixtureA.getUserData()).createCheckpoint();

                else
                    ((Checkpoint) fixtureB.getUserData()).createCheckpoint();
                break;

            case PLAYER_BIT | FINISH_BIT:

                if (fixtureA.getFilterData().categoryBits == FINISH_BIT)
                    ((FinishFlag) fixtureA.getUserData()).finishLevel();

                else
                    ((FinishFlag) fixtureB.getUserData()).finishLevel();
                break;
        }


    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
