package hhu.game2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Asteroid extends Entity {
    private List<Vector2> shape;

    public Asteroid(double posX, double posY) {
        super(posX, posY);
        setDefaultColor(Color.GRAY);
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

    @Override
    public List<Vector2> getShape() {
        if (null == shape) {
            shape = new ArrayList<>();

            int maxNodes = 10;
            double step = 360 / maxNodes;
            double r = 5 * getMass();
            System.out.println(r);

            for (int i = 0; i < 10; i++) {
                double deg = i * step;
                double sigma = Math.toRadians(i * step);
                double x = r * Math.sin(sigma);
                double y = r * Math.cos(sigma);
                shape.add(new Vector2(x, y));
            }
        }
        return shape;
    }
}
