package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import configuration.Configuration;

/**
 * Created by ManuGil on 09/03/15.
 */
public class AssetLoader {

    public static Texture logoTexture, dotT, colorCircleT, backgroundT, tutorialT, meteorT, coinT, buttonsT, titleT;
    public static TextureRegion transparent, logo, square, dot, colorCircle, flashCircle, background, tutorial, meteor, coin,
            playButton, shareButton, scoresButton, removeadsButton, title, playButtonOver, shareButtonOver, boardOver, backButtonOver,
            soundButton, muteButton, pauseButton;
    public static BitmapFont font, fontS, fontXS, fontB;
    private static Preferences prefs;

    public static Sound click, success, end, select, explosion, pickup, jetpack, woosh;
    public static Music music;

    public static void load() {
        //LOGO TEXTURE "logo.png"
        logoTexture = new Texture(Gdx.files.internal("logo.png"));
        logoTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        logo = new TextureRegion(logoTexture, 0, 0, logoTexture.getWidth(),
                logoTexture.getHeight());
        transparent = new TextureRegion(new Texture(Gdx.files.internal("transparent.png")), 0, 0,
                10, 10);
        square = new TextureRegion(new Texture(Gdx.files.internal("square.png")), 0, 0, 10, 10);
        dotT = new Texture(Gdx.files.internal("dot.png"));
        dot = new TextureRegion(dotT, 0, 0, dotT.getWidth(), dotT.getHeight());

        colorCircleT = new Texture(Gdx.files.internal("hero.png"));
        colorCircleT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        colorCircle = new TextureRegion(colorCircleT, 0, 0, 820,
                colorCircleT.getHeight());
        flashCircle = new TextureRegion(colorCircleT, 0, 0, 820, colorCircleT.getHeight());

        meteorT = new Texture(Gdx.files.internal("meteor.png"));
        meteorT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        meteor = new TextureRegion(meteorT, 0, 0, meteorT.getWidth(),
                meteorT.getHeight());

        coinT = new Texture(Gdx.files.internal("coin.png"));
        coinT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        coin = new TextureRegion(coinT, 0, 0, coinT.getWidth(),
                coinT.getHeight());

        //BUTTONS
        buttonsT = new Texture(Gdx.files.internal("buttons.png"));
        buttonsT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        playButton = new TextureRegion(buttonsT, 0, 0, buttonsT.getWidth(), 120);
        scoresButton = new TextureRegion(buttonsT, 0, 120, buttonsT.getWidth(), 120);
        shareButton = new TextureRegion(buttonsT, 0, 120 * 2, buttonsT.getWidth(), 120);
        removeadsButton = new TextureRegion(buttonsT, 0, 120 * 3, buttonsT.getWidth(), 120);
        playButtonOver = new TextureRegion(buttonsT, 0, 120 * 4, 200, 210);
        shareButtonOver = new TextureRegion(buttonsT, 200, 120 * 4, 200, 210);
        backButtonOver = new TextureRegion(buttonsT, 400, 120 * 4, 110, 120);
        boardOver = new TextureRegion(buttonsT, 0, 120 * 4 + 211, buttonsT.getWidth(), 492);
        soundButton = new TextureRegion(buttonsT, 0,
                120 * 4 + 211 + boardOver.getRegionHeight() + 1, 256, 202);
        muteButton = new TextureRegion(buttonsT, 256,
                120 * 4 + 211 + boardOver.getRegionHeight() + 1, 256, 202);
        pauseButton = new TextureRegion(buttonsT, 257 + 256,
                120 * 4 + 211 + boardOver.getRegionHeight() + 1, 155, 204);

        titleT = new Texture(Gdx.files.internal("jetpacklogo.png"));
        titleT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        title = new TextureRegion(titleT, 0, 0, titleT.getWidth(), titleT.getHeight());

        tutorialT = new Texture(Gdx.files.internal("tutorial.png"));
        tutorialT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        tutorial = new TextureRegion(tutorialT, 0, 0, tutorialT.getWidth(),
                tutorialT.getHeight());


        backgroundT = new Texture(Gdx.files.internal("background.png"));
        backgroundT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        background = new TextureRegion(backgroundT, 0, 0, backgroundT.getWidth(),
                backgroundT.getHeight());

        //LOADING FONT
        Texture tfont = new Texture(Gdx.files.internal("sans.png"), true);
        tfont.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);

        font = new BitmapFont(Gdx.files.internal("sans.fnt"),
                new TextureRegion(tfont), true);
        font.setScale(1.9f, -1.9f);
        //font.setColor(FlatColors.WHITE);

        fontB = new BitmapFont(Gdx.files.internal("sans.fnt"),
                new TextureRegion(tfont), true);
        fontB.setScale(1.4f, -1.4f);
        //fontB.setColor(FlatColors.WHITE);

        fontS = new BitmapFont(Gdx.files.internal("sans.fnt"),
                new TextureRegion(tfont), true);
        fontS.setScale(1.2f, -1.2f);
        //fontS.setColor(FlatColors.WHITE);

        fontXS = new BitmapFont(Gdx.files.internal("sans.fnt"),
                new TextureRegion(tfont), true);
        fontXS.setScale(0.9f, -0.9f);
        //fontXS.setColor(FlatColors.WHITE);


        //PREFERENCES - SAVE DATA IN FILE
        prefs = Gdx.app.getPreferences(Configuration.GAME_NAME);

        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }

        if (!prefs.contains("games")) {
            prefs.putInteger("games", 0);
        }

        click = Gdx.audio.newSound(Gdx.files.internal("blip_click.wav"));
        success = Gdx.audio.newSound(Gdx.files.internal("blip_success.wav"));
        end = Gdx.audio.newSound(Gdx.files.internal("blip_end.wav"));
        select = Gdx.audio.newSound(Gdx.files.internal("select.wav"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
        pickup = Gdx.audio.newSound(Gdx.files.internal("pickup.wav"));
        jetpack = Gdx.audio.newSound(Gdx.files.internal("jetpack.wav"));
        woosh = Gdx.audio.newSound(Gdx.files.internal("woosh.wav"));

        music = Gdx.audio.newMusic(Gdx.files.internal("music.ogg"));
    }

    public static void dispose() {
        font.dispose();
        fontS.dispose();
        fontXS.dispose();
        fontB.dispose();
        dotT.dispose();
        logoTexture.dispose();
        click.dispose();
        success.dispose();
        end.dispose();
        colorCircleT.dispose();
        meteorT.dispose();
        backgroundT.dispose();

    }

    public static void setHighScore(int val) {
        prefs.putInteger("highScore", val);
        prefs.flush();
    }

    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }

    public static void addGamesPlayed() {
        prefs.putInteger("games", prefs.getInteger("games") + 1);
        prefs.flush();
    }

    public static int getGamesPlayed() {
        return prefs.getInteger("games");
    }

    public static void setAds(boolean removeAdsVersion) {
        prefs = Gdx.app.getPreferences(Configuration.GAME_NAME);
        prefs.putBoolean("ads", removeAdsVersion);
        prefs.flush();
    }

    public static boolean getAds() {
        return prefs.getBoolean("ads", false);
    }

    public static void prefs() {
        prefs = Gdx.app.getPreferences(Configuration.GAME_NAME);
    }

    public static void setVolume(boolean val) {
        prefs.putBoolean("volume", val);
        prefs.flush();
    }

    public static boolean getVolume() {
        return prefs.getBoolean("volume");
    }
}

