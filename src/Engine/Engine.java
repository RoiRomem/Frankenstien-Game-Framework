package Engine;

import Game.GameInfo;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Engine extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private final int delay;
    public GameInterface gameClass;  // Reference to the game class

    public static ArrayList<GameObject> GameObjects = new ArrayList<>();
    public ArrayList<GameObject.physics> rigidObjects = new ArrayList<GameObject.physics>();

    private HashMap<Integer, input> PressedKeys = new HashMap<>();
    private HashMap<Integer, input> ReleasedKeys = new HashMap<>();
    private HashMap<Integer, input> TypedKeys = new HashMap<>();

    public Engine(Boolean Focusable, Color bgColor, int width, int height, int delay, GameInterface gameClass) {
        this.delay = delay;
        this.gameClass = gameClass;

        setFocusable(true);
        requestFocusInWindow();
        setBackground(bgColor);
        setPreferredSize(new Dimension(width, height));

        timer = new Timer(this.delay, this);
        timer.start();
    }

    public void addGameObject(GameObject go) {
        GameObjects.add(go);
    }

    public void addRigidObject(GameObject go, double mass) {
        go.physicsBody = go.new physics(mass);
        rigidObjects.add(go.physicsBody);
    }

    // Update logic
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!rigidObjects.isEmpty()) {
            for (GameObject.physics go : rigidObjects) {
                go.updatePhysics();
            }
        }
        gameClass.Update(this); // Call Update method of custom game class
        repaint(); // Trigger the paintComponent method
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Camera.DrawingCamera((Graphics2D) g);

        // Render all game objects
        for (GameObject go : GameObjects) {
            go.Draw(g);
        }

        Camera.CameraUpdate((Graphics2D) g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (PressedKeys.containsKey(e.getKeyCode())) {
            PressedKeys.get(e.getKeyCode()).action.run();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (ReleasedKeys.containsKey(e.getKeyCode())) {
            ReleasedKeys.get(e.getKeyCode()).action.run();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (TypedKeys.containsKey(e.getKeyCode())) {
            TypedKeys.get(e.getKeyCode()).action.run();
        }
    }

    public void addPressedKey(int keyCode, Runnable action) {
        PressedKeys.put(keyCode, new input(action, keyCode));
    }

    public void addReleasedKey(int keyCode, Runnable action) {
        ReleasedKeys.put(keyCode, new input(action, keyCode));
    }

    public void addTypedKey(int keyCode, Runnable action) {
        TypedKeys.put(keyCode, new input(action, keyCode));
    }

    public static void Run(GameInterface gameClassPar) {
        JFrame frame = new JFrame();
        Engine engine = new Engine(GameInfo.Focusable, GameInfo.BackgroundColor, GameInfo.width, GameInfo.height, GameInfo.delay, gameClassPar);

        // Add key listener
        engine.addKeyListener(engine);
        frame.addKeyListener(engine);

        gameClassPar.Start(engine);
        frame.add(engine);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
