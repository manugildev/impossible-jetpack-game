package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

import java.nio.ByteBuffer;

public class ScreenShot implements Runnable {
    private static int fileCounter = 0;
    private Pixmap pixmap;

    @Override
    public void run() {
        saveScreenshot();
    }

    public void prepare(int x, int y, int width, int height) {
        getScreenshot(x, y, width, height, false);
    }

    public void saveScreenshot() {
        try {
            FileHandle fh;
            do {
                fh = new FileHandle(
                        Gdx.files
                                .getLocalStoragePath() + "screenshot/screenshot.png");

                fh.delete();
            } while (fh.exists());
            rotatePixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            PixmapIO.writePNG(fh, pixmap);
            pixmap.dispose();
        } catch (Exception e) {
        }

    }

    private void rotatePixmap(int w, int h) {
        ByteBuffer pixels = pixmap.getPixels();
        int numBytes = w * h * 4;
        byte[] lines = new byte[numBytes];
        int numBytesPerLine = w * 4;
        for (int i = 0; i < h; i++) {
            pixels.position((h - i - 1) * numBytesPerLine);
            pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
        }
        pixels.clear();
        pixels.put(lines);
    }

    public void getScreenshot(int x, int y, int w, int h, boolean flipY) {
        Gdx.gl.glPixelStorei(GL20.GL_PACK_ALIGNMENT, 1);
        pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        Gdx.gl.glReadPixels(x, y, w, h, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixmap.getPixels());
    }
}