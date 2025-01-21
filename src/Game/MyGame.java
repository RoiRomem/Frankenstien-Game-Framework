package Game;

import Engine.*;
import java.awt.*;
import Game.constants.player;

public class MyGame {

    public static Engine e;

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
        // Create and add the player GameObject
        GameObject player = new GameObject(Game.constants.player.startingPos[0],
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
    }

    public static void Update() {
        // Game update logic here

    }
}