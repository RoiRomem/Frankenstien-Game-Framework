package Engine;

import Game.GameInfo;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Engine extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private final int delay;
    public GameInterface gameClass;  // Reference to the game class
    private sceneManager SceneManager = new sceneManager();
    private int lastFrameScene = -1;

    public static HashMap<GameObject, Byte> GameObjects = new HashMap<>();
    public HashMap<GameObject.physics, Byte> rigidObjects = new HashMap<>();

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
        GameObjects.put(go, (byte) 0);
    }

    public void addRigidObject(GameObject go, double mass) {
        go.physicsBody = go.new physics(mass);
        rigidObjects.put(go.physicsBody, (byte) 0); //lowkey using a byte just so I can use a sigma map, editor note: very sigma note
    }

    public void destory(GameObject go) {
        GameObjects.remove(go);
        if(go.physicsBody != null) {
            rigidObjects.remove(go.physicsBody);
        }
    }

    // Update logic
    @Override
    public void actionPerformed(ActionEvent e) {
        // IDE told me to do this error handling
        try {
            SceneManager.run();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }

        if (!rigidObjects.isEmpty()) {
            for (Map.Entry<GameObject.physics, Byte> entry : rigidObjects.entrySet()) {
                entry.getKey().updatePhysics();
            }
        }

        gameClass.Update(this); // Call Update method of custom game class
        try {
            SceneManager.update().run(); // scene run
        } catch (Exception _) {}
        repaint(); // Trigger the paintComponent method
        if(lastFrameScene != SceneManager.getSceneId()) SceneManager.start();
        lastFrameScene = SceneManager.getSceneId();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Camera.DrawingCamera((Graphics2D) g);

        // Render all game objects
        for (Map.Entry<GameObject, Byte> entry : GameObjects.entrySet()) {
            entry.getKey().Draw(g);
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
