package gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.utils.Array;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import configuration.Configuration;
import configuration.Settings;
import gameworld.GameWorld;
import helpers.AssetLoader;
import tweens.SpriteAccessor;
import tweens.VectorAccessor;

/**
 * Created by ManuGil on 22/03/15.
 */
public class Coin {

    private GameWorld world;
    private int x, y;
    private float radius;
    private Sprite sprite, backsprite;
    private Body body, point;
    private ParticleEffect effect;
    private TweenManager manager;
    public Circle circle;
    DistanceJointDef jointDef;
    private CoinState coinState;

    public enum CoinState {COLLECTED, IDLE}

    private Vector2 randomP;

    public Coin(GameWorld world, int x, int y, float radius) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.radius = radius;

        coinState = CoinState.IDLE;

        sprite = new Sprite(AssetLoader.coin);
        sprite.setPosition(x, y);
        sprite.setSize(radius * 2, radius * 2);
        sprite.setRotation(MathUtils.random(0, 360));
        sprite.setOriginCenter();
        sprite.setAlpha(0.8f);

        backsprite = new Sprite(AssetLoader.coin);
        backsprite.setSize(radius * 2 + 10, radius * 2 + 10);
        backsprite.setAlpha(0.3f);
        backsprite.setRotation(MathUtils.random(0, 360));
        backsprite.setOriginCenter();

        circle = new Circle(x, y, radius + Settings.COIN_COLLISION_MARGIN);

        BodyDef bodyDefC = new BodyDef();
        bodyDefC.type = BodyDef.BodyType.DynamicBody;
        bodyDefC.position.set(sprite.getX() / world.PIXELS_TO_METERS,
                sprite.getY() / world.PIXELS_TO_METERS);

        body = world.getWorldB().createBody(bodyDefC);
        body.setAngularDamping(0.5f);
        body.setLinearDamping(0.1f);
        //body.setFixedRotation(true);
        body.setLinearVelocity(MathUtils.random(-2, 2), MathUtils.random(-2, 2));
        body.setGravityScale(0);

        BodyDef bodyDefP = new BodyDef();
        bodyDefP.type = BodyDef.BodyType.DynamicBody;
        bodyDefP.position.set((sprite.getX()) / world.PIXELS_TO_METERS,
                (sprite.getY() + 10) / world.PIXELS_TO_METERS);
        point = world.getWorldB().createBody(bodyDefP);
        point.setGravityScale(0);

        CircleShape shape = new CircleShape();
        shape.setPosition(new Vector2(sprite.getWidth() / 2 / world.PIXELS_TO_METERS,
                sprite.getHeight()
                        / 2 / world.PIXELS_TO_METERS));
        shape.setRadius(radius / world.PIXELS_TO_METERS);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0.05f;
        fixtureDef.filter.categoryBits = Settings.CATEGORY_COIN;
        fixtureDef.filter.maskBits = Settings.MASK_COIN;
        //TODO: Check multicursor stuff in Android Studio

        //DISTANCE JOINT
        jointDef = new DistanceJointDef();
        jointDef.bodyA = body;

        jointDef.bodyB = point;

        jointDef.initialize(body, point,
                new Vector2(body.getPosition().x + (radius / world.PIXELS_TO_METERS),
                        body.getPosition().y + (radius / world.PIXELS_TO_METERS)),
                new Vector2(point.getPosition().x + (radius / world.PIXELS_TO_METERS),
                        point.getPosition().y + (radius / world.PIXELS_TO_METERS)));
        jointDef.dampingRatio = .3f;
        jointDef.frequencyHz = 50;
        jointDef.length = Settings.COIN_JOINT_DISTANCE / world.PIXELS_TO_METERS;
        jointDef.collideConnected = false;
        body.createFixture(fixtureDef);
        //point.createFixture(fixtureDefP);
        world.getWorldB().createJoint(jointDef);
        //world.getWorldB().createJoint(jointDef2);
        shape.dispose();

