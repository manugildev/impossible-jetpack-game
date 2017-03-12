package gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import configuration.Configuration;
import helpers.AssetLoader;

public class GameRenderer {

    private GameWorld world;
    private final ShapeRenderer shapeRenderer;
    private ShaderProgram fontShader;
    private Sprite backSprite;
    //GAME OBJECTS
    //private Hero hero;

    private GameCam camera;
    private SpriteBatch batch;
    private Texture texture;
    private Sprite sprite;

    BitmapFont font = new BitmapFont();
    private float angle;

    public GameRenderer(GameWorld world, int gameWidth, int gameHeight) {
        this.world = world;
        sprite = new Sprite(AssetLoader.square);
        sprite.setPosition(0, 0);
        sprite.setSize(world.worldWidth, world.worldHeight);
        camera = world.getCamera();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        initObjects();
        initFont();
    }

    private void initObjects() {

    }

    private void initFont() {
        fontShader = new ShaderProgram(Gdx.files.internal("font.vert"),
                Gdx.files.internal("font.frag"));
        if (!fontShader.isCompiled()) {
            Gdx.app.error("fontShader",
                    "compilation failed:\n" + fontShader.getLog());
        }
    }

    public void render(float delta, float runTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        batch.begin();
        world.render(batch, shapeRenderer, fontShader);
        batch.end();

        //REMOVE THIS OUTSIDE DEBUGGING
        if (Configuration.DEBUG) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.end();
        }

    }

    private boolean cameraInsideWorld() {
        Gdx.app.log("CameraPos", camera.getCamera().position.toString());
        return false;
    }

}
