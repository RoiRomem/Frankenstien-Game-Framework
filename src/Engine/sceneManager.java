package Engine;

import Game.GameInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class sceneManager {
    private String gameClassName = "Game."+GameInfo.GAME_CLASS_NAME; // Change this to your desired game class name
    private Class<?> gameClass;
    private HashMap<Integer, Scene> scenes = new HashMap<>();
    private int sceneId;

    public void run() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        try {
            gameClass = Class.forName(gameClassName);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        // Ensure the class implements GameInterface
        if (GameInterface.class.isAssignableFrom(gameClass)) {
            // Instantiate the game class
            GameInterface gameInstance = (GameInterface) gameClass.getDeclaredConstructor().newInstance();

            // Get the thinga magina
            sceneId = gameInstance.currentScene();
        } else {
            System.err.println("Error: The class does not implement GameInterface.");
        }
    }

    public void addScene(int sceneId, Scene scene) {
        scenes.put(sceneId, scene);
    }

    public void removeScene(int sceneId) {
        scenes.remove(sceneId);
    }

    public void update(Engine e) {
        scenes.get(sceneId).update(e);
    }

    public void start(Engine e) {
        if(scenes.get(sceneId) != null) scenes.get(sceneId).start(e);
    }

    public int getSceneId() {
        return sceneId;
    }
}
