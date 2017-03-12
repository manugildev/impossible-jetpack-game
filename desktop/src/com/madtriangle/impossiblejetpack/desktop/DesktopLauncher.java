package com.madtriangle.impossiblejetpack.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import noon.NoonGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "";
        config.height= (int) (1080 / 2);
        config.width= (int) (1920 / 2);
        new LwjglApplication(new NoonGame(new ActionResolverDesktop()), config);
    }
}
