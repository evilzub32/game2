package hhu.game2;

import java.awt.Color;
import java.util.Arrays;

public class Asteroid extends Entity {
    public Asteroid(int posX, int posY) {
        super(posX, posY, Arrays.asList(
                new Vector2(-20., -40.), // 1
                new Vector2(20., -40.), // 2
                new Vector2(40., -6.), // 3
                new Vector2(8., 6.), // 4
                new Vector2(16., 40.), // 5
                new Vector2(-24., 34.), // 6
                new Vector2(-26., 6.), // 7
                new Vector2(-40., 4.), // 8
                new Vector2(-40., -20.) // 9
        ));

        setVelocity(new Vector2(1, 1));
        setTurnRate(0.4);
        setDefaultColor(Color.GRAY);
    }

    @Override
    public double getMaxVelocity() {
        return 12;
    }
}