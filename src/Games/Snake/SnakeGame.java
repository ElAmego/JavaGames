package Games.Snake;
import com.javarush.engine.cell.*;

public class SnakeGame extends Game {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    private static final int GOAL = 28;
    private Snake snake;
    private Apple apple;
    private int turnDelay, score;
    private boolean isGameStopped;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame() {
        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        createNewApple();
        isGameStopped = false;
        setScore(score = 0);
        drawScene();
        setTurnTimer(turnDelay = 300);
    }

    private void drawScene() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (i % 2 == 0 ) {
                    if (j % 2 == 0) {
                        setCellValueEx(j, i, Color.GREEN, "");
                    } else {
                        setCellValueEx(i, j, Color.DARKGREEN, "");
                    }
                } else {
                    if (j % 2 != 0) {
                        setCellValueEx(j, i, Color.GREEN, "");
                    } else {
                        setCellValueEx(i, j, Color.DARKGREEN, "");
                    }
                }
            }
        }

        snake.draw(this);
        apple.draw(this);
    }

    private void createNewApple() {
        do {
            apple = new Apple(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));
        } while (snake.checkCollision(apple));
    }

    private void gameOver() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "Вы проиграли!", Color.WHITE, 50);
    }

    private void win() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "Вы выиграли!", Color.WHITE, 50);
    }

    @Override
    public void onTurn(int step) {
        snake.move(apple);

        if (!apple.isAlive) {
            createNewApple();
            setScore(score += 5);
            setTurnTimer(turnDelay -= 10);
        }

        if (!snake.isAlive) gameOver();
        if (snake.getLength() > GOAL) win();

        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.LEFT) {
            snake.setDirection(Direction.LEFT);
        } else if (key == Key.RIGHT) {
            snake.setDirection(Direction.RIGHT);
        } else if (key == Key.UP) {
            snake.setDirection(Direction.UP);
        } else if (key == Key.DOWN) {
            snake.setDirection(Direction.DOWN);
        } else if (key == Key.SPACE && isGameStopped) {
            createGame();
        }
    }
}
