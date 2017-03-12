package screens;

import com.badlogic.gdx.Screen;

import noon.ActionResolver;
import noon.NoonGame;

/**
 * Created by ManuGil on 25/03/15.
 */
public class Boot implements Screen {
    private final NoonGame game;
    private final ActionResolver actionResolver;
    public Boot(NoonGame game, ActionResolver actionResolver) {

        this.game = game;
        this.actionResolver = actionResolver;
        game.setScreen(new SplashScreen(game, actionResolver));
    }

    @Override
    public void show() {
        game.setScreen(new SplashScreen(game, actionResolver));
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
