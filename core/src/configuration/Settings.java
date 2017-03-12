package configuration;

/**
 * Created by ManuGil on 21/03/15.
 */
public class Settings {
    //The values listed above are game variables, that modify how the game actually is and works
    //I do not recommend changing this values at all
    //Take care

    //GAMEWORLD
    public static final int NUMBER_INITIAL_BACKGROUND_STARS = 180;
    public static final float WORLD_GRAVITY = -6.8f;
    public static final float JETPACK_SOUND_REPETITION_TIME = .15f;
    public static final float PHOTO_WAIT_TIME = .1f; //Don't use this
    public static final float MUSIC_VOLUME = 0.2f; //NUMBER BETWEEN 0-1
    public static final float INTERSTITIAL_DELAY = .6f;

    //COLLISION
    public static final short CATEGORY_HERO = 0x0001;  // 0000000000000001 in binary
    public static final short CATEGORY_COIN = 0x0002; // 0000000000000010 in binary
    public static final short CATEGORY_METEOR = 0x0004; // 0000000000000100 in binary
    public static final short MASK_HERO = CATEGORY_METEOR; // or ~CATEGORY_PLAYER
    public static final short MASK_COIN = CATEGORY_METEOR; // or ~CATEGORY_MONSTER
    public static final short MASK_METEOR = CATEGORY_HERO | CATEGORY_COIN | CATEGORY_METEOR; // or ~CATEGORY_MONSTER

    //HERO
    public static final float MAX_Y_VEL = 6;
    public static final float MIN_Y_VEL = -6;
    public static final float MAX_X_VEL = 3;
    public static final float MIN_X_VEL = -3;
    public static final float JETPACK_Y_ACCELERATION = 2.1f;
    public static final float JETPACK_X_ACCELERATION = 2.0f;
    public static final float JETPACK_Y_DECELERATION = 9f;
    public static final float JETPACK_X_DECELERATION = 2f;
    public static final float INITIAL_FLASH_TIME = .5f;
    public static final float RUMBLE_POWER = 20f;
    public static final float RUMBLE_TIME = .6f;
    public static final float LOCAL_JETPACK_LOCATION_X = 5;
    public static final float LOCAL_JETPACK_LOCATION_Y = 0; //NOT USED, ALWAYS IN CENTER
    public static final boolean JETPACK_PARTICLES = true;

    //METEORS
    public static final int NUMBER_INITIAL_METEORS = 13;
    public static final float METEOR_MIN_VEL = 2.5f;
    public static final float METEOR_MAX_VEL = 3f;
    public static final boolean METEOR_PARTICLES = true;

    //COINS
    public static final int NUMBER_INITIAL_COINS = 3;
    public static final float COIN_MAX_VEL = 0.2f;
    public static final float COIN_JOINT_DISTANCE = 10f;
    public static final float COIN_COLLISION_MARGIN = 5;
    public static final boolean COIN_PARTICLES = true;

}
