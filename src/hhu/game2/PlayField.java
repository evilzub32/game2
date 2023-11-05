package hhu.game2;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class PlayField extends JPanel implements ActionListener {
    private final int DELAY = 25;

    public static final int WIDTH = 1400;
    public static final int HEIGHT = 980;

    // keep a reference to the timer object that triggers actionPerformed() in
    // case we need access to it in another method
    private Timer timer;

    private List<Entity> entities;

    public PlayField() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);

        entities = new ArrayList<>();

        // this timer will call the actionPerformed() method every DELAY ms
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Entity entity : entities) {
            entity.update();
        }

        for (int i = 0; i < entities.size() - 1; i++) {
            Entity me = entities.get(i);
            for (int j = i + 1; j < entities.size(); j++) {
                Entity other = entities.get(j);
                if (me.collides(other)) {
                    me.setCurrentColor(Color.RED);
                    other.setCurrentColor(Color.RED);
                }
            }
        }

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Entity entity : entities) {
            handleWrapAround(entity);
            entity.draw(g, this);
        }

        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
    }

    public void addEntity(Entity entity) {
        if (!entities.contains(entity)) {
            entities.add(entity);
        }
    }

    private void handleWrapAround(Entity entity) {
        Point p = entity.getPos();
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
}