package Games.MoonLander;

import com.javarush.engine.cell.*;

import java.util.Arrays;

public class Rocket extends GameObject {
    private double speedY = 0;
    private double speedX = 0;
    private final double BOOST = 0.05;
    private final double SLOWDOWN = BOOST / 10;
    private final RocketFire DOWN_FIRE, LEFT_FIRE, RIGHT_FIRE;

    public Rocket(double x, double y) {
        super(x, y, ShapeMatrix.ROCKET);
        DOWN_FIRE = new RocketFire(Arrays.asList(ShapeMatrix.FIRE_DOWN_1, ShapeMatrix.FIRE_DOWN_2, ShapeMatrix.FIRE_DOWN_3));
        LEFT_FIRE = new RocketFire(Arrays.asList(ShapeMatrix.FIRE_SIDE_1, ShapeMatrix.FIRE_SIDE_2));
        RIGHT_FIRE = new RocketFire(Arrays.asList(ShapeMatrix.FIRE_SIDE_1, ShapeMatrix.FIRE_SIDE_2));
    }

    public void move(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed) {
        if (isUpPressed) {
            speedY -= BOOST;
        } else {
            speedY += BOOST;
        }
        y += speedY;

        if (isLeftPressed) {
            speedX -= BOOST;
            x += speedX;
        } else if (isRightPressed) {
            speedX += BOOST;
            x += speedX;
        } else if (speedX > SLOWDOWN) {
            speedX -= SLOWDOWN;
        } else if (speedX < -SLOWDOWN) {
            speedX += SLOWDOWN;
        } else {
            speedX = 0;
        }
        x += speedX;
        checkBorders();
        switchFire(isUpPressed, isLeftPressed, isRightPressed);
    }

    private void checkBorders() {
        if (x < 0) {
            x = 0;
            speedX = 0;
        } else if (x + width > MoonLanderGame.WIDTH) {
            x = MoonLanderGame.WIDTH - width;
            speedX = 0;
        }

        if (y <= 0) {
            y = 0;
            speedY = 0;
        }
    }

    public boolean isStopped() {
        return speedY < 10 * BOOST;
    }

    public boolean isCollision(GameObject object) {
        int transparent = Color.NONE.ordinal();

        for (int matrixX = 0; matrixX < width; matrixX++) {
            for (int matrixY = 0; matrixY < height; matrixY++) {
                int objectX = matrixX + (int) x - (int) object.x;
                int objectY = matrixY + (int) y - (int) object.y;

                if (objectX < 0 || objectX >= object.width || objectY < 0 || objectY >= object.height) {
                    continue;
                }

                if (matrix[matrixY][matrixX] != transparent && object.matrix[objectY][objectX] != transparent) {
                    return true;
                }
            }
        }
        return false;
    }

    public void land() {
        y -= 1;
    }

    public void crash() {
        matrix = ShapeMatrix.ROCKET_CRASH;
    }

    private void switchFire(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed) {
        if (isLeftPressed) {
            LEFT_FIRE.x = x + width;
            LEFT_FIRE.y = y + height;
            LEFT_FIRE.show();
        } else {
            LEFT_FIRE.hide();
        }

        if (isRightPressed) {
            RIGHT_FIRE.x = x - ShapeMatrix.FIRE_SIDE_1[0].length;
            RIGHT_FIRE.y = y + height;
            RIGHT_FIRE.show();
        } else  {
            RIGHT_FIRE.hide();
        }

        if (isUpPressed) {
            DOWN_FIRE.x = x + ((double) width /2);
            DOWN_FIRE.y = y + height;
            DOWN_FIRE.show();
        } else {
            DOWN_FIRE.hide();
        }
    }

    public void draw(Game game) {
        super.draw(game);
        DOWN_FIRE.draw(game);
        LEFT_FIRE.draw(game);
        RIGHT_FIRE.draw(game);
    }
}
