package ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import gameobjects.GameObject;
import gameworld.GameWorld;
import helpers.AssetLoader;
import helpers.FlatColors;

/**
 * Created by ManuGil on 14/03/15.
 */
public class Text extends GameObject {
    private final BitmapFont font;
    private final Color fontColor;
    private final float distance;
    private String text;
    private BitmapFont.HAlignment center;

    public Text(GameWorld world, float x, float y, float width, float height,
                TextureRegion texture, Color color, String text, BitmapFont font, Color fontColor,
                float distance, BitmapFont.HAlignment center) {
        super(world, x, y, width, height, texture, color);
        this.font = font;
        this.text = text;
        this.fontColor = fontColor;
        this.distance = distance;
        this.center = center;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, ShaderProgram fontshader) {

        super.render(batch, shapeRenderer);
        batch.setShader(fontshader);
        AssetLoader.font.setColor(FlatColors.DARK_BLACK);
        AssetLoader.font.drawWrapped(batch, text, getRectangle().x,
                getRectangle().y + getRectangle().height - distance - 3, getRectangle().width,
                center);
        AssetLoader.font.setColor(Color.WHITE);
        AssetLoader.font.drawWrapped(batch, text, getRectangle().x,
                getRectangle().y + getRectangle().height - distance, getRectangle().width,
                center);

        batch.setShader(null);
    }

    public void setText(String text) {
        this.text = text;
    }


}
