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
        MyGame.Start();
        timer.start();
    }

    //update graphics function
    @Override
    protected void paintComponent(Graphics g) {
        if(!GameObjects.isEmpty()) {
            for (GameObject go : GameObjects) {
                go.Draw(g);
            }
        }
    }

    //update logic function
    @Override
    public void actionPerformed(ActionEvent e) {
        //engine update logic:
        if(!rigidObjects.isEmpty()) {
            for(GameObject.physics go : rigidObjects) {
                go.updatePhysics();
            }
        }
        MyGame.Update();
        //apply
        repaint();
    }

    public void addGameObject(GameObject go) {
       GameObjects.add(go);
    }

    public void addRigidObject(GameObject go, double mass) {
        rigidObjects.add(go.new physics(go.x, go.y, go.width, go.height, mass));
    }

    public void addKey(KeyEvent KeyEvent, Runnable action) {
        Keys.add(new input(action, KeyEvent));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // we will go through an ArrayList that will include the inputs required and the function they trigger
        if (Keys.isEmpty()) return;
        if (Keys.contains(e)) return;
        for (input i : Keys) {
            if(i.keyCode == e) {
                i.action.run();
            }
        }
    }

    // unused atm but required at the moment
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    // start
    public static void main(String[] args) {
        EngineConstant c = MyGame.getEngineConstants();
        JFrame frame = new JFrame();
        Engine engine = new Engine(c.Focusable, c.color, c.width, c.height, c.delay);
        frame.add(engine);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}