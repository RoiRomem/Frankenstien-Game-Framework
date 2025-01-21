package Engine;
/*
this is probably some of the worst code I've ever written, hopefully this works and I dont have to change it because it honestly scares me 😁😁😁
 */
import java.awt.*;
import java.util.ArrayList;

public class GameObject {
    enum type {
        EMPTY,
        SPRITE,
        TEXT,
        RECTANGLE,
        ELLIPSE,
        LINE,
        ROUND_RECTANGLE
    }

    public type type;

    // Transform
    public static double x;
    public static double y;
    public int width;
    public int height;
    private Image image;
    private Color color;
    public String text;
    private Font font;

    public GameObject(double x, double y, int width, int height) {
        this.type = type.EMPTY;
        GameObject.x = x;
        GameObject.y = y;
        this.width = width;
        this.height = height;
    }

    public void SelectShape(type type) {
        this.type = type;
    }

    public void addImage(Image image) {
        this.image = image;
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
        this.type = type.SPRITE;
    }

    public void addColor(Color color) {
        this.color = color;
    }

    public void addText(String text, Font font) {
        this.text = text;
        this.font = font;
        this.type = type.TEXT;
    }

    void Draw(Graphics g) {
        if (type != type.EMPTY) {
            Graphics2D g2d = (Graphics2D) g;
            switch (type) {
                case SPRITE:
                    g2d.drawImage(image, (int) x, (int) y, null);
                    break;
                case TEXT:
                    g2d.setFont(font);
                    g2d.setColor(color);
                    g2d.drawString(text, (int) x, (int) y);
                    break;
                case RECTANGLE:
                    g2d.setColor(color);
                    g2d.drawRect((int) x, (int) y, width, height);
                    break;
                case ELLIPSE:
                    g2d.setColor(color);
                    g2d.drawOval((int) x, (int) y, width, height);
                    break;
                case LINE:
                    g2d.setColor(color);
                    g2d.drawLine((int) x, (int) y, (int) x, (int) y);
                    break;
                case ROUND_RECTANGLE:
                    g2d.setColor(color);
                    g2d.drawRoundRect((int) x, (int) y, width, height, 20, 20);
                    break;
            }
        }
    }

    // Physics class
    public class physics extends GameObject {
        enum direction { RIGHT, LEFT, UP, DOWN }

        public double mass; // Mass of the object (kg)
        public double timeStep = 0.016; // Time step (for smooth updates)
        public boolean enabled = false;

        public double velocityX = 0;
        public double velocityY = 0;

        public double accelerationX = 0;
        public double accelerationY = 0;

        private double netForceX = 0;
        private double netForceY = 0;

        public boolean isGravityEnabled = true;
        public double gravity = 9.8; // Earth's gravity (m/s^2)

        public double frictionCoefficient = 0.1; // Friction

        // I'm Limited by the technology of my time, thus I cannot comprehend the rest of this subclass I wrote here

        public physics(double x, double y, int width, int height, double mass) {
            super(x, y, width, height);
            this.mass = mass;
            this.enabled = true;
        }

        public void applyForce(direction dir, double force) {
            switch (dir) {
                case RIGHT:
                    netForceX += force;
                    break;
                case LEFT:
                    netForceX -= force;
                    break;
                case UP:
                    netForceY -= force;
                    break;
                case DOWN:
                    netForceY += force;
                    break;
            }
        }

        public void toggleGravity() {
            isGravityEnabled = !isGravityEnabled;
        }

        public void updatePhysics() {
            if (!enabled) return;

            // Reset forces for this frame
            netForceX = 0;
            netForceY = 0;

            // Apply gravity
            if (isGravityEnabled) {
                netForceY += mass * gravity;
            }

            // Apply friction (basic implementation)
            applyFriction();

            // Calculate acceleration (F = ma)
            accelerationX = netForceX / mass;
            accelerationY = netForceY / mass;

            // Update velocity (v = u + at)
            velocityX += accelerationX * timeStep;
            velocityY += accelerationY * timeStep;

            // Update position (s = s + vt)
            GameObject.x += velocityX * timeStep;
            GameObject.y += velocityY * timeStep;
        }

        private void applyFriction() {
            // Oppose velocity with frictional force
            if (velocityX > 0) {
                netForceX -= mass * gravity * frictionCoefficient;
            } else if (velocityX < 0) {
                netForceX += mass * gravity * frictionCoefficient;
            }

            if (velocityY > 0) {
                netForceY -= mass * gravity * frictionCoefficient;
            } else if (velocityY < 0) {
                netForceY += mass * gravity * frictionCoefficient;
            }
        }
    }

    // Collider class
    public class collider extends GameObject {
        enum collisionSide {
            TOP, BOTTOM, LEFT, RIGHT, TOPLEFT, TOPRIGHT, BOTTOMLEFT, BOTTOMRIGHT
        }

        private int pos[] = {0, 0};
        private int size[] = {0, 0};
        public static int colliderBuffer;
        public boolean triggerCollider = false;
        public boolean triggerCollision = false;

        // this one might be understandable

        public collider(int x, int y, int width, int height, int colliderBuffer) {
            super(x, y, width, height);
            this.pos[0] = x;
            this.pos[1] = y;
            this.size[0] = width;
            this.size[1] = height;
            collider.colliderBuffer = colliderBuffer;
        }

        public collisionSide checkForCollision() {
            ArrayList<GameObject> gList = Engine.GameObjects; // Assuming Engine.GameObjects contains all game objects
            triggerCollision = false;

            for (GameObject g : gList) {
                if (g == this) continue; // Avoid self-collision

                if (g.x < pos[0] + size[0] + colliderBuffer &&
                        g.x + g.width > pos[0] - colliderBuffer &&
                        g.y < pos[1] + size[1] + colliderBuffer &&
                        g.y + g.height > pos[1] - colliderBuffer) {

                    triggerCollision = true;

                    if (!triggerCollider) {
                        boolean topCollision = pos[1] > g.y + g.height;
                        boolean bottomCollision = pos[1] + size[1] < g.y;
                        boolean leftCollision = pos[0] > g.x + g.width;
                        boolean rightCollision = pos[0] + size[0] < g.x;

                        if (topCollision && leftCollision) return collisionSide.TOPLEFT;
                        if (topCollision && rightCollision) return collisionSide.TOPRIGHT;
                        if (bottomCollision && leftCollision) return collisionSide.BOTTOMLEFT;
                        if (bottomCollision && rightCollision) return collisionSide.BOTTOMRIGHT;
                        if (topCollision) return collisionSide.TOP;
                        if (bottomCollision) return collisionSide.BOTTOM;
                        if (leftCollision) return collisionSide.LEFT;
                        if (rightCollision) return collisionSide.RIGHT;
                    } else {
                        return null; // Trigger-only collision
                    }
                }
            }
            return null; // No collision detected
        }
    }
}
