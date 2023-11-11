package hhu.game2;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.List;

public class Player extends Entity implements KeyListener {
    private static final double MAX_THRUST = 0.4;
    private static final double TURN_RATE = 8.0;
    private static final double MAX_VELOCITY = 12;

    private static final double MAX_SHOTS = 20;
    private static final int SHOT_INTERVAL = 5;

    private double thrust;
    private boolean isFiring;
    private int shotCountdown;

    public Player(double posX, double posY) {
        super(posX, posY);
        setDefaultColor(Color.YELLOW);

        thrust = 0;
        isFiring = false;
        shotCountdown = 0;

        setMass(4);
    }

    @Override
    public void update() {
        // Thrust should point upward by default to be aligned with shape rotation
        Vector2 thrustVec = new Vector2(0, -1).rotate(getAngle_deg()).multiply(thrust);

        // add thrust to velocity
        setVelocity(getVelocity().add(thrustVec));

        if (isFiring) fireShot();
        if (shotCountdown > 0) shotCountdown--;

        super.update();
    }

    @Override
    public double getMaxVelocity() {
        return MAX_VELOCITY;
    }

    @Override
    public List<Vector2> getShape() {
        return Arrays.asList(
                new Vector2(0, -20),
                new Vector2(15, 20),
                new Vector2(-15, 20)
        );
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) {
            thrust = MAX_THRUST;
        }
        else if (key == KeyEvent.VK_LEFT) {
            setTurnRate(-TURN_RATE);
        }
        else if (key == KeyEvent.VK_RIGHT)  {
            setTurnRate(TURN_RATE);
        }
        else if (key == KeyEvent.VK_SPACE) {
            isFiring = true;
        }
        else if (key == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) {
            thrust = 0;
        } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            setTurnRate(0);
        } else if (key == KeyEvent.VK_SPACE) {
            isFiring = false;
        }
    }

    @Override
    public void handleCollision(Entity other) {
        if (super.collides(other)) {
            if (other instanceof Asteroid) {
                setCurrentColor(Color.RED);
                collideElastically(other);
            }
        }
    }

    private void fireShot() {
        if (shotCountdown == 0) {
            PlayField playField = PlayField.getInstance();
            int shotCount = playField.getShotCount();
            if (shotCount < SHOT_INTERVAL) {
                Shot shot = new Shot(this.getPos().x, this.getPos().y);
                shot.setVelocity(new Vector2(0, -1).rotate(getAngle_deg()).multiply(getVelocity().magnitude() + 10));
                playField.addEntity(shot);
                playField.setShotCount(playField.getShotCount() + 1);
                shotCountdown = SHOT_INTERVAL;
            }
        }
    }
}