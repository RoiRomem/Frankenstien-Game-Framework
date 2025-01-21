package Engine;

import Game.*;

import javax.swing.*; // Provides GUI components like JPanel, JFrame, and Timer
import java.awt.*;
import java.awt.event.*; // Provides event-handling classes (e.g., ActionListener, KeyListener)
import java.util.ArrayList;

public class Engine extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private final int delay;

    private ArrayList<input> Keys = new ArrayList<>();
    public static ArrayList<GameObject> GameObjects = new ArrayList<>();
    public ArrayList<GameObject.physics> rigidObjects = new ArrayList<>();

    public Engine(Boolean Focusable, Color bgColor, int width, int height, int delay) {
        this.delay = delay;

        setFocusable(Focusable);
        setBackground(bgColor);
        setPreferredSize(new Dimension(width, height));
        addKeyListener(this);

        timer = new Timer(this.delay, this);
        timer.start(); // Start the timer immediately
    }

    // Update graphics function
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Clears the background
        if (!GameObjects.isEmpty()) {
            for (GameObject go : GameObjects) {
                go.Draw(g);
            }
        }
    }

    // Update logic function
    @Override
    public void actionPerformed(ActionEvent e) {
        // Engine update logic:
        if (!rigidObjects.isEmpty()) {
            for (GameObject.physics go : rigidObjects) {
                go.updatePhysics();
            }
        }
        MyGame.Update(); // Update game-specific logic
        repaint(); // Trigger the paintComponent method
    }

    public void addGameObject(GameObject go) {
        GameObjects.add(go);
    }

    public void addRigidObject(GameObject go, double mass) {
        go.physicsBody = go.new physics(mass);
        rigidObjects.add(go.physicsBody);
    }

    public void addKey(int keyCode, Runnable action) {
        Keys.add(new input(action, keyCode));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (Keys.isEmpty()) return;
        for (input i : Keys) {
            if (i.keyCode == e.getKeyCode()) { // Compare key codes
                i.action.run();
                break;
            }
        }
    }

    // Unused but required by KeyListener
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    // Static method to initialize the game
    public static void Run() {
        JFrame frame = new JFrame();
        Engine engine = MyGame.MyEngine();
        MyGame.Start(); // Call Start() after the engine is fully initialized
        frame.add(engine);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
