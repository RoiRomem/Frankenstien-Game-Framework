package Engine;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Engine extends JPanel implements ActionListener {
    private Timer timer;
    private final int delay;
    private GameInterface gameClass;  // Reference to the game class

    public static ArrayList<GameObject> GameObjects = new ArrayList<>();
    public ArrayList<GameObject.physics> rigidObjects = new ArrayList<>();

    public Engine(Boolean Focusable, Color bgColor, int width, int height, int delay, GameInterface gameClass) {
        this.delay = delay;
        this.gameClass = gameClass;

        setFocusable(Focusable);
        setBackground(bgColor);
        setPreferredSize(new Dimension(width, height));

        timer = new Timer(this.delay, this);
        timer.start(); // Start the timer immediately
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
        gameClass.Update(); // Call Update method of custom game class
        repaint(); // Trigger the paintComponent method
    }

    public static void Run(GameInterface gameClass) {
        JFrame frame = new JFrame();
        Engine engine = new Engine(true, Color.WHITE, 800, 600, 16, gameClass);
        gameClass.Start(); // Call Start method of custom game class
        frame.add(engine);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}
