package Game;

import Engine.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

public class MyGame implements GameInterface {
    @Override
    public void Start(Engine e) {
        Image player = new ImageIcon("images/player1.png").getImage();
        Engine.SceneManager.addScene(0, Engine.SceneManager.sceneMaker("Scenes.MainScene"));
        Engine.AssetManager.loadAsset("Player", player);
    }

    @Override
    public void Update(Engine e) {}

    @Override
    public int currentScene() {
        return 0;
    }
}
