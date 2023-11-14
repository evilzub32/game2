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

    private Size size;

    public Asteroid(double posX, double posY, Size size) {
        super(posX, posY);
        setDefaultColor(Color.GRAY);

        double mass = generateMass(size);
        List<Vector2> shape = generateShape(mass);
        setMass(mass);
        setShape(shape);
        setTurnRate(0.5 + Math.random() * 2);
        setAngle_deg(Math.random() * 360);

        this.size = size;

        update();
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
                explode(shot.getVelocity().normalize());
            }
        }
    }

    public void explode(Vector2 extraVelocity) {
        Size newSize = null;
        if (size == Size.LARGE) {
            newSize = Size.MID;
        } else if (size == Size.MID) {
            newSize = Size.SMALL;
        } else if (size == Size.SMALL) {
            setMarkedForDeletion(true);
            return;
        }

        PlayField playField = PlayField.getInstance();

        Asteroid newAst = new Asteroid(getPos().x, getPos().y, newSize);
        newAst.getPos().x = getBoundingBox().rightX + 5;
        newAst.setVelocity(getVelocity().add(extraVelocity));
        playField.addEntity(newAst);

        newAst = new Asteroid(getPos().x, getPos().y, newSize);
        newAst.getPos().x = getBoundingBox().leftX - 5;
        newAst.setVelocity(getVelocity().add(extraVelocity));
        playField.addEntity(newAst);

        newAst = new Asteroid(getPos().x, getPos().y, newSize);
        newAst.getPos().y = getBoundingBox().upperY - 5;
        newAst.setVelocity(getVelocity().add(extraVelocity));
        playField.addEntity(newAst);

        newAst = new Asteroid(getPos().x, getPos().y, newSize);
        newAst.getPos().y = getBoundingBox().lowerY + 5;
        newAst.setVelocity(getVelocity().add(extraVelocity));
        playField.addEntity(newAst);

        this.setMarkedForDeletion(true);
    }

    public void explode() {
        explode(new Vector2());
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
