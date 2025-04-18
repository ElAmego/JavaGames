package Games.MoonLander;

import com.javarush.engine.cell.*;

public class MoonLanderGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private Rocket rocket;
    private GameObject landscape, platform;
    private boolean isUpPressed, isRightPressed, isLeftPressed, isGameStopped;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        showGrid(false);
        createGame();
    }

    private void createGame() {
        isLeftPressed = false;
        isRightPressed = false;
        isUpPressed = false;
        isGameStopped = false;
        score = 1000;

        createGameObjects();
        drawScene();
        setTurnTimer(50);
    }

    private void drawScene() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                setCellColor(x, y, Color.BLACK);
            }
        }

        rocket.draw(this);
        landscape.draw(this);
    }

    private void createGameObjects() {
        rocket = new Rocket((double) WIDTH/2, 0);
        landscape = new GameObject(0, 25, ShapeMatrix.LANDSCAPE);
        platform = new GameObject(23, MoonLanderGame.HEIGHT-1, ShapeMatrix.PLATFORM);
    }

    private void check() {
        if (rocket.isCollision(platform) && rocket.isStopped()) {
            win();
        } else if (rocket.isCollision(landscape)) {
            gameOver();
        }
    }

    private void win() {
        rocket.land();
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "Вы победили!", Color.WHITE, 70);
        stopTurnTimer();
    }

    private void gameOver() {
        rocket.crash();
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "Game Over!", Color.WHITE, 70);
        score = 0;
        stopTurnTimer();
    }

    @Override
    public void onTurn (int step) {
        rocket.move(isUpPressed, isLeftPressed, isRightPressed);
        check();
        drawScene();

        if (score > 0) score--;
        setScore(score);
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x > WIDTH || !(y < HEIGHT && y > 0)) return;

        super.setCellColor(x, y, color);
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.UP) isUpPressed = true;
        else if (key == Key.LEFT) {
            isLeftPressed = true;
            isRightPressed = false;
        } else if (key == Key.RIGHT) {
            isRightPressed = true;
            isLeftPressed = false;
        } else if (key == Key.SPACE && isGameStopped) {
            createGame();
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if (key == Key.UP) isUpPressed = false;
        else if (key == Key.LEFT) isLeftPressed = false;
        else if (key == Key.RIGHT) isRightPressed = false;
    }
}