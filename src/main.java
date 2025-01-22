import Engine.Engine;
import Engine.GameInterface;
import Game.GameInfo;

public class main {
    public static void main(String[] args) {
        String gameClassName = "Game."+GameInfo.GAME_CLASS_NAME; // Change this to your desired game class name

        try {
            // Use reflection to dynamically load and instantiate the game class
            Class<?> gameClass = Class.forName(gameClassName);

            // Ensure the class implements GameInterface
            if (GameInterface.class.isAssignableFrom(gameClass)) {
                // Instantiate the game class
                GameInterface gameInstance = (GameInterface) gameClass.getDeclaredConstructor().newInstance();

                // Run the game
                Engine.Run(gameInstance); // Pass the instance to Engine.Run()
            } else {
                System.err.println("Error: The class does not implement GameInterface.");
            }
        } catch (Exception e) {
            // Handle errors (e.g., class not found, instantiation failure, etc.)
            System.err.println("Error initializing game class: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
