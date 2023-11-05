package hhu.game2;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    private static void initWindow() {
        JFrame window = new JFrame("Game 2");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PlayField playField = new PlayField();

        Player player = new Player(PlayField.WIDTH / 2, PlayField.HEIGHT / 2);
        playField.setPlayer(player);

        for (int i = 0; i < 8; i++) {
            Asteroid asteroid = generateAsteroid();
            playField.addEntity(asteroid);
        }

        window.add(playField);
        window.addKeyListener(player);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private static Asteroid generateAsteroid() {
        int x = (int)(PlayField.WIDTH * Math.random());
        int y = (int)(PlayField.WIDTH * Math.random());

        double turnRate = 0.5 + Math.random() * 2;
        double velX = 1 + Math.random() * 2;
        double velY = 1 + Math.random() * 2;

        Asteroid asteroid = new Asteroid(x, y);
        asteroid.setTurnRate(turnRate);
        asteroid.setVelocity(new Vector2(velX, velY));

        return asteroid;
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