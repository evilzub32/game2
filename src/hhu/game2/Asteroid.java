package hhu.game2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Asteroid extends Entity {
    public enum Size {
        SMALL(2, 5),
        MID(5, 8),
        LARGE(8, 11);

        public final int min;
        public final int max;

        Size(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }

    private List<Vector2> shape;

    public Asteroid(double posX, double posY, Size size) {
        super(posX, posY);
        setDefaultColor(Color.GRAY);

        double mass = generateMass(size);
        List<Vector2> shape = generateShape(mass);
        setMass(mass);
        setShape(shape);
    }

    @Override
    public double getMaxVelocity() {
        return 12;
    }

    @Override
    public void handleCollision(Entity other) {
        if (collides(other)) {
            if (other instanceof Asteroid) {
                collideElastically(other);
            } else if (other instanceof Shot shot) {
                shot.shouldBeDeleted();
                collideElastically(other);
            }
        }
    }

    private static double generateMass(Size size) {
        return size.min + Math.random() * (size.max - size.min);
    }

    private static List<Vector2> generateShape(double mass) {
        Random rand = new Random();
        List<Vector2> shape = new ArrayList<>();

        int maxNodes = 6;
        double step = 360d / maxNodes;
        double r = 7 * mass;

        for (int i = 0; i < maxNodes; i++) {
            double deg = i * step;
            double sigma = Math.toRadians(deg);
            double x = r * Math.sin(sigma) + rand.nextDouble(10) * (rand.nextBoolean() ? 1 : -1);
            double y = r * Math.cos(sigma) + rand.nextDouble(10) * (rand.nextBoolean() ? 1 : -1);
            shape.add(new Vector2(x, y));
        }

        return shape;
    }
}
