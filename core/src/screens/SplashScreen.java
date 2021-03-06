package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import configuration.Configuration;
import helpers.AssetLoader;
import noon.ActionResolver;
import noon.NoonGame;
import tweens.SpriteAccessor;
import tweens.Value;
import tweens.ValueAccessor;

/**
 * Created by ManuGil on 09/03/15.
 */
public class SplashScreen implements Screen {

    private TweenManager manager;
    private SpriteBatch batcher;
    private Sprite sprite;
    private NoonGame game;
    private ActionResolver actionResolver;
    private Sprite background;
    private Sprite spriteBack;
    private float width = Gdx.graphics.getWidth();
    private float height = Gdx.graphics.getHeight();
    float desiredWidth;
    float scale;

    public SplashScreen(NoonGame game, ActionResolver actionResolver) {
        this.game = game;
        this.actionResolver = actionResolver;
        spriteBack = new Sprite(AssetLoader.background);
        //spriteBack.setColor(FlatColors.WHITE);
        spriteBack.setPosition(0, 0);
        spriteBack.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        actionResolver.viewAd(false);
    }

    @Override
    public void show() {
        sprite = new Sprite(AssetLoader.logo);
        sprite.setColor(1, 1, 1, 0);

        desiredWidth = width * .2f;
        scale = desiredWidth / sprite.getWidth();

        sprite.setSize(sprite.getWidth() * scale, sprite.getHeight() * scale);
        sprite.setPosition((width / 2) - (sprite.getWidth() / 2), (height / 2)
                - (sprite.getHeight() / 2));
        sprite.setAlpha(0);
        setupTween();
        batcher = new SpriteBatch();
    }

    private void setupTween() {
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();

        TweenCallback cb = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                game.setScreen(new GameScreen(game, actionResolver));
            }
        };

        if (!Configuration.SPLASHSCREEN) {
            game.setScreen(new GameScreen(game, actionResolver));
        }
        Tween.to(sprite, SpriteAccessor.ALPHA, 1.5f).target(1).delay(.7f)
                .ease(TweenEquations.easeInOutQuad).repeatYoyo(1, .2f)
                .setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE)
                .start(manager);
        // Tween.to(r1, -1, 1f).target(0 - sprite.getHeight())
        // .ease(TweenEquations.easeInOutQuad).delay(1.6f).start(manager);
    }

    @Override
    public void render(float delta) {
        manager.update(delta);
        Gdx.gl.glClearColor(92.5f, 94.1f, 94.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batcher.begin();
        spriteBack.draw(batcher);
        sprite.draw(batcher);
        batcher.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}