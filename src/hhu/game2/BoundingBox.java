package hhu.game2;

public class BoundingBox {
    public double rightX;
    public double lowerY;
    public double leftX;
    public double upperY;

    public BoundingBox() {
        // guarantee that real world values are always bigger or lower than initial values
        rightX = Double.MIN_VALUE;
        lowerY = Double.MIN_VALUE;
        leftX = Double.MAX_VALUE;
        upperY = Double.MAX_VALUE;
    }

    public boolean isAbove(BoundingBox other) {
        return lowerY < other.upperY;
    }

    public boolean isBelow(BoundingBox other) {
        return upperY > other.lowerY;
    }

    public boolean isLeft(BoundingBox other) {
        return rightX < other.leftX;
    }

    public boolean isRight(BoundingBox other) {
        return leftX > other.rightX;
    }

    public boolean collides(BoundingBox other) {
        return !(isAbove(other) || isBelow(other) || isLeft(other) || isRight(other));
    }
}
