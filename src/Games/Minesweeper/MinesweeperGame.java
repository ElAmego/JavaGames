package Games.Minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private static final int SIDE = 9;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countClosedTiles = SIDE * SIDE;
    private int countMinesOnField, countFlags, score;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";
    private boolean isGameStopped;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        setTurnTimer(10);
        createGame();
    }

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                boolean isMine = getRandomNumber(10) < 1;
                if (isMine) {
                    countMinesOnField++;
                }
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellValueEx(x, y, Color.DARKGRAY, "", Color.BLACK);
            }
        }

        countFlags = countMinesOnField;
        countMineNeighbors();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (!isGameStopped) {
            openTile(x, y);
        } else {
            restart();
        }
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        if (!isGameStopped) markTile(x, y);
    }

    private void openTile(int x, int y) {
        GameObject gameObject = gameField[y][x];

        if (isGameStopped || gameObject.isOpen() || gameObject.isFlag()) return;

        gameObject.setOpen(true);
        countClosedTiles--;

        if (gameObject.isMine()) {
            setCellValueEx(x, y, Color.RED, MINE);
            gameOver();
        } else {
            int countMineNeighbors = gameObject.getCountMineNeighbors();

            if (countMineNeighbors == 0) {
                for (GameObject neighbor: getNeighbors(gameObject)) {
                    if (!neighbor.isOpen()) {
                        openTile(neighbor.getX(), neighbor.getY());
                    }
                }
            }

            setCellValueEx(x, y, Color.GRAY,countMineNeighbors == 0 ? "" : String.valueOf(countMineNeighbors), getColor(countMineNeighbors));
            score += 5;
            onTurn(10);
            if (countClosedTiles == countMinesOnField)  win();
        }
    }

    private void markTile(int x, int y) {
        GameObject gameObject = gameField[y][x];
        if (!gameObject.isOpen() && countFlags != 0 && !gameObject.isFlag()) {
            gameObject.setFlag(true);
            countFlags--;
            setCellValue(x, y, FLAG);
        } else if (gameObject.isFlag()) {
            gameObject.setFlag(false);
            countFlags++;
            setCellValue(x, y,"");
        }
    }

    private void countMineNeighbors() {
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                GameObject gameObject = gameField[j][i];
                if(!gameObject.isMine()) {
                    for (GameObject neighbor: getNeighbors(gameObject)) {
                        if (neighbor.isMine()) {
                            gameObject.setCountMineNeighbors(1);
                        }
                    }
                }
            }
        }
    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.getY() - 1; y <= gameObject.getY() + 1; y++) {
            for (int x = gameObject.getX() - 1; x <= gameObject.getX() + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "БУМ!!! Вы проиграли!", Color.WHITE, 50);
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "Поздравляем! Вы победили!", Color.WHITE, 50);
    }

    private void restart() {
        gameField = new GameObject[SIDE][SIDE];
        isGameStopped = false;
        countClosedTiles = SIDE * SIDE;
        score = countMinesOnField = 0;
        setScore(score);
        createGame();

    }

    public Color getColor(int value) {
        return switch (value) {
            case 0 -> Color.NONE;
            case 1 -> Color.BLUE;
            case 2 -> Color.GREEN;
            case 3 -> Color.RED;
            default -> null;
        };
    }

    public void onTurn(int step) {
        setScore(score);
    }
}