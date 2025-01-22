package Game;

import Engine.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MyGame {

    public static Engine e;

    static GameObject player;

    // Initialize the engine constants
    public static Engine MyEngine() {
        Boolean focusable = true;
        Color backgroundColor = Color.WHITE;
        int width = 800;
        int height = 600;
        int delay = 16; // Set to 60 fps
        e = new Engine(focusable, backgroundColor, width, height, delay);
        return e;
    }


    public static void Start() {
        e.addKey(KeyEvent.VK_SPACE, MyGame::Jump);
        e.addKey(KeyEvent.VK_RIGHT, MyGame::MoveRight);
        e.addKey(KeyEvent.VK_LEFT, MyGame::MoveLeft);


        // Create and add the player GameObject
        player = new GameObject(Game.constants.player.startingPos[0],
                Game.constants.player.startingPos[1],
                Game.constants.player.size[0],
                Game.constants.player.size[1]);
        player.SelectShape(GameObject.type.RECTANGLE);
        player.addColor(Color.BLACK);
        e.addGameObject(player);
        e.addRigidObject(player, Game.constants.player.mass);
        player.physicsBody.toggleGravity();  // Enable gravity for player

        // create floor
        GameObject floor = new GameObject(100, 400, 600, 20);
        floor.SelectShape(GameObject.type.RECTANGLE);
        floor.addColor(Color.darkGray);
        e.addGameObject(floor);
        e.addRigidObject(floor, Game.constants.player.mass);
        // Note: floor's gravity remains disabled by default

        // create a wall for testing:
        GameObject wall = new GameObject(200, 350, 20, 50);
        wall.SelectShape(GameObject.type.RECTANGLE);
        wall.addColor(Color.darkGray);
        e.addGameObject(wall);
        e.addRigidObject(wall, Game.constants.player.mass);
        // Note: wall's gravity remains disabled by default
    }

    public static void Jump() {
        if (player.physicsBody.isGrounded) player.physicsBody.applyForce(GameObject.physics.direction.UP, Game.constants.player.jumpForce);
    }

    public static void MoveRight() {
       player.physicsBody.applyForce(GameObject.physics.direction.RIGHT, Game.constants.player.speed);
    }

    public static void MoveLeft() {
        player.physicsBody.applyForce(GameObject.physics.direction.LEFT, Game.constants.player.speed);
    }

    public static void Update() {
        // Game update logic here

    }
}