package knight.arkham.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import knight.arkham.Nightmare;

import static knight.arkham.helpers.Constants.FULL_SCREEN_HEIGHT;
import static knight.arkham.helpers.Constants.FULL_SCREEN_WIDTH;

public class MainMenuScreen extends ScreenAdapter {
    private final Nightmare game;
    private final Skin skin;
    private final Stage stage;
    private final Table table;
    private final Viewport viewport;
    public MainMenuScreen() {

        game = Nightmare.INSTANCE;

        AssetManager assetManager = new AssetManager();

        AssetDescriptor<Skin> uiSkin = new AssetDescriptor<>("images/ui/uiskin.json", Skin.class, new SkinLoader.SkinParameter("images/ui/uiskin.atlas"));

        assetManager.load(uiSkin);

        assetManager.finishLoading();

        skin = assetManager.get(uiSkin);

//        For menu screen the extendViewport is highly recommended
        viewport = new ExtendViewport(FULL_SCREEN_WIDTH, FULL_SCREEN_HEIGHT);

        stage = new Stage(viewport);

        table = new Table();

        table.setFillParent(true);

        stage.addActor(table);

        addButton("Play").addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen());
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        addButton("Options");
        addButton("Credits");

        addButton("Quit").addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        //De esta forma agrego que mi stage reaccione al hover y click del mouse.
        Gdx.input.setInputProcessor(stage);
    }

    private TextButton addButton(String buttonName) {

        TextButton textButton = new TextButton(buttonName, skin);

//        De esta forma indico que mi botón ocupara la fila completa y quiero agregarle un padding bottom de 10
        table.add(textButton).width(400).height(60).padBottom(15);
        table.row();

        return textButton;
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0, 0);

        stage.act();
        stage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {

        stage.dispose();
        skin.dispose();
    }
}
