package gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import gameworld.GameWorld;

/**
 * Created by ManuGil on 10/03/15.
 */
public class Background extends GameObject {
    public Background(GameWorld world, float x, float y, float width, float height,
                      TextureRegion texture, Color color) {
        super(world, x, y, width, height, texture, color);
    }


}
