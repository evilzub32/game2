package hhu.game2;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class PlayField extends JPanel implements ActionListener, KeyListener {
    private final int DELAY = 25;

    public static final int WIDTH = 1400;
    public static final int HEIGHT = 980;

    // keep a reference to the timer object that triggers actionPerformed() in
    // case we need access to it in another method
    private final Timer timer;

    private final List<Entity> entities;

    private static PlayField playField;

    private int shotCount;

    private boolean showVelocity;
    private boolean showBoundingBox;

    private PlayField() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);

        entities = new ArrayList<>();
        shotCount = 0;

        showVelocity = false;
        showBoundingBox = false;

        // this timer will call the actionPerformed() method every DELAY ms
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public static PlayField getInstance() {
        if (null == PlayField.playField) {
            PlayField.playField = new PlayField();
        }
        return PlayField.playField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<Entity> entityList = new ArrayList<>(entities);
        for (Entity entity : entityList) {
            if (entity.isMarkedForDeletion()) {
                entities.remove(entity);
            } else {
                entity.update();
                handleWrapAround(entity);
            }
        }

        detectCollisions();

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Entity entity : entities) {
            entity.draw(g, showBoundingBox, showVelocity);
        }

        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
    }

    public void addEntity(Entity entity) {
        entity.update();
        entities.add(entity);
    }

    public int getShotCount() {
        return shotCount;
    }

    public void setShotCount(int shotCount) {
        this.shotCount = shotCount;
    }

    private void handleWrapAround(Entity entity) {
        Vector2 p = entity.getPos();
        if (p.x > WIDTH) {
            p.x = p.x - WIDTH;
        } else if (p.x < 0) {
            p.x = WIDTH + p.x;
        }

        if (p.y > HEIGHT) {
            p.y = p.y - HEIGHT;
        } else if (p.y < 0) {
            p.y = HEIGHT + p.y;
        }
    }

    private void detectCollisions() {
        int entityCount = entities.size();
        for (int i = 0; i < entityCount - 1; i++) {
            Entity me = entities.get(i);
            for (int j = i + 1; j < entityCount; j++) {
                Entity other = entities.get(j);
                me.handleCollision(other);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        } else if (key == KeyEvent.VK_P) {
            if (timer.isRunning()) {
                timer.stop();
            } else {
                timer.start();
            }
        } else if (key == KeyEvent.VK_F1) {
            showBoundingBox = !showBoundingBox;
        } else if (key == KeyEvent.VK_F2) {
            showVelocity = !showVelocity;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}