package hhu.game2;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static void initWindow() {
        JFrame window = new JFrame("Game 2");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PlayField playField = PlayField.getInstance();

        Player player = new Player(PlayField.WIDTH / 2, PlayField.HEIGHT / 2);
        playField.addEntity(player);

        for (Asteroid asteroid : generateAsteroid(6, Asteroid.Size.LARGE)) {
            playField.addEntity(asteroid);
        }

        window.add(playField);
        window.addKeyListener(player);
        window.addKeyListener(playField);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private static List<Asteroid> generateAsteroid(int num, Asteroid.Size size) {
        List<Asteroid> asteroids = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < num; i++) {
            double x = PlayField.WIDTH * rand.nextDouble();
            double y = PlayField.WIDTH * rand.nextDouble();

            double velX = 1 + rand.nextDouble() * 2 * (rand.nextBoolean() ? 1 : -1);
            double velY = 1 + rand.nextDouble() * 2 * (rand.nextBoolean() ? 1 : -1);

            Asteroid asteroid = new Asteroid(x, y, size);
            asteroid.setVelocity(new Vector2(velX, velY));
            asteroids.add(asteroid);
        }

        return asteroids;
    }

    public static void main(String[] args) {
        // TODO: Could use "method reference" in modern Java? What kind of sorcery is this??
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initWindow();
            }
        });
    }
}