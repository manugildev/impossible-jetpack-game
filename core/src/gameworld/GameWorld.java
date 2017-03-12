package gameworld;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import configuration.Configuration;
import configuration.Settings;
import gameobjects.Background;
import gameobjects.Coin;
import gameobjects.Hero;
import gameobjects.Meteor;
import gameobjects.Star;
import helpers.AssetLoader;
import helpers.FlatColors;
import noon.ActionResolver;
import noon.NoonGame;
import screenlogics.Gameover;
import screenlogics.Menu;
import screenlogics.Pause;
import ui.MenuButton;
import ui.MuteButton;
import ui.Text;

/**
 * Created by ManuGil on 09/03/15.
 */

public class GameWorld {

    public final float w;
    //GENERAL VARIABLES
    public float gameWidth;
    public float gameHeight;
    public float worldWidth;
    public float worldHeight;

    public ActionResolver actionResolver;
    public NoonGame game;
    public GameWorld world = this;

    //GAME CAMERA
    private GameCam camera;

    //VARIABLES
    public GameState gameState;
    private int score;
    private final int numberOfStars = Settings.NUMBER_INITIAL_BACKGROUND_STARS;
    private final int numberOfMeteors = Settings.NUMBER_INITIAL_METEORS;
    public final int numberOfCoins = Settings.NUMBER_INITIAL_COINS;
    private final int numberOfPoints = 15;
    public final int marginOfPoints = -50;


    //GAMEOBJECTS
    private Background background, tutorial;
    private Array<Star> stars = new Array<Star>();
    private Array<Meteor> meteors = new Array<Meteor>();
    private Array<Vector2> points = new Array<Vector2>();
    private Array<Vector2> pointsDir = new Array<Vector2>();
    private Array<Coin> coins = new Array<Coin>();
    private Meteor meteor;
    private Coin coin;
    private Hero hero;
    public MuteButton muteButton;
    public MenuButton pauseButton;

    //UI
    private Menu menu;
    private Gameover gameover;
    private Pause pause;
    private Text scoreText;

    //BOX2D
    private World worldB;
    Body body;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    Sprite sprite;

    public final float PIXELS_TO_METERS = 100f;
    public boolean clicked, clickedLeft, clickedRight;

    public GameWorld(NoonGame game, ActionResolver actionResolver, float gameWidth,
                     float gameHeight, float worldWidth, float worldHeight) {
        this.gameWidth = gameWidth;
        this.w = gameHeight / 100;
        this.gameHeight = gameHeight;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.game = game;
        this.actionResolver = actionResolver;

        //TODO: Remove this line
        actionResolver.viewAd(false);
        camera = new GameCam(this, 0, 0, gameWidth, gameHeight);
        gameState = GameState.MENU;
        startGame();

    }

    public void reset() {

        background = new Background(this, -100, -100, gameWidth + 200, gameHeight + 200,
                AssetLoader.background, Color.WHITE);
        tutorial = new Background(this, 0, 0, gameWidth, gameHeight, AssetLoader.tutorial,
                Color.WHITE);
        menu = new Menu(this);
        gameover = new Gameover(this);
        pause = new Pause(this);
        stars.clear();
        for (int i = 0; i < numberOfStars; i++) {
            stars.add(new Star(world));
        }

        points.clear();
        pointsDir.clear();
        for (int i = 1; i < numberOfPoints - 1; i++) {
            points.add(new Vector2(marginOfPoints, gameHeight / (numberOfPoints + 1) * (i + 1)));
            points.add(new Vector2(gameWidth - marginOfPoints,
                    gameHeight / (numberOfPoints + 1) * (i + 1)));
            points.add(new Vector2(gameWidth / (numberOfPoints + 1) * (i + 1), marginOfPoints));
            points.add(new Vector2(gameWidth / (numberOfPoints + 1) * (i + 1),
                    gameHeight - marginOfPoints));
            //TODO: More spawn points for the coins
            for (int j = 0; j < numberOfPoints - 1; j++) {
                pointsDir.add(new Vector2((int) (gameWidth / ((numberOfPoints)) * (j + 1)),
                        (int) (gameHeight / ((numberOfPoints) + 1) * (i + 1))));
            }

        }

        //BOX2D
        worldB = new World(new Vector2(0, Settings.WORLD_GRAVITY), true);
        debugRenderer = new Box2DDebugRenderer();
        //CREATING HERO
        hero = new Hero(this, (int) (gameWidth / 2 - 35), (int) (gameHeight / 2 - 45), 70, 70);

        //CREATING METEORS
        meteors.clear();
        int j = 0;
        for (int i = 0; i < numberOfMeteors; i++) {
            meteor = new Meteor(this, -100, -100, 20);
            meteors.add(meteor);
        }

        //CREATING COINS
        coins.clear();
        for (int i = 0; i < numberOfCoins; i++) {
            Vector2 p = pointsDir.get(MathUtils.random(0, pointsDir.size - 1));
            coin = new Coin(this, (int) p.x, (int) p.y, 20);
            coins.add(coin);
        }

        scoreText = new Text(this, 0, 0, gameWidth, gameHeight,
                AssetLoader.transparent, Color.WHITE, score + "", AssetLoader.fontS,
                Color.WHITE, 30,
                BitmapFont.HAlignment.CENTER);

        muteButton = new MuteButton(40 + 40, gameHeight - 40 - ((202 * 80 / 256) / 2), 80,
                202 * 80 / 256,
                AssetLoader.soundButton, AssetLoader.muteButton, FlatColors.WHITE);
        muteButton.fadeIn(.9f, .8f, .1f);
        checkIfMusicWasPlaying();

        pauseButton = new MenuButton(this, gameWidth - 40 - 80,
                gameHeight - 40 - ((202 * 80 / 256)), 60, 202 * 80 / 256, AssetLoader.pauseButton,
                Color.WHITE);
        //menu.start(.6f);
    }


