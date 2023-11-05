package hhu.game2;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public class Player extends Entity implements KeyListener {
    private static final double MAX_THRUST = 0.4;
    private static final double TURN_RATE = 8.0;
    private static final double MAX_VELOCITY = 12;

    private double thrust;

    public Player(int posX, int posY) {
        super(posX, posY, Arrays.asList(
                new Vector2(0, -20),
                new Vector2(15, 20),
                new Vector2(-15, 20)
        ));
        setDefaultColor(Color.YELLOW);

        thrust = 0;

    }

    @Override
    public void update() {
        // Thrust should point upward by default to be aligned with shape rotation
        Vector2 thrustVec = new Vector2(0, -1).rotate(getAngle_deg()).multiply(thrust);

        // add thrust to velocity
        setVelocity(getVelocity().add(thrustVec));

        super.update();
    }

    @Override
    public double getMaxVelocity() {
        return MAX_VELOCITY;
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
        }
    }
}