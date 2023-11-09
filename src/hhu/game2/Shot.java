package hhu.game2;

import java.util.Arrays;

public class Shot extends Entity {
    private static final int ttl = 70;
    private int liveTime;

    public Shot(double posX, double posY) {
        super(posX, posY, Arrays.asList(
                new Vector2(0, 0), // 1
                new Vector2(2, 0), // 2
                new Vector2(2, 2), // 3
                new Vector2(0, 2) // 4
        ));
        this.liveTime = 0;
    }

    @Override
    public double getMaxVelocity() {
        return 30;
    }

    @Override
    public void update() {
        super.update();

        this.liveTime++;
        if (this.liveTime >= ttl) {
            setMarkedForDeletion(true);
            PlayField playField = PlayField.getInstance();
            playField.setShotCount(playField.getShotCount() - 1);
        }
    }
}
