package knight.arkham.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import knight.arkham.Nightmare;
import knight.arkham.helpers.GameContactListener;
import knight.arkham.helpers.TileMapHelper;
import knight.arkham.objects.Player;

public class GameScreen extends ScreenAdapter {
    private final Nightmare game;
    private final OrthographicCamera camera;
    private final TileMapHelper dayDreamMapHelper;
    private final TileMapHelper nightmareMapHelper;
    private final World daydreamWorld;
    private final World nightmareWorld;
    private final TextureAtlas textureAtlas;
    private final Player daydreamPlayer;
    private final Player nightmarePlayer;
    private boolean isDebug;
    private boolean isNightmareMode;

    public GameScreen() {
        game = Nightmare.INSTANCE;

        game.setToDispose = false;

        camera = game.camera;

        daydreamWorld = new World(new Vector2(0, -40), true);
        nightmareWorld = new World(new Vector2(0, -40), true);

        daydreamWorld.setContactListener(new GameContactListener());
        nightmareWorld.setContactListener(new GameContactListener());

        textureAtlas = new TextureAtlas("images/atlas/Mario_and_Enemies.pack");

        TextureRegion playerRegion = textureAtlas.findRegion("little_mario");

        daydreamPlayer = new Player(new Rectangle(450, 50, 16, 16), daydreamWorld, playerRegion);
        nightmarePlayer = new Player(new Rectangle(450, 50, 16, 16), nightmareWorld, playerRegion);

        game.saveGameData("GameScreen", daydreamPlayer.getWorldPosition());
        game.saveGameData("GameScreen", nightmarePlayer.getWorldPosition());

        dayDreamMapHelper = new TileMapHelper(daydreamWorld, textureAtlas, "maps/playground/test.tmx");
        nightmareMapHelper = new TileMapHelper(nightmareWorld, textureAtlas, "maps/playground/test2.tmx");
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height);
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0, 0);

        if (isNightmareMode){

            nightmareMapHelper.update(delta, nightmarePlayer, camera);
            drawNightmareLevel();
        }else {
            dayDreamMapHelper.update(delta, daydreamPlayer, camera);
            drawDaydreamLevel();
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.F1))
            isDebug = !isDebug;

        if (Gdx.input.isKeyJustPressed(Input.Keys.F2))
            isNightmareMode = !isNightmareMode;

        game.quitTheGame();
    }

    private void drawDaydreamLevel() {

        if (!isDebug)
            dayDreamMapHelper.draw(camera, daydreamPlayer);

        else
            game.debugRenderer.render(daydreamWorld, camera.combined);
    }

    private void drawNightmareLevel() {

        if (!isDebug)
            nightmareMapHelper.draw(camera, nightmarePlayer);

        else
            game.debugRenderer.render(nightmareWorld, camera.combined);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {

        daydreamPlayer.dispose();
        textureAtlas.dispose();
        dayDreamMapHelper.dispose();
    }
}