        //PARTICLE EFFECT
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("coin.p"), Gdx.files.internal(""));
        effect.setPosition(-300, -300);


        //TWEENS
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.registerAccessor(Vector2.class, new VectorAccessor());
        manager = new TweenManager();
        randomP = world.getPointsDir().get(MathUtils.random(0, world.getPointsDir().size - 1));
        sprite.setAlpha(0);
        start();

    }

    public void start() {
        reset();
    }

    public void update(float delta) {

        manager.update(delta);
        sprite.setPosition((body.getPosition().x * world.PIXELS_TO_METERS),
                (body.getPosition().y * world.PIXELS_TO_METERS));

        circle.setPosition(body.getWorldPoint(body.getLocalCenter()).x * world.PIXELS_TO_METERS,
                body.getWorldPoint(body.getLocalCenter()).y * world.PIXELS_TO_METERS);
        point.setTransform(randomP.x / world.PIXELS_TO_METERS, randomP.y / world.PIXELS_TO_METERS,
                0);
        limitVel();
        if (Settings.COIN_PARTICLES) {
            effect.update(delta);
            effect.setPosition(body.getWorldPoint(body.getLocalCenter()).x * world.PIXELS_TO_METERS,
                    body.getWorldPoint(body.getLocalCenter()).y * world.PIXELS_TO_METERS);
        }
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
        sprite.setOrigin(0, 0);
        outOfBounds();

    }

    private void outOfBounds() {
        if (body.getPosition().y * world.PIXELS_TO_METERS > (world.gameHeight + (sprite
                .getHeight() / 2))) {
            reset();
        } else if (body.getPosition().y * world.PIXELS_TO_METERS < -sprite
                .getHeight() / 2) {
            reset();
        }
        if (body.getPosition().x * world.PIXELS_TO_METERS > (world.gameWidth + (sprite
                .getWidth() / 2))) {
            reset();
        } else if (body.getPosition().x * world.PIXELS_TO_METERS < -sprite
                .getWidth() / 2) {
            reset();
        }
    }

    private void limitVel() {
        if (body.getLinearVelocity().y > Settings.COIN_MAX_VEL) {
            body.setLinearVelocity(body.getLinearVelocity().x, Settings.COIN_MAX_VEL);
        }
        if (body.getLinearVelocity().y < -Settings.COIN_MAX_VEL) {
            body.setLinearVelocity(body.getLinearVelocity().x, -Settings.COIN_MAX_VEL);
        }

        if (body.getLinearVelocity().x > Settings.COIN_MAX_VEL) {
            body.setLinearVelocity(Settings.COIN_MAX_VEL, body.getLinearVelocity().y);
        }
        if (body.getLinearVelocity().x < -Settings.COIN_MAX_VEL) {
            body.setLinearVelocity(-Settings.COIN_MAX_VEL, body.getLinearVelocity().y);
        }
    }

    public void render(SpriteBatch batcher, ShapeRenderer shapeRenderer) {

        sprite.draw(batcher);
        if (sprite.getColor().a >= .6f) {
            if (Settings.COIN_PARTICLES) {
                effect.draw(batcher);
            }
        }

        if (Configuration.DEBUG) {
            batcher.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.circle(circle.x, circle.y, circle.radius);
            shapeRenderer.end();
            batcher.begin();
        }
    }

    public void reset() {
        manager.killAll();
        coinState = CoinState.IDLE;
        //
        //body.setAngularVelocity(MathUtils.random(-2,2));
        randomP = world.getPointsDir().get(MathUtils.random(0, world.getPointsDir().size - 1));
        body.setTransform((randomP.x + 10) / world.PIXELS_TO_METERS,
                randomP.y / world.PIXELS_TO_METERS,
                0);
        point.setTransform(randomP.x / world.PIXELS_TO_METERS, randomP.y / world.PIXELS_TO_METERS,
                0);
        body.applyForce(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f), 4, 4, true);
        jointDef = new DistanceJointDef();
        jointDef.bodyA = body;

        jointDef.bodyB = point;

        jointDef.initialize(body, point,
                new Vector2(body.getPosition().x + (radius / world.PIXELS_TO_METERS),
                        body.getPosition().y + (radius / world.PIXELS_TO_METERS)),
                new Vector2(point.getPosition().x + (radius / world.PIXELS_TO_METERS),
                        point.getPosition().y + (radius / world.PIXELS_TO_METERS)));
        jointDef.dampingRatio = 1f;
        jointDef.frequencyHz = 50;
        jointDef.length = Settings.COIN_JOINT_DISTANCE / world.PIXELS_TO_METERS;
        jointDef.collideConnected = false;
        //point.createFixture(fixtureDefP);
        world.getWorldB().createJoint(jointDef);
        //fadeIn(MathUtils.random(.4f, .4f), MathUtils.random(.1f, .7f));
        sprite.setAlpha(1);


    }

    public void scale(float from, float duration, float delay) {
        sprite.setScale(from);
        Tween.to(sprite, SpriteAccessor.SCALE, duration).target(1).delay(delay)
                .ease(TweenEquations.easeOutBounce).start(manager);
    }

    public void fadeIn(float duration, float delay) {
        sprite.setAlpha(0);
        Tween.to(sprite, SpriteAccessor.ALPHA, duration).target(.75f).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }


    public void fadeOut(float duration, float delay) {
        sprite.setAlpha(.75f);
        TweenCallback cbFadeOut = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                reset();
            }
        };
        Tween.to(sprite, SpriteAccessor.ALPHA, duration).target(.0f).setCallbackTriggers(
                TweenCallback.COMPLETE).setCallback(cbFadeOut).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }

    public void fadeOutNothing(float duration, float delay) {
        sprite.setAlpha(.75f);
        TweenCallback cbFadeOut = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                reset();
            }
        };
        Tween.to(sprite, SpriteAccessor.ALPHA, duration).target(.0f).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(manager);
    }

    public void end() {
        effect.reset();
        fadeOutNothing(.3f, .3f);
    }

    public void collide() {

        if (coinState != CoinState.COLLECTED) {
            AssetLoader.success.play();
            removeBodySafely(body);
            fadeOut(.4f, .0f);
            effect.reset();
            world.addScore(1);
            if (world.getScore() == 1) {
                //Stop the flashing
                world.getHero().godTween.free();
                world.getHero().inmortal.setValue(0);
            }

        }

        coinState = CoinState.COLLECTED;
        //point.createFixture(fixtureDefP);


    }

    public void removeBodySafely(Body body) {
        //to prevent some obscure c assertion that happened randomly once in a blue moon
        final Array<JointEdge> list = body.getJointList();
        while (list.size > 0) {
            world.getWorldB().destroyJoint(list.get(0).joint);
        }
        // actual remove
    }

    public Body getBody() {
        return body;
    }


}
