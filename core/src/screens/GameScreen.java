package screens;

/**
 * Created by ManuGil on 09/03/15.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import noon.ActionResolver;
import gameworld.GameRenderer;
import gameworld.GameWorld;
import helpers.InputHandler;
import noon.NoonGame;

public class GameScreen implements Screen {

    private GameWorld world;
    private GameRenderer renderer;
    private float runTime;
    public float sW = Gdx.graphics.getWidth();
    public float sH = Gdx.graphics.getHeight();
    public float gameHeight = 1080;
    public float gameWidth = sW / (sH / gameHeight);
    public float w = 1080 / 100;
    private NoonGame game;
    private ActionResolver actionResolver;

    public float worldWidth = gameWidth * 1;
    public float worldHeight = gameHeight * 1;


    public GameScreen(NoonGame game, ActionResolver actionResolver) {
        this.game = game;
        this.actionResolver = actionResolver;
        Gdx.app.log("GameScreen", "Attached");
        Gdx.app.log("GameWidth " + gameWidth, "GameHeight " + gameHeight);
        world = new GameWorld(game, actionResolver, gameWidth, gameHeight, worldWidth, worldHeight);
        Gdx.input.setInputProcessor(new InputHandler(world, sW / gameWidth, sH
                / gameHeight));
        renderer = new GameRenderer(world, (int) gameWidth, (int) gameHeight);
    }

    @Override
    public void render(float delta) {
        runTime += delta;
        world.update(delta);
        renderer.render(delta, runTime);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen", "show called");
    }

    @Override
    public void hide() {
        Gdx.app.log("GameScreen", "hide called");
        world.setPauseMode();
    }

    @Override
    public void pause() {
        Gdx.app.log("GameScreen", "pause called");
        world.setPauseMode();
    }

    @Override
    public void resume() {
        Gdx.app.log("GameScreen", "resume called");
    }

    @Override
    public void dispose() {

    }
}
