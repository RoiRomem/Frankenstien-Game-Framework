package Engine;
/*
this is probably some of the worst code I've ever written, hopefully this works and I dont have to change it because it honestly scares me 😁😁😁
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

    // Transform
    public double x;
    public double y;
    public int width;
    public int height;
    private Image image;
    private Color color;
    public String text;
    private Font font;

    // Components
    public physics physicsBody;
    public collider colliderBody;

    public GameObject(double x, double y, int width, int height) {
        this.objectType = type.EMPTY;
        this.x = x;
        this.y = y;
        this.width = width;
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

    void Draw(Graphics g) {
        if (objectType != type.EMPTY) {
            Graphics2D g2d = (Graphics2D) g;
            switch (objectType) {
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
        public double gravity = 9.8;

        public double frictionCoefficient = 0.1;

        public physics(double mass) {
            this.gameObject = GameObject.this;
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

            netForceX = 0;
            netForceY = 0;

            if (isGravityEnabled) {
                netForceY += mass * gravity;
            }

            applyFriction();

            accelerationX = netForceX / mass;
            accelerationY = netForceY / mass;

            velocityX += accelerationX * timeStep;
            velocityY += accelerationY * timeStep;

            gameObject.x += velocityX * timeStep;
            gameObject.y += velocityY * timeStep;
        }

        private void applyFriction() {
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
    public class collider {
        public enum collisionSide {
            TOP, BOTTOM, LEFT, RIGHT, TOPLEFT, TOPRIGHT, BOTTOMLEFT, BOTTOMRIGHT
        }

        private GameObject gameObject;
        private int[] pos = new int[2];
        private int[] size = new int[2];
        private int colliderBuffer;
        public boolean triggerCollider = false;
        public boolean triggerCollision = false;

        public collider(int colliderBuffer) {
            this.gameObject = GameObject.this;
            this.pos[0] = (int)gameObject.x;
            this.pos[1] = (int)gameObject.y;
            this.size[0] = gameObject.width;
            this.size[1] = gameObject.height;
            this.colliderBuffer = colliderBuffer;
        }

        public collisionSide checkForCollision() {
            ArrayList<GameObject> gList = Engine.GameObjects;
            triggerCollision = false;

            // Update position to match parent GameObject
            pos[0] = (int)gameObject.x;
            pos[1] = (int)gameObject.y;

            for (GameObject g : gList) {
                if (g == gameObject) continue; // Avoid self-collision

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
                    }
                }
            }
            return null;
        }
    }
}