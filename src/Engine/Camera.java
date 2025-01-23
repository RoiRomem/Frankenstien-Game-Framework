package Engine;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Camera extends Engine {
    private static int cameraX = 0;
    private static int cameraY = 0;
    private static int cameraScale = 1;

    public int getX() {
        return cameraX;
    }

    public int getY() {
        return cameraY;
    }

    public int getScale() {
        return cameraScale;
    }

    public Camera(Boolean Focusable, Color bgColor, int width, int height, int delay, GameInterface gameClass) {
        super(Focusable, bgColor, width, height, delay, gameClass);
    }

    public static void CameraUpdate(Graphics2D g)
    {
        AffineTransform transform = new AffineTransform();
        transform.translate(cameraX, cameraY);
        transform.scale(cameraScale, cameraScale);
        g.setTransform(transform);
    }

    public static void DrawingCamera(Graphics2D g)
    {
        g.setTransform(new AffineTransform());
    }

}
