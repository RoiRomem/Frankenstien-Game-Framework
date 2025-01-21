package Engine;

/*
this is probably some of the worst code I've ever written, hopefully this works and I don't have to change it because it honestly scares me 😁😁😁
thanks to chatgpt and his incredible debuging, I can't understand this monstrosity even more 😁😁😁
 */
import java.awt.*;
import java.util.ArrayList;

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
    private collider colliderBody;

    public GameObject(double x, double y, int width, int height) {
        this.objectType = type.EMPTY;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Getters and setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

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
                g2d.drawRect((int) x, (int) y, width, height);
                break;
            case ELLIPSE:
                g2d.drawOval((int) x, (int) y, width, height);
                break;
            case LINE:
                g2d.drawLine((int) x, (int) y, (int) (x + width), (int) (y + height));
                break;
            case ROUND_RECTANGLE:
                g2d.drawRoundRect((int) x, (int) y, width, height, 20, 20);
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
        public static final double GRAVITY = 9.8;
        public static final double FRICTION = 0.1;

        public collider colliderComponent;

        public physics(double mass) {
            this.gameObject = GameObject.this;
            this.mass = mass;
            this.enabled = true;
            this.colliderComponent = new collider(1);
        }

        public void applyForce(direction dir, double force) {
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

        public void updatePhysics() {
            if (!enabled) return;

            // Apply gravity if enabled
            if (isGravityEnabled) {
                netForceY += mass * GRAVITY;
            }

            applyFriction();

            // Calculate accelerations
            accelerationX = netForceX / mass;
            accelerationY = netForceY / mass;

            // Update velocities
            velocityX += accelerationX * timeStep;
            velocityY += accelerationY * timeStep;

            // Predict next position
            double predictedX = gameObject.x + velocityX * timeStep;
            double predictedY = gameObject.y + velocityY * timeStep;

            // Check for collisions
            collider.collisionSide collision = colliderComponent.checkForCollision();

            if (collision != null) {
                switch (collision) {
                    case LEFT -> {
                        velocityX = 0;
                        predictedX = gameObject.x; // Prevent movement to the left
                    }
                    case RIGHT -> {
                        velocityX = 0;
                        predictedX = gameObject.x; // Prevent movement to the right
                    }
                    case TOP -> {
                        velocityY = 0;
                        predictedY = gameObject.y; // Prevent upward movement
                    }
                    case BOTTOM -> {
                        velocityY = 0;
                        predictedY = gameObject.y; // Prevent downward movement
                    }
                    case ALL -> {
                        velocityX = 0;
                        velocityY = 0;
                        predictedX = gameObject.x;
                        predictedY = gameObject.y;
                    }
                }
            }

            // Update actual position
            gameObject.x = predictedX;
            gameObject.y = predictedY;
        }


        private void applyFriction() {
            if (velocityX > 0) {
                netForceX -= mass * GRAVITY * FRICTION;
            } else if (velocityX < 0) {
                netForceX += mass * GRAVITY * FRICTION;
            }

            if (velocityY > 0) {
                netForceY -= mass * GRAVITY * FRICTION;
            } else if (velocityY < 0) {
                netForceY += mass * GRAVITY * FRICTION;
            }
        }

        private void reactToCollision() {
            collider.collisionSide side = colliderComponent.checkForCollision();
            if (side == null) return;

            switch (side) {
                case LEFT -> velocityX = Math.abs(velocityX);
                case RIGHT -> velocityX = -Math.abs(velocityX);
                case TOP -> velocityY = Math.abs(velocityY);
                case BOTTOM -> velocityY = -Math.abs(velocityY);
                case TOPLEFT, TOPRIGHT, BOTTOMLEFT, BOTTOMRIGHT -> {
                    velocityX = -velocityX * 0.5;
                    velocityY = -velocityY * 0.5;
                }
                case ALL -> {
                    velocityX = 0;
                    velocityY = 0;
                }
            }
        }
    }

    // Collider class
    public class collider {
        public enum collisionSide {
            TOP, BOTTOM, LEFT, RIGHT, TOPLEFT, TOPRIGHT, BOTTOMLEFT, BOTTOMRIGHT, ALL
        }

        private GameObject gameObject;
        private int colliderBuffer;

        public collider(int colliderBuffer) {
            this.gameObject = GameObject.this;
            this.colliderBuffer = colliderBuffer;
        }

        public collisionSide checkForCollision() {
            ArrayList<GameObject> gList = Engine.GameObjects;

            for (GameObject g : gList) {
                if (g == gameObject) continue;

                boolean isColliding = g.x < gameObject.x + gameObject.width + colliderBuffer &&
                        g.x + g.width > gameObject.x - colliderBuffer &&
                        g.y < gameObject.y + gameObject.height + colliderBuffer &&
                        g.y + g.height > gameObject.y - colliderBuffer;

                if (isColliding) {
                    // Determine collision side (simplified example)
                    boolean isLeft = gameObject.x > g.x + g.width;
                    boolean isRight = gameObject.x + gameObject.width < g.x;
                    boolean isTop = gameObject.y > g.y + g.height;
                    boolean isBottom = gameObject.y + gameObject.height < g.y;

                    if (isTop) return collisionSide.TOP;
                    if (isBottom) return collisionSide.BOTTOM;
                    if (isLeft) return collisionSide.LEFT;
                    if (isRight) return collisionSide.RIGHT;

                    return collisionSide.ALL; // Fallback for complex overlaps
                }
            }
            return null; // No collision
        }
    }
}
