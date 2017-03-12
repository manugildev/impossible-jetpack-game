package gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import gameworld.GameWorld;
import helpers.AssetLoader;
import helpers.FlatColors;
import tweens.SpriteAccessor;
import tweens.VectorAccessor;

/**
 * Created by ManuGil on 09/02/15.
 */
public class Star {
    private Vector2 position, velocity;
    private Sprite sprite;

    private GameWorld world;
    private int type;
    private int angle, angleInc;

    private TweenManager manager;

    public Star(GameWorld world) {
        this.world = world;
        sprite = new Sprite(AssetLoader.square);

        float size = (float) Math.random() * 3 + 3;

        sprite.setSize(size, size);
        sprite.setPosition((float) Math.random() * world.worldWidth,
                (float) Math.random() * world.worldHeight);
        position = new Vector2(sprite.getX(), sprite.getY());
        velocity = new Vector2((float) Math.random() * 100 + 100,
                (float) Math.random() * -100 - 100);

        int random = (int) (Math.random() * 20 + 0.5f);
        int random1 = (int) (Math.random() * 20 + 0.5f);
        velocity = new Vector2(random1, random);
        if (position.x < world.gameWidth / 2) {
            velocity.x *= -1;
        }
        if (position.y < world.gameHeight / 2) {
            velocity.y *= -1;
        }

        /*if (Math.random() < 0.5f) {
            velocity.x = velocity.x * -1;
        }
        if (Math.random() < 0.5f) {
            velocity.y = velocity.y * -1;
        }*/
        type = Math.random() < 0.5f ? 1 : 0;
        FlatColors colors = new FlatColors();
        colors.organizeColors();

        sprite.setColor(colors.colors.get((int) Math.floor(Math.random() * 15)));
        sprite.setAlpha((float) (Math.random() * 0.7f + 0.05f));
        //sprite.setColor(world.parseColor("#FFFFFF", (float) (Math.random() * 0.5f + 0.05f)));


        //angleInc = (int) (Math.random() * 3);
        //angleInc *= Math.random() < 0.5f ? -1 : 1;

        //TWEEN STUFF
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.registerAccessor(Vector2.class, new VectorAccessor());
        manager = new TweenManager();
        //reset();

    }

    //EFFECTS
    public void fadeIn(float duration, float delay) {
        float finalA = sprite.getColor().a;
        sprite.setAlpha(0);
        Tween.to(sprite, SpriteAccessor.ALPHA, duration).target(finalA).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }

    public void update(float delta) {
        manager.update(delta);
        /*if (position.x < world.getHero()
                .getPoint().x + (world.gameWidth / 2) + 70 && position.x > world.getHero()
                .getPoint().x - (world.gameWidth / 2) - 70 && position.y > world.getHero()
                .getPoint().y - (world.gameHeight / 2) - 70 && position.y < world.getHero()
                .getPoint().y + (world.gameHeight / 2) + 70) {*/
        //angle += angleInc;
        //sprite.setRotation(angle);
        position.add(velocity.cpy().scl(delta));
        sprite.setPosition(position.x, position.y);
        if (sprite.getX() < -20 || sprite.getX() > world.worldWidth + 20) {
            reset();
        }
        if (sprite.getY() < -20 || sprite.getY() > world.worldHeight + 20) {
            reset();
        }
        // }
    }

    private void reset() {

        sprite.setPosition(MathUtils.random(world.gameWidth / 2 - 10, world.gameWidth / 2 + 10),
                MathUtils.random(world.gameHeight / 2 - 10, world.gameHeight / 2 + 10));
        position = new Vector2(sprite.getX(), sprite.getY());
        fadeIn(MathUtils.random(3, 15f), MathUtils.random(1, 15f));
        int random = (int) (Math.random() * 30 + 0.5f);
        int random1 = (int) (Math.random() * 30 + 0.5f);
        velocity = new Vector2(Math.random() < 0.5f ? random1 * -1 : random1,
                Math.random() < 0.5f ? random * -1 : random);
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        /*if (position.x < world.getHero()
                .getPoint().x + (world.gameWidth / 2) + 70 && position.x > world.getHero()
                .getPoint().x - (world.gameWidth / 2) - 70 && position.y > world.getHero()
                .getPoint().y - (world.gameHeight / 2) - 70 && position.y < world.getHero()
                .getPoint().y + (world.gameHeight / 2) + 70) {*/
        //sprite.setColor(world.parseColor("#FFFFFF", 0.5f));
        sprite.draw(batch);
        // }
    }


}
