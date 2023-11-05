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

        PlayField playField = new PlayField();

        Player player = new Player(PlayField.WIDTH / 2, PlayField.HEIGHT / 2);
        playField.setPlayer(player);

        for (Asteroid asteroid : generateAsteroid(8)) {
            playField.addEntity(asteroid);
        }

        window.add(playField);
        window.addKeyListener(player);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private static List<Asteroid> generateAsteroid(int num) {
        List<Asteroid> asteroids = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < num; i++) {
            int x = (int) (PlayField.WIDTH * rand.nextDouble());
            int y = (int) (PlayField.WIDTH * rand.nextDouble());

            double turnRate = 0.5 + rand.nextDouble() * 2 * (rand.nextBoolean() ? 1 : -1);
            double velX = 1 + rand.nextDouble() * 2 * (rand.nextBoolean() ? 1 : -1);
            double velY = 1 + rand.nextDouble() * 2* (rand.nextBoolean() ? 1 : -1);

            Asteroid asteroid = new Asteroid(x, y);
            asteroid.setTurnRate(turnRate);
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