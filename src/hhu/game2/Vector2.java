package hhu.game2;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Vector2 {
    public double x, y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(
                this.x + other.x,
                this.y + other.y
        );
    }

    public Vector2 add(double num) {
        return new Vector2(
                this.x + num,
                this.y + num
        );
    }

    public Vector2 sub(Vector2 other) {
        return new Vector2(
                this.x - other.x,
                this.y - other.y
        );
    }

    public Vector2 rotate(double deg) {
        double angle_rad = Math.toRadians(deg);

        return new Vector2(
            cos(angle_rad) * this.x - sin(angle_rad) * this.y,
            sin(angle_rad)  * this.x + cos(angle_rad) * this.y
        );
    }

    public Vector2 multiply(double factor) {
        return new Vector2(
                this.x * factor,
                this.y * factor
        );
    }

    public double magnitude() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public Vector2 normalize() {
        double factor = 1 / this.magnitude();
        return new Vector2(
          this.x * factor,
          this.y * factor
        );
    }

    public double dotProduct(Vector2 other) {
        return this.x * other.x + this.y * other.y;
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
