package Games.Snake;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";
    public int x, y;
    public boolean isAlive = true;
    private Direction direction = Direction.LEFT;
    private final List<GameObject> snakeParts = new ArrayList<>();

    public Snake(int x, int y) {
        this.x = x;
        this.y = y;

        for (int i = 0; i < 3; i++) {
            snakeParts.add(i, new GameObject(x+i, y));
        }
    }

    public void draw(Game game) {
        for (int i = 0; i < snakeParts.size(); i++) {
            game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, i == 0 ? HEAD_SIGN : BODY_SIGN,
                    isAlive ? Color.BLACK : Color.RED, 75);
        }
    }

    public void move(Apple apple) {
        GameObject newHead = createNewHead();
//        if (newHead.x < 0 || newHead.y < 0 || newHead.x >= SnakeGame.WIDTH || newHead.y >= SnakeGame.HEIGHT) {
//            isAlive = false;
//            return;
//        }

        if (checkCollision(newHead)) {
            isAlive = false;
            return;
        }

        snakeParts.add(0, newHead);

        if (newHead.x == apple.x && newHead.y == apple.y) {
            apple.isAlive = false;
            return;
        }

        removeTail();
    }

    public void setDirection(Direction direction) {
        if (
                (direction == Direction.UP && this.direction == Direction.DOWN)  ||
                (direction == Direction.DOWN && this.direction == Direction.UP)  ||
                (direction == Direction.RIGHT && this.direction == Direction.LEFT)  ||
                (direction == Direction.LEFT && this.direction == Direction.RIGHT)
        ) return;

        if (
                (this.direction == Direction.UP && snakeParts.get(0).y == snakeParts.get(1).y) ||
                (this.direction == Direction.DOWN && snakeParts.get(0).y == snakeParts.get(1).y) ||
                (this.direction == Direction.RIGHT && snakeParts.get(0).x == snakeParts.get(1).x) ||
                (this.direction == Direction.LEFT && snakeParts.get(0).x == snakeParts.get(1).x)
        ) return;

        this.direction = direction;
    }

    public GameObject createNewHead() {
        GameObject gameObject = null;
        GameObject currentHead = snakeParts.get(0);

        if (direction == Direction.LEFT) {
            if (currentHead.x > 0) {
                gameObject = new GameObject(currentHead.x-1, currentHead.y);
            } else {
                gameObject = new GameObject(SnakeGame.HEIGHT-1, currentHead.y);
            }
        } else if (direction == Direction.RIGHT) {
            if (currentHead.x != SnakeGame.WIDTH-1) {
                gameObject = new GameObject(currentHead.x+1, currentHead.y);
            } else {
                gameObject = new GameObject(0, currentHead.y);
            }
        } else if (direction == Direction.UP) {
            if (currentHead.y > 0) {
                gameObject = new GameObject(currentHead.x, currentHead.y-1);
            } else {
                gameObject = new GameObject(currentHead.x, SnakeGame.HEIGHT-1);
            }
        } else if (direction == Direction.DOWN) {
            if (currentHead.y != SnakeGame.HEIGHT-1) {
                gameObject = new GameObject(currentHead.x, currentHead.y+1);
            } else {
                gameObject = new GameObject(currentHead.x, 0);
            }
        }

        return gameObject;
    }

    public void removeTail() { snakeParts.remove(snakeParts.size()-1); }

    public boolean checkCollision(GameObject gameObject) {
        boolean isCollision = false;

        for (GameObject snakePart: snakeParts) {
            if (snakePart.x == gameObject.x && snakePart.y == gameObject.y) {
                isCollision = true;
                break;
            }
        }

        return isCollision;
    }

    public int getLength() { return snakeParts.size(); }
}
