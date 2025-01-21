package Engine;

// This class stores an input value
public class input {
    Runnable action;
    int keyCode;

    // Constructor
    input(Runnable action, int keyCode) {
        this.action = action;
        this.keyCode = keyCode;
    }
}