    private void checkIfMusicWasPlaying() {
        if (AssetLoader.getVolume()) {
            AssetLoader.music.setLooping(true);
            AssetLoader.music.play();
            AssetLoader.music.setVolume(Settings.MUSIC_VOLUME);
            AssetLoader.setVolume(true);
        }
        if (AssetLoader.music.isPlaying()) {
            world.muteButton.isPressed = true;
        } else {
            world.muteButton.isPressed = false;
        }
    }


    public void update(float delta) {
        //UI
        menu.update(delta);
        gameover.update(delta);
        pause.update(delta);
        muteButton.update(delta);
        pauseButton.update(delta);
        //TEXTS
        scoreText.update(delta);
        scoreText.setText(score + "");

        //GAMEOBJECTS
        for (int i = 0; i < numberOfStars; i++) {
            stars.get(i).update(delta);
        }
        if (!isPause()) {
            worldB.step(1f / 60f, 6, 2);
            hero.update(delta);
        }
        if (isRunning()) {
            collisions();
        } else if (isTutorial()) {
            tutorial.update(delta);
        }
        if (!isPause()) {
            for (int i = 0; i < numberOfMeteors; i++) {
                meteors.get(i).update(delta);
            }
            for (int i = 0; i < numberOfCoins; i++) {
                coins.get(i).update(delta);
            }
        }
        //Gdx.app.log("GameState", gameState.toString());

    }

    private void collisions() {
        for (int i = 0; i < numberOfCoins; i++) {
            if (Intersector.overlaps(coins.get(i).circle, hero.rectangle)) {
                coins.get(i).collide();
            }
        }

        for (int i = 0; i < numberOfMeteors; i++) {
            if (Intersector.overlaps(meteors.get(i).circle, hero.rectangle)) {
                if (score != 0)
                    hero.collide();

            }
        }


    }


    public void render(SpriteBatch batcher, ShapeRenderer shapeRenderer, ShaderProgram fontShader) {
        batcher.end();
        debugMatrix = batcher.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS,
                PIXELS_TO_METERS, 0);
        batcher.begin();
        camera.render(batcher, shapeRenderer);

        //RENDERING GAME OBJECTS
        background.render(batcher, shapeRenderer);
        for (int i = 0; i < numberOfStars; i++) {
            stars.get(i).render(batcher, shapeRenderer);
        }
        for (int i = 0; i < numberOfMeteors; i++) {
            meteors.get(i).render(batcher, shapeRenderer);
        }
        if (!world.isMenu() && !isTransition()) {
            hero.render(batcher, shapeRenderer);
        }
        if (isRunning())
            for (int i = 0; i < numberOfCoins; i++) {
                coins.get(i).render(batcher, shapeRenderer);
            }

        if (isRunning() || isPause()) {
            scoreText.render(batcher, shapeRenderer, fontShader);
        }

        if (isRunning()) {
            pauseButton.render(batcher, shapeRenderer);
        }

        if (isTutorial()) {
            tutorial.render(batcher, shapeRenderer);
        }

        if (isMenu() || isTransition()) {
            menu.render(batcher, shapeRenderer);
        }
        if (isGameOver() || isTransition()) {
            gameover.render(batcher, shapeRenderer, fontShader);
        }

