package hhu.game2;

import java.util.Arrays;
import java.util.List;

public class Shot extends Entity {
    public static final int ttl = 70;
    private int liveTime;

    public Shot(double posX, double posY) {
        super(posX, posY);
        this.liveTime = 0;
        setMass(2);
    }

    public int getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(int liveTime) {
        this.liveTime = liveTime;
    }

    @Override
    public double getMaxVelocity() {
        return 30;
    }

    @Override
    public void update() {
        super.update();

        this.liveTime++;
        if (this.liveTime >= ttl) shouldBeDeleted();
    }

    @Override
    public void handleCollision(Entity other) {
        if (collides(other)) {
            if (other instanceof Asteroid asteroid) {
                asteroid.explode(getVelocity().normalize());
                shouldBeDeleted();
            }
        }
    }

    @Override
    public List<Vector2> getShape() {
        return Arrays.asList(
                new Vector2(), // 1
                new Vector2(2, 0), // 2
                new Vector2(2, 2), // 3
                new Vector2(0, 2) // 4
        );
    }

    public void shouldBeDeleted() {
        setMarkedForDeletion(true);
        PlayField playField = PlayField.getInstance();
        playField.setShotCount(playField.getShotCount() - 1);
    }
}
