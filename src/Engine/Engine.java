package Engine;

import Game.GameInfo;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Engine extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private final int delay;
    public GameInterface gameClass;  // Reference to the game class

    public static ArrayList<GameObject> GameObjects = new ArrayList<>();
    public ArrayList<GameObject.physics> rigidObjects = new ArrayList<>();
    private ArrayList<input> PressedKeys = new ArrayList<>();
    private ArrayList<input> ReleasedKeys = new ArrayList<>();
    private ArrayList<input> TypedKeys = new ArrayList<>();

    public Engine(Boolean Focusable, Color bgColor, int width, int height, int delay, GameInterface gameClass) {
        this.delay = delay;
        this.gameClass = gameClass;

        setFocusable(true);  // Always set to true
        requestFocusInWindow();  // Request focus
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

    //Update logic
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
        Camera.DrawingCamera( (Graphics2D) g );
        // Render all game objects
        for (GameObject go : GameObjects) {
            go.Draw(g);
        }

        Camera.CameraUpdate( (Graphics2D) g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (PressedKeys.isEmpty()) return;
        for (input i : PressedKeys) {
            if (i.keyCode == e.getKeyCode()) { // Compare key codes
                i.action.run();
                break;
            }
        }
    }

    // Unused but required by KeyListener
    @Override
    public void keyReleased(KeyEvent e) {
        if (ReleasedKeys.isEmpty()) return;
        for (input i : ReleasedKeys) {
            if (i.keyCode == e.getKeyCode()) {
                i.action.run();
                break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (TypedKeys.isEmpty()) return;
        for (input i : TypedKeys) {
            if (i.keyCode == e.getKeyCode()) {
                i.action.run();
                break;
            }
        }
    }


    public void addPressedKey(int keyCode, Runnable action) {
       PressedKeys.add(new input(action, keyCode));
   }

   public void addReleasedKey(int keyCode, Runnable action) {
        ReleasedKeys.add(new input(action, keyCode));
   }

   public void addTypedKey(int keyCode, Runnable action) {
        TypedKeys.add(new input(action, keyCode));
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
