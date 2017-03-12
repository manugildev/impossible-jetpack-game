package gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import configuration.Configuration;
import configuration.Settings;
import gameworld.GameWorld;
import helpers.AssetLoader;
import helpers.FlatColors;
import helpers.Rumble;
import tweens.SpriteAccessor;
import tweens.Value;
import tweens.VectorAccessor;

/**
 * Created by ManuGil on 20/03/15.
 */
public class Hero {
    private GameWorld world;
    private int x, y;
    private float width, height;
    private Sprite sprite;
    private Body body;
    public boolean clickedRight, clickedLeft;
    private ParticleEffect effect, explosion;
    private TweenManager manager;
    public Rectangle rectangle;
    private TweenCallback cbFinish, cbInmortal, cbPhoto;
    private ExecutorService executor;

    public enum HeroState {DEAD, ALIVE}

    public HeroState heroState;
    Tween sound;

    Value second = new Value();
    Value inmortal = new Value();
    Value photoSecond = new Value();

    public Rumble rumble;
    public Tween godTween;

    public Hero(final GameWorld world, int x, int y, float width, float height) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        executor = Executors.newFixedThreadPool(25);
        heroState = HeroState.DEAD;
        sprite = new Sprite(AssetLoader.colorCircle);

        sprite.setPosition(x, y);
        sprite.setSize(width, height);
        rectangle = new Rectangle(x, y, width, height);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth() / 2) /
                        world.PIXELS_TO_METERS,
                (sprite.getY() + sprite.getHeight() / 2) / world.PIXELS_TO_METERS);
        bodyDef.fixedRotation = true;


        body = world.getWorldB().createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2 / world.PIXELS_TO_METERS, sprite.getHeight()
                / 2 / world.PIXELS_TO_METERS);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.17f;
        fixtureDef.restitution = 1f;
        fixtureDef.friction = 0.1f;
        fixtureDef.filter.categoryBits = Settings.CATEGORY_HERO;
        fixtureDef.filter.maskBits = Settings.MASK_HERO;
        body.createFixture(fixtureDef);
        shape.dispose();

        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("jetpack.p"), Gdx.files.internal(""));
        effect.setPosition(-100, -100);

        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("explosion.p"), Gdx.files.internal(""));
        explosion.setPosition(-100, -100);

        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.registerAccessor(Vector2.class, new VectorAccessor());
        manager = new TweenManager();
        second.setValue(0);
        sound = Tween.to(second, 0, 0).target(1).start(manager);


        this.rumble = new Rumble(world);

        cbFinish = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                world.finishGame();
            }
        };
        cbPhoto = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                //ScreenshotFactory.saveScreenshot();

            }
        };
        fadeIn(.3f, .1f);


        cbInmortal = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                inmortal.setValue(0);
                godTween = Tween.to(inmortal, -1, Settings.INITIAL_FLASH_TIME).target(1)
                        .setCallback(cbInmortal)
                        .setCallbackTriggers(TweenCallback.COMPLETE).target(
                                1).start(manager);
            }
        };
        inmortal.setValue(0);
        godTween = Tween.to(inmortal, -1, Settings.INITIAL_FLASH_TIME).target(1)
                .setCallback(cbInmortal)
                .setCallbackTriggers(TweenCallback.COMPLETE).target(
                        1).start(manager);
    }

    public void update(float delta) {
        manager.update(delta);

        if (heroState == HeroState.ALIVE) {
            if (inmortal.getValue() > 0.5) {
                sprite.setColor(FlatColors.BLUE);
            } else {
                sprite.setColor(FlatColors.WHITE);
            }
            sprite.setPosition((body.getPosition().x * world.PIXELS_TO_METERS) - sprite.
                            getWidth() / 2,
                    (body.getPosition().y * world.PIXELS_TO_METERS) - sprite.getHeight() / 2);
            rectangle.setPosition((body.getPosition().x * world.PIXELS_TO_METERS) - sprite.
                            getWidth() / 2,
                    (body.getPosition().y * world.PIXELS_TO_METERS) - sprite.getHeight() / 2);
            explosion.setPosition(
                    body.getWorldPoint(body.getLocalCenter()).x * world.PIXELS_TO_METERS,
                    body.getWorldPoint(body.getLocalCenter()).y * world.PIXELS_TO_METERS);

        }
        effect.update(delta);
        explosion.update(delta);


        // Ditto for rotation

        sprite.setOriginCenter();
        if (heroState == HeroState.ALIVE) {
            if (body.getLinearVelocity().y > Settings.MAX_Y_VEL) {
                body.setLinearVelocity(body.getLinearVelocity().x, Settings.MAX_Y_VEL);
            } else {
                if (clickedRight) {
                    body.applyForceToCenter(Settings.JETPACK_X_ACCELERATION,
                            Settings.JETPACK_Y_ACCELERATION, true);
                    //effect.setPosition(sprite.getX() + 5, sprite.getY() + (sprite.getWidth() / 2));

                } else if (clickedLeft) {
                    body.applyForceToCenter(-Settings.JETPACK_X_ACCELERATION,
                            Settings.JETPACK_Y_ACCELERATION, true);
                    //effect.setPosition(sprite.getX() + sprite.getWidth() - 5,                        sprite.getY() + (sprite.getWidth() / 2));

                } else {
                    // effect.setPosition(sprite.getX() + sprite.getWidth() / 2,                sprite.getY() + sprite.getHeight() - 10);

                }
                if (Settings.JETPACK_PARTICLES) {
                    effectPosition();
                }
            }
            limitVel();
            outOfBounds();
        }
        if (rumble.time > 0) {
            rumble.tick(delta, new Vector2(world.gameWidth / 2, world.gameHeight / 2));
        }


        //DEAD CHECK
        if (heroState == HeroState.DEAD) {
            body.setLinearVelocity(0, 0);
            body.setGravityScale(0);
        }
    }

    private void effectPosition() {
        if (clickedLeft || clickedRight) {
            if (second.getValue() == 1) {
                second.setValue(0);
                sound = Tween.to(second, -1, Settings.JETPACK_SOUND_REPETITION_TIME).target(1)
                        .start(manager);
                AssetLoader.jetpack.play(.05f);
                //Gdx.app.log("Playing Sound", new Date()+"");
            }
            if (sprite.isFlipX()) {
                effect.setPosition(
                        sprite.getX() + sprite.getWidth() - Settings.LOCAL_JETPACK_LOCATION_X,
                        sprite.getY() + (sprite.getWidth() / 2));
            } else {
                effect.setPosition(sprite.getX() + Settings.LOCAL_JETPACK_LOCATION_X,
                        sprite.getY() + (sprite.getWidth() / 2));
            }

        } else {
            effect.setPosition(sprite.getX() + sprite.getWidth() / 2,
                    sprite.getY() + sprite.getHeight() - 10);
        }
    }


    private void limitVel() {
        if (body.getLinearVelocity().y < Settings.MIN_Y_VEL) {
            body.setLinearVelocity(body.getLinearVelocity().x, Settings.MIN_Y_VEL);
        }
        if (body.getLinearVelocity().y > Settings.MAX_Y_VEL) {
            body.setLinearVelocity(body.getLinearVelocity().x, Settings.MAX_Y_VEL);
        }
        if (body.getLinearVelocity().x > Settings.MAX_X_VEL) {
            body.setLinearVelocity(Settings.MAX_X_VEL, body.getLinearVelocity().y);
        }
        if (body.getLinearVelocity().x < Settings.MIN_X_VEL) {
            body.setLinearVelocity(Settings.MIN_X_VEL, body.getLinearVelocity().y);
        }
    }


    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if ((clickedLeft || clickedRight) && Settings.JETPACK_PARTICLES) effect.draw(batch);

        sprite.draw(batch);

        if (heroState == HeroState.DEAD) {

            explosion.draw(batch);

        }

        if (Configuration.DEBUG) {
            batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.rect(rectangle.x, rectangle.y + 10, rectangle.width,
                    rectangle.height - 10);
            shapeRenderer.end();
            batch.begin();
        }

    }

    private void outOfBounds() {
        if (body.getPosition().y * world.PIXELS_TO_METERS > (world.gameHeight + (sprite
                .getHeight() / 2))) {
            body.setTransform(body.getPosition().x,
                    (((-sprite.getHeight() / 2)) / world.PIXELS_TO_METERS), 0);
        } else if (body.getPosition().y * world.PIXELS_TO_METERS < -sprite
                .getHeight() / 2) {
            body.setTransform(body.getPosition().x,
                    (world.gameHeight + (sprite.getHeight() / 2)) / world.PIXELS_TO_METERS, 0);
        }
        if (body.getPosition().x * world.PIXELS_TO_METERS > (world.gameWidth + (sprite
                .getWidth() / 2))) {
            body.setTransform((((-sprite.getWidth() / 2)) / world.PIXELS_TO_METERS),
                    body.getPosition().y,
                    0);
        } else if (body.getPosition().x * world.PIXELS_TO_METERS < -sprite
                .getWidth() / 2) {
            body.setTransform((world.gameWidth + (sprite.getWidth() / 2)) / world.PIXELS_TO_METERS,
                    body.getPosition().y, 0);
        }
    }

    public void clickedLeft() {
        sprite.setFlip(true, false);
        world.getHero().getBody().applyForceToCenter(0, 7, true);
        world.getHero().clickedLeft = true;
        effect.reset();
        effect.start();
        rotateEffect(+20);

    }

    public void clickedRight() {
        sprite.setFlip(false, false);
        world.getHero().getBody().applyForceToCenter(0, 7, true);
        world.getHero().clickedRight = true;
        effect.reset();
        effect.start();
        rotateEffect(-20);
    }

    public void notClickedLeft() {
        world.getHero().getBody().applyForceToCenter(+Settings.JETPACK_X_DECELERATION,
                -Settings.JETPACK_Y_DECELERATION, true);
        world.getHero().clickedLeft = false;
        rotateEffect(0);
    }

    public void notClickedRight() {
        world.getHero().getBody().applyForceToCenter(-Settings.JETPACK_X_DECELERATION,
                -Settings.JETPACK_Y_DECELERATION, true);
        world.getHero().clickedRight = false;

        rotateEffect(0);
    }

    private void rotateEffect(int i) {
        Tween.to(sprite, SpriteAccessor.ANGLE, 0.6f).target(i).ease(
                TweenEquations.easeInOutSine).start(manager);
    }

    public Body getBody() {
        return body;
    }

    public void start() {
        heroState = HeroState.ALIVE;
        body.setGravityScale(1);
    }

    public Vector2 getPosition() {
        return new Vector2(sprite.getX(), sprite.getY());
    }

    public void collide() {
        if (heroState == HeroState.ALIVE) {
            explosion.reset();
            explosion.start();
            body.setGravityScale(0);
            fadeOut(.3f, .5f);
            rumble.rumble(Settings.RUMBLE_POWER, Settings.RUMBLE_TIME);
            AssetLoader.explosion.play();
            finish();
        }
        heroState = HeroState.DEAD;
    }

    public void fadeOut(float duration, float delay) {
        sprite.setAlpha(1);
        Tween.to(sprite, SpriteAccessor.ALPHA, duration).target(.0f).setCallbackTriggers(
                TweenCallback.COMPLETE).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }

    private void finish() {
        second.setValue(0);
        Tween.to(second, -1, 1f).setCallback(cbFinish).setCallbackTriggers(
                TweenCallback.COMPLETE).target(1).start(manager);

        photoSecond.setValue(0);
        Tween.to(photoSecond, -1, Settings.PHOTO_WAIT_TIME).setCallback(cbPhoto)
                .setCallbackTriggers(
                        TweenCallback.COMPLETE).target(1).start(manager);
    }

    public void fadeIn(float duration, float delay) {
        sprite.setAlpha(0);
        Tween.to(sprite, SpriteAccessor.ALPHA, duration).target(1).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }


}
