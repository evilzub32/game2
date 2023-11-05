package hhu.game2;

import java.awt.Point;

public class BoundingBox {
    public double maxX;
    public double maxY;
    public double minX;
    public double minY;

    public BoundingBox() {
        maxX = Double.MIN_VALUE;
        maxY = Double.MIN_VALUE;
        minX = Double.MAX_VALUE;
        minY = Double.MAX_VALUE;
    }

    public boolean collides(BoundingBox other) {
        boolean isAbove = maxY < other.minY;
        boolean isBelow = minY > other.maxY;
        boolean isLeft = maxX < other.minX;
        boolean isRight = minX > other.maxX;

        return !(isAbove || isBelow || isLeft || isRight);
    }
}
