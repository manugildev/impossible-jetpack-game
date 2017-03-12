package helpers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;

import configuration.Configuration;
import gameworld.GameState;
import gameworld.GameWorld;

/**
 * Created by ManuGil on 09/03/15.
 */
public class InputHandler implements InputProcessor {

    private GameWorld world;
    private float scaleFactorX;
    private float scaleFactorY;
    private int activeTouch = 0;

    public InputHandler(GameWorld world, float scaleFactorX, float scaleFactorY) {
        this.world = world;
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.RIGHT) {
            world.getHero().clickedRight();
        } else if (keycode == Input.Keys.LEFT) {
            world.getHero().clickedLeft();
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.R) {
            world.reset();
        } else if (keycode == Input.Keys.D) {
            if (Configuration.DEBUG) Configuration.DEBUG = false;
            else Configuration.DEBUG = true;
        }
        if (keycode == Input.Keys.RIGHT) {
            world.getHero().notClickedRight();
        } else if (keycode == Input.Keys.LEFT) {
            world.getHero().notClickedLeft();
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        activeTouch++;
        if (world.isRunning()) {
            if (world.pauseButton.isTouchDown(screenX, screenY)) {
                world.setPauseMode();
            } else
                runningTouchDown(screenX);

        } else if (world.isTutorial()) {

        } else if (world.isMenu()) {
            checkButtonsDown(screenX, screenY);
        } else if (world.isGameOver()) {
            checkButtonsDownGameOver(screenX, screenY);
        } else if (world.isPause()) {
            world.finishPause();
        }

        world.muteButton.isTouchDown(screenX, screenY);

        return false;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        activeTouch--;

        if (world.isRunning()) {
            if (activeTouch == 0) {
                world.getHero().notClickedRight();
                world.getHero().notClickedLeft();
            } else {
                if (screenX > world.gameWidth / 2) {
                    world.getHero().notClickedRight();
                } else {
                    world.getHero().notClickedLeft();
                }
            }

        } else if (world.isTutorial()) {
            world.finishTutorial();
        } else if (world.isMenu()) {
            checkButtonsUp(screenX, screenY);
        } else if (world.isGameOver()) {
            checkButtonsUpGameOver(screenX, screenY);
        } else if (world.isPause()) {
            world.pauseButton.isTouchUp(screenX, screenY);
        }
        //world.muteButton.isTouchUp(screenX,screenY);

        return false;
    }


    private void runningTouchDown(float screenX) {
        if (activeTouch == 1) {
            if (screenX > world.gameWidth / 2) {
                world.getHero().clickedRight();
            } else {
                world.getHero().clickedLeft();
            }
        } else if (activeTouch == 2) {
            if (world.getHero().clickedLeft) {
                world.getHero().clickedRight();
            } else if (world.getHero().clickedRight) {
                world.getHero().clickedLeft();
            }
        }
    }

    private void checkButtonsDown(float screenX, float screenY) {
        for (int i = 1; i < world.getMenu().menubuttons.size; i++) {
            if (world.getMenu().menubuttons.get(i).isTouchDown((int) screenX, (int) screenY)) {
                AssetLoader.click.play();
            }
        }
    }

    private void checkButtonsDownGameOver(float screenX, float screenY) {
        for (int i = 0; i < world.getGameOver().menubuttons.size - 1; i++) {
            if (world.getGameOver().menubuttons.get(i).isTouchDown((int) screenX, (int) screenY)) {
                AssetLoader.click.play();
            }
        }
    }

    private void checkButtonsUp(int screenX, int screenY) {
        if (world.getMenu().menubuttons.get(1).isTouchUp(screenX, screenY)) {
            world.gameState = GameState.TRANSITION;
            world.getMenu().startGame();
        } else if (world.getMenu().menubuttons.get(2).isTouchUp(screenX, screenY)) {
            world.actionResolver.showScores();
        } else if (world.getMenu().menubuttons.get(3).isTouchUp(screenX, screenY)) {
            world.actionResolver.shareGame(Configuration.SHARE_MESSAGE);
        } else if (world.getMenu().menubuttons.get(4).isTouchUp(screenX, screenY)) {
            world.actionResolver.IAPClick();
        } else {
            for (int i = 1; i < world.getMenu().menubuttons.size; i++) {
                world.getMenu().menubuttons.get(i).isPressed = false;
                world.getMenu().menubuttons.get(i).getSprite().setColor(Color.WHITE);
            }
        }
    }

    private void checkButtonsUpGameOver(int screenX, int screenY) {
        if (world.getGameOver().menubuttons.get(0).isTouchUp(screenX, screenY)) {
            world.getGameOver().startGame();
        } else if (world.getGameOver().menubuttons.get(1).isTouchUp(screenX, screenY)) {
            world.actionResolver.shareGame(Configuration.SHARE_MESSAGE);
        } else if (world.getGameOver().menubuttons.get(2).isTouchUp(screenX, screenY)) {
            world.gameState = GameState.TRANSITION;
            world.getGameOver().startMenu();
        } else if (world.getGameOver().menubuttons.get(3).isTouchUp(screenX, screenY)) {
            world.actionResolver.IAPClick();
        } else {
            for (int i = 0; i < world.getGameOver().menubuttons.size - 1; i++) {
                world.getGameOver().menubuttons.get(i).isPressed = false;
                world.getGameOver().menubuttons.get(i).getSprite().setColor(Color.WHITE);
            }
        }
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (world.gameHeight - screenY / scaleFactorY);
    }
}
