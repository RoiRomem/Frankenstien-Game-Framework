package Engine;

/*
this is probably some of the worst code I've ever written, hopefully this works and I don't have to change it because it honestly scares me 😁😁😁
thanks to chatgpt and his incredible debuging, I can't understand this monstrosity even more 😁😁😁
 */
import java.awt.*;
import java.util.Map;

public class GameObject {
    public enum type {
        EMPTY,
        SPRITE,
        TEXT,
        RECTANGLE,
        ELLIPSE,
        LINE,
        ROUND_RECTANGLE
    }

    private type objectType;

    // Transform properties
    private double x;
    private double y;
    private int width;
    private int height;

    // Graphics properties
    private Image image;
    private Color color;
    private String text;
    private Font font;

    // Components
    public physics physicsBody;

    public GameObject(double x, double y, int width, int height) {
        this.objectType = type.EMPTY;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Getters and setters
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public void SelectShape(type type) {
        this.objectType = type;
    }

    public void addImage(Image image) {
        this.image = image;
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
        this.objectType = type.SPRITE;
    }

    public void addColor(Color color) {
        this.color = color;
    }

    public void addText(String text, Font font) {
        this.text = text;
        this.font = font;
        this.objectType = type.TEXT;
    }

    public void Draw(Graphics g) {
        if (objectType == type.EMPTY) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color != null ? color : Color.BLACK);

        switch (objectType) {
            case SPRITE:
                if (image != null) {
                    g2d.drawImage(image, (int) x, (int) y, null);
                }
                break;
            case TEXT:
                if (text != null && font != null) {
                    g2d.setFont(font);
                    g2d.drawString(text, (int) x, (int) y);
                }
                break;
            case RECTANGLE:
                g2d.fillRect((int) x, (int) y, width, height);
                break;
            case ELLIPSE:
                g2d.fillOval((int) x, (int) y, width, height);
                break;
            case LINE:
                g2d.drawLine((int) x, (int) y, (int) (x + width), (int) (y + height));
                break;
            case ROUND_RECTANGLE:
                g2d.fillRoundRect((int) x, (int) y, width, height, 20, 20);
                break;
        }
    }

    // Physics class
    public class physics {
        public enum direction { RIGHT, LEFT, UP, DOWN }

        private GameObject gameObject;
        public double mass;
        public double timeStep = 0.016;
        public boolean enabled = false;

        public double velocityX = 0;
        public double velocityY = 0;

        public double accelerationX = 0;
        public double accelerationY = 0;

        private double netForceX = 0;
        private double netForceY = 0;

        public boolean isGravityEnabled = false;
        public static final double GRAVITY = 9.8 * 60;
        public static final double FRICTION = 0.1;

        public collider colliderComponent;
        public boolean isGrounded = false;

        public physics(double mass) {
            this.gameObject = GameObject.this;
            this.mass = mass;
            this.enabled = true;
            this.colliderComponent = new collider(1);
        }

        public void AddForce(direction dir, double force) {
            switch (dir) {
                case RIGHT -> netForceX += force;
                case LEFT -> netForceX -= force;
                case UP -> netForceY -= force;
                case DOWN -> netForceY += force;
            }
        }

        public void toggleGravity() {
            isGravityEnabled = !isGravityEnabled;
        }

        public void resetNetForce() {
            netForceX = 0;
            netForceY = 0;
        }

        public void updatePhysics() {
            if (!enabled) return;

            // Apply gravity if enabled
            if (isGravityEnabled) {
                netForceY += mass * GRAVITY;
            }

            // Apply friction when grounded
            if (isGrounded) {
                applyFriction();
            }

            // Calculate accelerations
            accelerationX = netForceX / mass;
            accelerationY = netForceY / mass;

            // Update velocities
            velocityX += accelerationX * timeStep;
            velocityY += accelerationY * timeStep;

            // Reset net forces
            netForceX = 0;
            netForceY = 0;

            // Store original position
            double originalX = gameObject.x;
            double originalY = gameObject.y;

            // Try movement with collision checks
            double newX = originalX + velocityX * timeStep;
            double newY = originalY + velocityY * timeStep;

            // Move X
            gameObject.x = newX;
            if (colliderComponent.checkForCollision() != null) {
                gameObject.x = originalX;
                velocityX = 0;
            }

            // Move Y
            double oldY = gameObject.y;
            gameObject.y = newY;
            collider.collisionSide collision = colliderComponent.checkForCollision();

            if (collision != null) {
                gameObject.y = oldY;
                velocityY = 0;
                isGrounded = true;
            } else {
                    isGrounded = false;
            }
        }

        private void applyFriction() {
            if (Math.abs(velocityX) > 0.1) {
                double frictionForce = -Math.signum(velocityX) * mass * GRAVITY * FRICTION;
                netForceX += frictionForce;
            } else {
                velocityX = 0;
            }
        }
    }

    public class collider {
        public enum collisionSide { TOP, BOTTOM, LEFT, RIGHT }

        private GameObject gameObject;

        public collider(int colliderBuffer) {
            this.gameObject = GameObject.this;
        }

        public collisionSide checkForCollision() {
            for (Map.Entry<GameObject, Byte> other : Engine.GameObjects.entrySet()) {
                if (other.getKey() == gameObject) continue;
                if (other.getKey().physicsBody == null) continue;

                // Calculate edges of both objects
                double thisLeft = gameObject.x;
                double thisRight = gameObject.x + gameObject.width;
                double thisTop = gameObject.y;
                double thisBottom = gameObject.y + gameObject.height;

                double otherLeft = other.getKey().x;
                double otherRight = other.getKey().x + other.getKey().width;
                double otherTop = other.getKey().y;
                double otherBottom = other.getKey().y + other.getKey().height;

                // Check for overlap
                if (thisRight > otherLeft && thisLeft < otherRight &&
                        thisBottom > otherTop && thisTop < otherBottom) {

                    // Calculate overlap amounts
                    double overlapLeft = thisRight - otherLeft;
                    double overlapRight = otherRight - thisLeft;
                    double overlapTop = thisBottom - otherTop;
                    double overlapBottom = otherBottom - thisTop;

                    // Find smallest overlap
                    double smallestOverlap = Math.min(Math.min(overlapLeft, overlapRight),
                            Math.min(overlapTop, overlapBottom));

                    // Return the direction of the smallest overlap
                    if (smallestOverlap == overlapLeft) return collisionSide.LEFT;
                    if (smallestOverlap == overlapRight) return collisionSide.RIGHT;
                    if (smallestOverlap == overlapTop) return collisionSide.BOTTOM;
                    if (smallestOverlap == overlapBottom) return collisionSide.TOP;
                }
            }
            return null;
        }
    }
}