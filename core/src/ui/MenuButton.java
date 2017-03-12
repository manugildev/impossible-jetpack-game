package ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import aurelienribon.tweenengine.TweenCallback;
import gameobjects.GameObject;
import gameworld.GameWorld;
import tweens.Value;

/**
 * Created by ManuGil on 14/03/15.
 */
public class MenuButton extends GameObject {

    private Color color;
    private Value time = new Value();
    private TweenCallback cbGameScreen;

    public MenuButton(final GameWorld world, float x, float y, float width, float height,
                      TextureRegion texture, Color color) {
        super(world, x, y, width, height, texture, color);
        this.color = color;
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {

        super.render(batch, shapeRenderer);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

    }



}