        if (isPause()) {
            pause.render(batcher, shapeRenderer, fontShader);
        }

        //TODO: this
        muteButton.draw(batcher);

        if (Configuration.DEBUG) {
            batcher.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(FlatColors.WHITE);
            for (int i = 0; i < points.size; i++) {
                shapeRenderer.circle(points.get(i).x, points.get(i).y, 4);
            }
            shapeRenderer.setColor(FlatColors.YELLOW);
            for (int i = 0; i < pointsDir.size; i++) {
                shapeRenderer.circle(pointsDir.get(i).x, pointsDir.get(i).y, 4);
            }
            shapeRenderer.end();
            debugRenderer.render(worldB, debugMatrix);
            batcher.begin();
        }

    }

    public void finishGame() {
        saveScoreLogic();
        gameover.start();
        for (int i = 0; i < numberOfCoins; i++) {
            coins.get(i).end();
        }
    }

    private void saveScoreLogic() {
        AssetLoader.addGamesPlayed();
        int gamesPlayed = AssetLoader.getGamesPlayed();

        // GAMES PLAYED ACHIEVEMENTS!
        actionResolver.submitScore(score);
        actionResolver.submitGamesPlayed(gamesPlayed);
        if (score > AssetLoader.getHighScore()) {
            AssetLoader.setHighScore(score);
        }
        //checkAchievements();
    }

    public void startGame() {
        score = 0;
        reset();
    }

    public GameCam getCamera() {
        return camera;
    }

    public int getScore() {
        return score;
    }

    public static Color parseColor(String hex, float alpha) {
        String hex1 = hex;
        if (hex1.indexOf("#") != -1) {
            hex1 = hex1.substring(1);
            // Gdx.app.log("Hex", hex1);
        }
        Color color = Color.valueOf(hex1);
        color.a = alpha;
        return color;
    }

    public Body getBody() {
        return body;
    }

    public boolean isRunning() {
        return gameState == GameState.RUNNING;
    }

    public boolean isTutorial() {
        return gameState == GameState.TUTORIAL;
    }

    public World getWorldB() {
        return worldB;
    }

    public Hero getHero() {
        return hero;
    }

    public Array<Vector2> getPoints() {
        return points;
    }

    public Array<Vector2> getPointsDir() {
        return pointsDir;
    }

    public void finishTutorial() {
        for (int i = 0; i < coins.size; i++) {
            coins.get(i).start();
        }
        tutorial.fadeOutTutorial(.3f, 0f);
    }

    public void addScore(int i) {
        score += 1;
    }

    public boolean isMenu() {
        return gameState == GameState.MENU;
    }

    public Menu getMenu() {
        return menu;
    }

    public Background getTutorial() {
        return tutorial;
    }

    public void resetGAME() {
        score = 0;
        removeBodies();

        hero = new Hero(this, (int) (gameWidth / 2 - 35), (int) (gameHeight / 2 - 45), 70, 70);
        //CREATING METEORS
        meteors.clear();
        for (int i = 0; i < numberOfMeteors; i++) {
            meteor = new Meteor(this, -100, -100, 20);
            meteors.add(meteor);
        }

        //CREATING COINS
        coins.clear();
        for (int i = 0; i < numberOfCoins; i++) {
            Vector2 p = pointsDir.get(MathUtils.random(0, pointsDir.size - 1));
            coin = new Coin(this, (int) p.x, (int) p.y, 20);
            coins.add(coin);
        }
    }

    private void removeBodies() {
        removeBodySafely(hero.getBody());
        for (int i = 0; i < numberOfCoins; i++) {
            removeBodySafely(coins.get(i).getBody());
        }
        for (int i = 0; i < numberOfMeteors; i++) {
            removeBodySafely(meteors.get(i).getBody());
        }

    }

    public void removeBodySafely(Body body) {
        //to prevent some obscure c assertion that happened randomly once in a blue moon
        final Array<JointEdge> list = body.getJointList();
        while (list.size > 0) {
            getWorldB().destroyJoint(list.get(0).joint);
        }
        // actual remove
        worldB.destroyBody(body);
    }

    public boolean isGameOver() {
        return gameState == GameState.GAMEOVER;
    }

    public Gameover getGameOver() {
        return gameover;
    }

    public boolean isTransition() {
        return gameState == GameState.TRANSITION;
    }

    public boolean isPause() {
        return gameState == GameState.PAUSE;
    }

    public void setPauseMode() {
        if (isRunning()) {
            pause.start();
        }
    }

    public void finishPause() {
        pause.finish();
    }

    public NoonGame getGame() {
        return game;
    }
}
