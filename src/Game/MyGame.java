package Game;

import Engine.*;


public class MyGame implements GameInterface {
    @Override
    public void Start(Engine e) {
        Engine.SceneManager.addScene(0, Engine.SceneManager.sceneMaker("Scenes.MainScene"));
    }

    @Override
    public void Update(Engine e) {}

    @Override
    public int currentScene() {
        return 0;
    }
}
