package hhu.game2;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    private static void initWindow() {
        JFrame window = new JFrame("Game 2");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PlayField playField = new PlayField();
        Player player = new Player(PlayField.WIDTH / 2, PlayField.HEIGHT / 2);
        playField.addEntity(player);
        playField.addKeyListener(player);

        playField.addEntity(new Asteroid(0, 0));

        window.add(playField);
        window.addKeyListener(player);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
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