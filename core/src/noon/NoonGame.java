package noon;

import com.badlogic.gdx.Game;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import helpers.AssetLoader;
import screens.Boot;

public class NoonGame extends Game {
	private ActionResolver actionresolver;
    public Executor executor;


    public NoonGame(ActionResolver actionresolver) {
        this.actionresolver = actionresolver;
        executor = Executors.newFixedThreadPool(25);
    }

    @Override
    public void create() {
        AssetLoader.load();
        setScreen(new Boot(this, actionresolver));
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }
}
