package hhu.game2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
    private double turnRate;
    private double angle_deg;

    private Vector2 velocity;

    private Point pos;
    private List<Vector2> shape;
    private List<Vector2> rotatedShape;
    private Color defaultColor;
    private Color currentColor;

    public Entity(int posX, int posY, List<Vector2> shape) {
        turnRate = 0;
        angle_deg = 0;

        velocity = new Vector2(0,0);

        pos = new Point(posX, posY);

        this.shape = shape;
        this.rotatedShape = shape;

        this.defaultColor = Color.WHITE;
        this.currentColor = defaultColor;
    }

    public abstract double getMaxVelocity();

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
        pos.x += velocity.x;
        pos.y += velocity.y;

        update_shape();

        currentColor = defaultColor;
    }

    private void update_shape() {
        rotatedShape = new ArrayList<>();
        for (Vector2 shapePoint : shape) {
            Vector2 rotated = shapePoint.rotate(angle_deg);
            rotatedShape.add(new Vector2(pos.x + rotated.x, pos.y + rotated.y));
        }
    }

    public void draw(Graphics g, ImageObserver observer) {
        Graphics2D g2d = (Graphics2D) g;

        Polygon polygon = new Polygon();
        for (Vector2 point : getRotatedShape()) {
            polygon.addPoint((int)point.x, (int)point.y);
        }

        g2d.setColor(currentColor);
        g2d.draw(polygon);
    }

    public BoundingBox getBoundingBox() {
        BoundingBox box = new BoundingBox();
        for (Vector2 point : rotatedShape) {
            if (point.x > box.maxX) box.maxX = point.x;
            if (point.x < box.minX) box.minX = point.x;
            if (point.y > box.maxY) box.maxY = point.y;
            if (point.y < box.minY) box.minY = point.y;
        }
        return box;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    public boolean collides(Entity other) {
        return getBoundingBox().collides(other.getBoundingBox());
    }

    public double getTurnRate() {
        return turnRate;
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

    public Point getPos() {
        return pos;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public List<Vector2> getShape() {
        return shape;
    }

    public void setShape(List<Vector2> shape) {
        this.shape = shape;
    }

    public List<Vector2> getRotatedShape() {
        return rotatedShape;
    }

    public void setRotatedShape(List<Vector2> rotatedShape) {
        this.rotatedShape = rotatedShape;
    }
}
