package screenlogics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import gameobjects.Background;
import gameworld.GameState;
import gameworld.GameWorld;
import helpers.AssetLoader;
import tweens.Value;
import ui.Text;

/**
 * Created by ManuGil on 25/03/15.
 */
public class Pause {
    private GameWorld world;
    private TweenManager manager;
    private TweenCallback cbPause;
    private Background background;
    private Text pauseText;
    private Value second = new Value();

    public Pause(final GameWorld world) {
        this.world = world;
        background = new Background(this.world, 0, 0, world.gameWidth, world.gameHeight,
                AssetLoader.background, Color.WHITE);
        manager = new TweenManager();
        cbPause = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                pauseText.setX(-world.gameWidth);
                world.gameState = GameState.RUNNING;
            }
        };

        pauseText = new Text(world, 0, 0, world.gameWidth, world.gameHeight / 2 + 100,
                AssetLoader.transparent, Color.WHITE, "Pause", AssetLoader.fontS,
                Color.WHITE, 30,
                BitmapFont.HAlignment.CENTER);
    }

    public void start() {
        world.gameState = GameState.PAUSE;
        background.fadeIn(0.5f, .5f, .1f);
        pauseText.effectX(-world.gameWidth, 0, .5f, .1f);
    }

    public void update(float delta) {
        manager.update(delta);
        pauseText.update(delta);
        background.update(delta);
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, ShaderProgram fontshader) {
        background.render(batch, shapeRenderer);
        pauseText.render(batch, shapeRenderer, fontshader);
    }

    public void finish() {
        background.fadeOut(.5f, .1f);
        pauseText.effectX(0, world.gameWidth, .5f, .1f);
        AssetLoader.woosh.play();
        Tween.to(second, -1, .65f).target(1).setCallback(cbPause)
                .setCallbackTriggers(TweenCallback.COMPLETE).start(manager);
    }


}
