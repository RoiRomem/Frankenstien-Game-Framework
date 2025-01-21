package Engine;

// this class stores an input value
// this is the only file I truly understand
import java.awt.event.KeyEvent;

public class input {
    Runnable action;
    KeyEvent keyCode;

    //constructor
    input(Runnable action, KeyEvent keyCode) {
        this.action= action;
        this.keyCode = keyCode;
    }
}
