package Game;

import Engine.*;
import java.awt.*;

import static java.awt.event.KeyEvent.*;


public class MyGame implements GameInterface {

    static GameObject player;

    @Override
    public void Start(Engine e) {
        // Create and add the player GameObject
        player = new GameObject(Game.constants.player.startingPos[0], Game.constants.player.startingPos[1], Game.constants.player.size[0], Game.constants.player.size[1]);
        player.SelectShape(GameObject.type.RECTANGLE);
        player.addColor(Color.BLACK);
        e.addGameObject(player);
        e.addRigidObject(player, Game.constants.player.mass);
        player.physicsBody.toggleGravity();  // Enable gravity for player

        // create floor
        GameObject floor = new GameObject(0, 400, 800, 20);
        floor.SelectShape(GameObject.type.RECTANGLE);
        floor.addColor(Color.darkGray);
        e.addGameObject(floor);
        e.addRigidObject(floor, 200);

        e.addPressedKey(VK_SPACE, MyGame::Jump);
        e.addPressedKey(VK_RIGHT, MyGame::MoveRight);
        e.addPressedKey(VK_LEFT, MyGame::MoveLeft);
        e.addReleasedKey(VK_R, MyGame::respawn);
    }

    @Override
    public void Update(Engine e) {
        // Game update logic here
        if(player.getX() >= 800) {
            player.setX(1);
            player.setY(player.getY() - 10);
        }
        if(player.getX() <= 0) {
            player.setX(799);
            player.setX(player.getY() + 10);
        }
    }

    public static void Jump() {
        if(player.physicsBody.isGrounded) player.physicsBody.AddForce(GameObject.physics.direction.UP, Game.constants.player.jumpForce);
    }

    public static void MoveRight() {
        player.physicsBody.AddForce(GameObject.physics.direction.RIGHT, Game.constants.player.speed);
    }

    public static void MoveLeft() {
        player.physicsBody.AddForce(GameObject.physics.direction.LEFT, Game.constants.player.speed);
    }

    public static void respawn() {
        player.setX(Game.constants.player.startingPos[0]);
        player.setY(Game.constants.player.startingPos[1]);
        player.physicsBody.resetNetForce();
    }

}
