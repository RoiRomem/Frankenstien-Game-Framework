package Game;

import java.awt.Color;

public class EngineConstant {
    public Boolean Focusable;
    public Color color;
    public int delay = 16; //by default, it will be equal to 60 fps
    public int width, height; //dimensions of the window

    public EngineConstant(Boolean Focusable, Color color, int width, int height) {
        this.Focusable = Focusable;
        this.color = color;
        this.width = width;
        this.height = height;
    }


    void changeDelay(int delay) {
        this.delay = delay;
    }
}
