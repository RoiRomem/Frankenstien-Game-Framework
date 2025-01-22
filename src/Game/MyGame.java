package Game;

import Engine.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MyGame implements GameInterface {

    public static Engine e;

    static GameObject player;

    @Override
    public void Start() {
        // Initialize the engine constants
        Boolean focusable = true;
        Color backgroundColor = Color.WHITE;
        int width = 800;
        int height = 600;
        int delay = 16; // Set to 60 fps
        e = new Engine(focusable, backgroundColor, width, height, delay, this);

        // Create and add the player GameObject
        player = new GameObject(100, 100, 50, 50);
        player.SelectShape(GameObject.type.RECTANGLE);
        player.addColor(Color.BLACK);
        e.addGameObject(player);
        e.addRigidObject(player, 50);
        player.physicsBody.toggleGravity();  // Enable gravity for player

        // create floor
        GameObject floor = new GameObject(100, 400, 600, 20);
        floor.SelectShape(GameObject.type.RECTANGLE);
        floor.addColor(Color.darkGray);
        e.addGameObject(floor);
        e.addRigidObject(floor, 200);
    }

    @Override
    public void Update() {
        // Game update logic here
        System.out.println("I'm running!");
    }

    public static void main(String[] args) {
        Engine.Run(new MyGame());  // Pass the MyGame instance to Engine to run the game
    }
}
