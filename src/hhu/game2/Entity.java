package hhu.game2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
    private double turnRate;
    private double angle_deg;

    private Vector2 velocity;

    private Vector2 pos;
    private List<Vector2> shape;
    private List<Vector2> rotatedShape;

    private Color defaultColor;
    private Color currentColor;

    private double mass = 10;

    private boolean markedForDeletion;

    private int lastCollisionTick;

    public Entity(double posX, double posY) {
        turnRate = 0;
        angle_deg = 0;

        velocity = new Vector2();

        pos = new Vector2(posX, posY);

        this.defaultColor = Color.WHITE;
        this.currentColor = defaultColor;

        this.markedForDeletion = false;

        this.lastCollisionTick = 0;
    }

    public abstract double getMaxVelocity();

    public abstract void handleCollision(Entity other);

    public boolean isMarkedForDeletion() {
        return markedForDeletion;
    }

    public void setMarkedForDeletion(boolean markedForDeletion) {
        this.markedForDeletion = markedForDeletion;
    }

    public void update() {
        angle_deg += turnRate;
        if (angle_deg < 0) angle_deg = angle_deg + 360;
        else if (angle_deg > 360) angle_deg = angle_deg - 360;

        // limit velocity to MAX_SPEED
        double speed = velocity.magnitude();
        if (speed > getMaxVelocity()) {
            double limiter = getMaxVelocity() / speed;
            velocity = velocity.multiply(limiter);
        }

        // update position
        pos = pos.add(velocity);

        update_shape();

        currentColor = defaultColor;

        if (lastCollisionTick > 0) {
            lastCollisionTick--;
        }
    }

    private void update_shape() {
        rotatedShape = new ArrayList<>();
        for (Vector2 shapePoint : getShape()) {
            Vector2 rotated = shapePoint.rotate(angle_deg);

            rotatedShape.add(new Vector2(pos.x + rotated.x, pos.y + rotated.y));
        }
    }

    public void draw(Graphics g, boolean showBoundingBox, boolean showVelocity) {
        Graphics2D g2d = (Graphics2D) g;

        Polygon polygon = new Polygon();
        for (Vector2 point : getRotatedShape()) {
            polygon.addPoint((int)point.x, (int)point.y);
        }

        g2d.setColor(currentColor);
        g2d.draw(polygon);

        if (showBoundingBox) {
            polygon = new Polygon();
            polygon.addPoint((int) getBoundingBox().leftX, (int) getBoundingBox().lowerY);
            polygon.addPoint((int) getBoundingBox().rightX, (int) getBoundingBox().lowerY);
            polygon.addPoint((int) getBoundingBox().rightX, (int) getBoundingBox().upperY);
            polygon.addPoint((int) getBoundingBox().leftX, (int) getBoundingBox().upperY);
            g2d.setColor(Color.GRAY);
            g2d.draw(polygon);
        }

        if (showVelocity) {
            g2d.setColor(Color.YELLOW);
            g2d.drawLine((int) pos.x, (int) pos.y, (int) (pos.x + velocity.x * 10), (int) (pos.y + velocity.y * 10));
        }
    }

    public BoundingBox getBoundingBox() {
        BoundingBox box = new BoundingBox();
        for (Vector2 point : rotatedShape) {
            if (point.x > box.rightX) box.rightX = point.x;
            if (point.x < box.leftX) box.leftX = point.x;
            if (point.y > box.lowerY) box.lowerY = point.y;
            if (point.y < box.upperY) box.upperY = point.y;
        }
        return box;
    }

    public void collideElastically(Entity other) {
        // Make nomenclature compatible with wikipedia formula
        // https://en.wikipedia.org/wiki/Elastic_collision#Two-dimensional
        Vector2 v1 = velocity;
        Vector2 v2 = other.velocity;
        Vector2 x1 = pos;
        Vector2 x2 = other.pos;
        double m1 = getMass();
        double m2 = other.getMass();

        // try to make the formula human-readable by splitting it in parts
        // calc my new velocity vector
        double mq1 = (2 * m2) / (m1 + m2);
        double dotProd1 = v1.sub(v2).dotProduct(x1.sub(x2));
        double distSquare1 = Math.pow(x1.sub(x2).magnitude(), 2);
        Vector2 dist1 = x1.sub(x2);
        Vector2 v1b = v1.sub(dist1.multiply(mq1 * (dotProd1 / distSquare1)));
        // calc the other's new velocity vector
        double mq2 = (2 * m1) / (m1 + m2);
        double dotProd2 = v2.sub(v1).dotProduct(x2.sub(x1));
        double distSquare2 = Math.pow(x2.sub(x1).magnitude(), 2);
        Vector2 dist2 = x2.sub(x1);
        Vector2 v2b = v2.sub(dist2.multiply(mq2 * (dotProd2 / distSquare2)));

        this.velocity = v1b;
        other.velocity = v2b;

        // Overlapping entities will stick together, so ensure entities are not overlapping.
        // TODO: Simplify
        BoundingBox myBB = getBoundingBox();
        BoundingBox otherBB = other.getBoundingBox();

        boolean intersectsFromAbove = myBB.upperY < otherBB.lowerY && myBB.lowerY > otherBB.lowerY;
        boolean intersectsFromBelow = myBB.lowerY > otherBB.upperY && myBB.lowerY < otherBB.lowerY;
        boolean intersectsFromLeft = myBB.leftX < otherBB.rightX && myBB.rightX > otherBB.rightX;
        boolean intersectsFromRight = myBB.rightX > otherBB.leftX && myBB.leftX < otherBB.leftX;

        Vector2 oPos = other.pos;
        if (intersectsFromAbove && intersectsFromLeft) {
            double diffY = otherBB.lowerY - myBB.upperY;
            double diffX = otherBB.rightX - myBB.leftX;
            if (diffY > diffX) oPos.x -= diffX;
            else oPos.y -= diffY;
        } else if (intersectsFromAbove && intersectsFromRight) {
            double diffY = otherBB.lowerY - myBB.upperY;
            double diffX = myBB.rightX - otherBB.leftX;
            if (diffY > diffX) oPos.x += diffX;
            else oPos.y -= diffY;
        } else if (intersectsFromBelow && intersectsFromLeft) {
            double diffY = myBB.lowerY - otherBB.upperY;
            double diffX = otherBB.rightX - myBB.leftX;
            if (diffY > diffX) oPos.x -= diffX;
            else oPos.y += diffY;
        } else if (intersectsFromBelow && intersectsFromRight) {
            double diffY = myBB.lowerY - otherBB.upperY;
            double diffX = myBB.rightX - otherBB.leftX;
            if (diffY > diffX) oPos.x += diffX;
            else oPos.y += diffY;
        }
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    public boolean collides(Entity other) {
        if (getBoundingBox().collides(other.getBoundingBox())) {
            // Grace period after collision to avoid colliding with the same object multiple times successively
            if (lastCollisionTick > 0 || other.lastCollisionTick > 0) {
                return false;
            } else {
                lastCollisionTick = 5;
                return true;
            }
        }
        return false;
    }

    public void setTurnRate(double turnRate) {
        this.turnRate = turnRate;
    }

    public double getAngle_deg() {
        return angle_deg;
    }

    public void setAngle_deg(double angle_deg) {
        this.angle_deg = angle_deg;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getPos() {
        return pos;
    }

    public List<Vector2> getRotatedShape() {
        return rotatedShape;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public List<Vector2> getShape() {
        return shape;
    }

    public void setShape(List<Vector2> shape) {
        this.shape = shape;
    }
}
