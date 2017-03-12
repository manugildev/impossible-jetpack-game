package configuration;

/**
 * Created by ManuGil on 09/03/15.
 */

public class Configuration {

    public static final String GAME_NAME = "Impossible Jetpack";
    public static boolean DEBUG = false;
    public static final boolean SPLASHSCREEN = true;

    //ADMOB IDS
    public static final String AD_UNIT_ID_BANNER = "ca-app-pub-6147578034437241/4745179018";
    public static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-6147578034437241/2157019011";
    public static float AD_FREQUENCY = .9f;

    //In App Purchases
    public static final String ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwNDPT7WNhaabWsqaIr+rPyb1yGPOrzCJfNGh1JBp/zSoDyyc9gDdXSkYX1Qg1hmrVQUcMPfLWGjHGQZwSTlDXNO1HoJ2xpV3UYQ8kH2rNKE24ak2NnbXKzrapgeRtUFFHnMkW+a2LB1jT8uLM5LHfjH/6XOFWK48VYkg0dyuK94+5iGimebMgfNB2zW4YrPgB+zExxT2RSH1V3v9dAzUAQqOPeMM4FrZy52zLlmFjMcJR+zPZUv+9ate3o0iElxBspydf1apDdDqM5kfbDk5AuDiZ4OTiQmIoW1MDOy2+wY+YQA54DZxbIJFVR+MLVGQHO6r5nZG1FfS3LxcUVi93wIDAQAB";
    public static final String PRODUCT_ID = "removeads";

    //LEADERBOARDS
    public static final String LEADERBOARD_HIGHSCORE = "CgkI66bg6vUHEAIQBQ";
    public static final String LEADERBOARD_GAMESPLAYED = "CgkI66bg6vUHEAIQBg";

    //Share Message
    public static final String SHARE_MESSAGE = "Can you beat my High Score at " + GAME_NAME + "? #ImpossibleJetpack";
    public static boolean Share_WITH_IMAGE = true;
}
